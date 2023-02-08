/*
 * Tencent is pleased to support the open source community by making
 * MMKV available.
 *
 * Copyright (C) 2018 THL A29 Limited, a Tencent company.
 * All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *       https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.tmf.module.storage.kv;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tencent.mmkv.MMKV;
import com.tencent.mmkv.ParcelableMMKV;
import com.tencent.tmf.demo.IAshmemMMKV;

import java.util.Random;

public abstract class BenchMarkBaseService extends Service {
    public static final String CMD_ID = "cmd_id";
    public static final String CMD_READ_INT = "cmd_read_int";
    public static final String CMD_WRITE_INT = "cmd_write_int";
    public static final String CMD_READ_STRING = "cmd_read_string";
    public static final String CMD_WRITE_STRING = "cmd_write_string";
    public static final String CMD_PREPARE_ASHMEM_BY_CP = "cmd_prepare_ashmem_by_ContentProvider";
    public static final String CMD_PREPARE_ASHMEM_KEY = "cmd_prepare_ashmem_key";

    // 1M, ashmem cannot change size after opened
    public static final int ASHMEMMMKV_SIZE = 1024 * 1024;
    public static final String ASHMEMMMKV_ID = "tetAshmemMMKVByCP";

    private String[] mArrStrings;
    private String[] mArrKeys;
    private String[] mArrIntKeys;

    private static final int mLoops = 1000;
    public static final String MMKV_ID = "benchmark_interprocess";
    private static final String SP_ID = "benchmark_interprocess_sp";
    private static final String CryptKey = null;
    //private static final String CryptKey = "Tencent MMKV";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MMKV", "onCreate BenchMarkBaseService");

        MMKV.initialize(this);
        {
            long startTime = System.currentTimeMillis();

            MMKV mmkv = MMKV.mmkvWithID(MMKV_ID, MMKV.MULTI_PROCESS_MODE);

            long endTime = System.currentTimeMillis();
            Log.i("MMKV", "load [" + MMKV_ID + "]: " + (endTime - startTime) + " ms");
        }
        mArrStrings = new String[mLoops];
        mArrKeys = new String[mLoops];
        mArrIntKeys = new String[mLoops];
        Random r = new Random();
        for (int index = 0; index < mLoops; index++) {
            mArrStrings[index] = "MMKV-" + r.nextInt();
            mArrKeys[index] = "testStr_" + index;
            mArrIntKeys[index] = "int_" + index;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MMKV", "onDestroy BenchMarkBaseService");

        MMKV.onExit();
    }

    protected void batchWriteInt(String caller) {
        mmkvBatchWriteInt(caller);
        sqliteWriteInt(caller);
        spBatchWriteInt(caller);
    }

    private void mmkvBatchWriteInt(String caller) {
        Random r = new Random();
        long startTime = System.currentTimeMillis();

        MMKV mmkv = getMMKV();
        for (int index = 0; index < mLoops; index++) {
            int tmp = r.nextInt();
            String key = mArrIntKeys[index];
            mmkv.encode(key, tmp);
        }
        long endTime = System.currentTimeMillis();
        Log.i("MMKV",
                caller + " mmkv write int: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    private void sqliteWriteInt(String caller) {
        Random r = new Random();
        long startTime = System.currentTimeMillis();

        SQLIteKV sqlIteKV = new SQLIteKV(this);
        //sqlIteKV.beginTransaction();
        for (int index = 0; index < mLoops; index++) {
            int tmp = r.nextInt();
            String key = mArrIntKeys[index];
            sqlIteKV.putInt(key, tmp);
        }
        //sqlIteKV.endTransaction();
        long endTime = System.currentTimeMillis();
        Log.i("MMKV", caller + " sqlite write int: loop[" + mLoops + "]: " + (endTime - startTime)
                + " ms");
    }

    private void spBatchWriteInt(String caller) {
        Random r = new Random();
        long startTime = System.currentTimeMillis();

        SharedPreferences preferences =
                MultiProcessSharedPreferences.getSharedPreferences(this, SP_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (int index = 0; index < mLoops; index++) {
            int tmp = r.nextInt();
            String key = mArrIntKeys[index];
            editor.putInt(key, tmp);
            //editor.commit();
            editor.apply();
        }
        long endTime = System.currentTimeMillis();
        Log.i("MMKV", caller + " MultiProcessSharedPreferences write int: loop[" + mLoops
                + "]: " + (endTime - startTime) + " ms");
    }

    protected void batchReadInt(String caller) {
        mmkvBatchReadInt(caller);
        sqliteReadInt(caller);
        spBatchReadInt(caller);
    }

    private void mmkvBatchReadInt(String caller) {
        long startTime = System.currentTimeMillis();

        MMKV mmkv = getMMKV();
        for (int index = 0; index < mLoops; index++) {
            String key = mArrIntKeys[index];
            int tmp = mmkv.decodeInt(key);
        }
        long endTime = System.currentTimeMillis();
        Log.i("MMKV",
                caller + " mmkv read int: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    private void sqliteReadInt(String caller) {
        long startTime = System.currentTimeMillis();

        SQLIteKV sqlIteKV = new SQLIteKV(this);
        //sqlIteKV.beginTransaction();
        for (int index = 0; index < mLoops; index++) {
            String key = mArrIntKeys[index];
            int tmp = sqlIteKV.getInt(key);
        }
        //sqlIteKV.endTransaction();
        long endTime = System.currentTimeMillis();
        Log.i("MMKV",
                caller + " sqlite read int: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    private void spBatchReadInt(String caller) {
        long startTime = System.currentTimeMillis();

        SharedPreferences preferences =
                MultiProcessSharedPreferences.getSharedPreferences(this, SP_ID, MODE_PRIVATE);
        for (int index = 0; index < mLoops; index++) {
            String key = mArrIntKeys[index];
            int tmp = preferences.getInt(key, 0);
        }
        long endTime = System.currentTimeMillis();
        Log.i("MMKV", caller + " MultiProcessSharedPreferences read int: loop[" + mLoops
                + "]: " + (endTime - startTime) + " ms");
    }

    protected void batchWriteString(String caller) {
        mmkvBatchWriteString(caller);
        sqliteWriteString(caller);
        spBatchWrieString(caller);
    }

    private void mmkvBatchWriteString(String caller) {
        long startTime = System.currentTimeMillis();

        MMKV mmkv = getMMKV();
        for (int index = 0; index < mLoops; index++) {
            final String valueStr = mArrStrings[index];
            final String strKey = mArrKeys[index];
            mmkv.encode(strKey, valueStr);
        }
        long endTime = System.currentTimeMillis();
        Log.i("MMKV", caller + " mmkv write String: loop[" + mLoops + "]: " + (endTime - startTime)
                + " ms");
    }

    private void sqliteWriteString(String caller) {
        long startTime = System.currentTimeMillis();

        SQLIteKV sqlIteKV = new SQLIteKV(this);
        //sqlIteKV.beginTransaction();
        for (int index = 0; index < mLoops; index++) {
            final String value = mArrStrings[index];
            final String key = mArrKeys[index];
            sqlIteKV.putString(key, value);
        }
        //sqlIteKV.endTransaction();
        long endTime = System.currentTimeMillis();
        Log.i("MMKV", caller + " sqlite write String: loop[" + mLoops
                + "]: " + (endTime - startTime) + " ms");
    }

    private void spBatchWrieString(String caller) {
        long startTime = System.currentTimeMillis();

        SharedPreferences preferences =
                MultiProcessSharedPreferences.getSharedPreferences(this, SP_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (int index = 0; index < mLoops; index++) {
            final String str = mArrStrings[index];
            final String key = mArrKeys[index];
            editor.putString(key, str);
            //editor.commit();
            editor.apply();
        }
        long endTime = System.currentTimeMillis();
        Log.i("MMKV", caller + " MultiProcessSharedPreferences write String: loop[" + mLoops
                + "]: " + (endTime - startTime) + " ms");
    }

    protected void batchReadString(String caller) {
        mmkvBatchReadString(caller);
        sqliteReadString(caller);
        spBatchReadStrinfg(caller);
    }

    private void mmkvBatchReadString(String caller) {
        long startTime = System.currentTimeMillis();

        MMKV mmkv = getMMKV();
        for (int index = 0; index < mLoops; index++) {
            final String strKey = mArrKeys[index];
            final String tmpStr = mmkv.decodeString(strKey);
        }
        long endTime = System.currentTimeMillis();
        Log.i("MMKV", caller + " mmkv read String: loop[" + mLoops + "]: " + (endTime - startTime)
                + " ms");
    }

    private void sqliteReadString(String caller) {
        long startTime = System.currentTimeMillis();

        SQLIteKV sqlIteKV = new SQLIteKV(this);
        //sqlIteKV.beginTransaction();
        for (int index = 0; index < mLoops; index++) {
            final String key = mArrKeys[index];
            final String tmp = sqlIteKV.getString(key);
        }
        //sqlIteKV.endTransaction();
        long endTime = System.currentTimeMillis();
        Log.i("MMKV", caller + " sqlite read String: loop[" + mLoops
                + "]: " + (endTime - startTime) + " ms");
    }

    private void spBatchReadStrinfg(String caller) {
        long startTime = System.currentTimeMillis();

        SharedPreferences preferences =
                MultiProcessSharedPreferences.getSharedPreferences(this, SP_ID, MODE_PRIVATE);
        for (int index = 0; index < mLoops; index++) {
            final String key = mArrKeys[index];
            final String tmp = preferences.getString(key, null);
        }
        long endTime = System.currentTimeMillis();
        Log.i("MMKV", caller + " MultiProcessSharedPreferences read String: loop[" + mLoops
                + "]: " + (endTime - startTime) + " ms");
    }

    MMKV mAshmemMMKV;

    protected MMKV getMMKV() {
        if (mAshmemMMKV != null) {
            return mAshmemMMKV;
        } else {
            return MMKV.mmkvWithID(MMKV_ID, MMKV.MULTI_PROCESS_MODE, CryptKey);
        }
    }

    public class AshmemMMKVGetter extends IAshmemMMKV.Stub {

        private AshmemMMKVGetter() {
            // 1M, ashmem cannot change size after opened
            final String id = "tetAshmemMMKV";
            mAshmemMMKV = MMKV.mmkvWithAshmemID(BenchMarkBaseService.this, id, ASHMEMMMKV_SIZE,
                    MMKV.MULTI_PROCESS_MODE, CryptKey);
            mAshmemMMKV.encode("bool", true);
        }

        @SuppressWarnings("checkstyle:MethodName")
        public ParcelableMMKV GetAshmemMMKV() {
            return new ParcelableMMKV(mAshmemMMKV);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MMKV", "onBind, intent=" + intent);
        return new AshmemMMKVGetter();
    }

    protected void prepareAshmemMMKVByCP() {
        // it's ok for other process not knowing cryptKey
        final String cryptKey = null;
        mAshmemMMKV = MMKV.mmkvWithAshmemID(this, ASHMEMMMKV_ID, ASHMEMMMKV_SIZE,
                MMKV.MULTI_PROCESS_MODE, cryptKey);
    }
}

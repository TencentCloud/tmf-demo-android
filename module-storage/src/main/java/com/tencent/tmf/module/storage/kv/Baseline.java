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

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.tencent.mmkv.MMKV;
import java.util.Random;

public final class Baseline {

    private String[] mArrStrings;
    private String[] mArrKeys;
    private String[] mArrIntKeys;
    private int mLoops = 1000;
    private Context mContext;
    private static final String MMKV_ID = "baseline3";
    private static final String CryptKey = null;
    //private static final String CryptKey = "baseline_key3";

    Baseline(Context context, int loops) {
        mContext = context;
        mLoops = loops;

        mArrStrings = new String[loops];
        mArrKeys = new String[loops];
        mArrIntKeys = new String[loops];
        Random r = new Random();
        for (int index = 0; index < loops; index++) {
            mArrStrings[index] = "MMKV-" + r.nextInt();
            mArrKeys[index] = "str_" + index;
            mArrIntKeys[index] = "int_" + index;
        }
    }

    public void mmkvBaselineTest() {
        mmkvBatchWriteInt();
        mmkvBatchReadInt();
        mmkvBatchWriteString();
        mmkvBatchReadString();

        //mmkvBatchDeleteString();
        //MMKV mmkv = MMKV.mmkvWithID(MMKV_ID, MMKV.SINGLE_PROCESS_MODE, CryptKey);
        //mmkv.trim();
    }

    private void mmkvBatchWriteInt() {
        Random r = new Random();
        long startTime = System.currentTimeMillis();

        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID, MMKV.SINGLE_PROCESS_MODE, CryptKey);
        for (int index = 0; index < mLoops; index++) {
            int tmp = r.nextInt();
            String key = mArrIntKeys[index];
            mmkv.encode(key, tmp);
        }
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "MMKV write int: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    private void mmkvBatchReadInt() {
        long startTime = System.currentTimeMillis();

        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID, MMKV.SINGLE_PROCESS_MODE, CryptKey);
        for (int index = 0; index < mLoops; index++) {
            String key = mArrIntKeys[index];
            int tmp = mmkv.decodeInt(key);
        }
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "MMKV read int: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    private void mmkvBatchWriteString() {
        long startTime = System.currentTimeMillis();

        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID, MMKV.SINGLE_PROCESS_MODE, CryptKey);
        for (int index = 0; index < mLoops; index++) {
            final String valueStr = mArrStrings[index];
            final String strKey = mArrKeys[index];
            mmkv.encode(strKey, valueStr);
        }
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "MMKV write String: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    private void mmkvBatchReadString() {
        long startTime = System.currentTimeMillis();

        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID, MMKV.SINGLE_PROCESS_MODE, CryptKey);
        for (int index = 0; index < mLoops; index++) {
            String strKey = mArrKeys[index];
            String tmpStr = mmkv.decodeString(strKey);
        }
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "MMKV read String: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    private void mmkvBatchDeleteString() {
        long startTime = System.currentTimeMillis();

        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID, MMKV.SINGLE_PROCESS_MODE, CryptKey);
        for (int index = 0; index < mLoops; index++) {
            String strKey = mArrKeys[index];
            mmkv.removeValueForKey(strKey);
        }
        long endTime = System.currentTimeMillis();

        Log.i("MMKV",
                "MMKV delete String: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    public void sharedPreferencesBaselineTest() {
        spBatchWriteInt();
        spBatchReadInt();
        spBatchWrieString();
        spBatchReadStrinfg();
    }

    private void spBatchWriteInt() {
        Random r = new Random();
        long startTime = System.currentTimeMillis();

        SharedPreferences preferences = mContext.getSharedPreferences(MMKV_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (int index = 0; index < mLoops; index++) {
            int tmp = r.nextInt();
            String key = mArrIntKeys[index];
            editor.putInt(key, tmp);
            //            editor.commit();
            editor.apply();
        }
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "SharedPreferences write int: loop[" + mLoops + "]: " + (endTime - startTime)
                + " ms");
    }

    private void spBatchReadInt() {
        long startTime = System.currentTimeMillis();

        SharedPreferences preferences = mContext.getSharedPreferences(MMKV_ID, MODE_PRIVATE);
        for (int index = 0; index < mLoops; index++) {
            String key = mArrIntKeys[index];
            int tmp = preferences.getInt(key, 0);
        }
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "SharedPreferences read int: loop[" + mLoops + "]: " + (endTime - startTime)
                + " ms");
    }

    private void spBatchWrieString() {
        long startTime = System.currentTimeMillis();

        SharedPreferences preferences = mContext.getSharedPreferences(MMKV_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (int index = 0; index < mLoops; index++) {
            final String str = mArrStrings[index];
            final String key = mArrKeys[index];
            editor.putString(key, str);
            //            editor.commit();
            editor.apply();
        }
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "SharedPreferences write String: loop[" + mLoops
                + "]: " + (endTime - startTime) + " ms");
    }

    private void spBatchReadStrinfg() {
        long startTime = System.currentTimeMillis();

        SharedPreferences preferences = mContext.getSharedPreferences(MMKV_ID, MODE_PRIVATE);
        for (int index = 0; index < mLoops; index++) {
            final String key = mArrKeys[index];
            final String tmp = preferences.getString(key, null);
        }
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "SharedPreferences read String: loop[" + mLoops
                + "]: " + (endTime - startTime) + " ms");
    }

    public void sqliteBaselineTest() {
        sqliteWriteInt();
        sqliteReadInt();
        sqliteWriteString();
        sqliteReadString();
    }

    @SuppressWarnings({"checkstyle:VariableDeclarationUsageDistance"})
    private void sqliteWriteInt() {
        Random r = new Random();
        long startTime = System.currentTimeMillis();

        SQLIteKV sqlIteKV = new SQLIteKV(mContext);
        sqlIteKV.beginTransaction();
        for (int index = 0; index < mLoops; index++) {
            int tmp = r.nextInt();
            String key = mArrIntKeys[index];
            sqlIteKV.putInt(key, tmp);
        }
        sqlIteKV.endTransaction();
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "sqlite write int: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    private void sqliteReadInt() {
        long startTime = System.currentTimeMillis();

        SQLIteKV sqlIteKV = new SQLIteKV(mContext);
        sqlIteKV.beginTransaction();
        for (int index = 0; index < mLoops; index++) {
            String key = mArrIntKeys[index];
            int tmp = sqlIteKV.getInt(key);
        }
        sqlIteKV.endTransaction();
        long endTime = System.currentTimeMillis();

        Log.i("MMKV", "sqlite read int: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    private void sqliteWriteString() {
        long startTime = System.currentTimeMillis();

        SQLIteKV sqlIteKV = new SQLIteKV(mContext);
        sqlIteKV.beginTransaction();
        for (int index = 0; index < mLoops; index++) {
            final String value = mArrStrings[index];
            final String key = mArrKeys[index];
            sqlIteKV.putString(key, value);
        }
        sqlIteKV.endTransaction();
        long endTime = System.currentTimeMillis();

        Log.i("MMKV",
                "sqlite write String: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    private void sqliteReadString() {
        long startTime = System.currentTimeMillis();

        SQLIteKV sqlIteKV = new SQLIteKV(mContext);
        sqlIteKV.beginTransaction();
        for (int index = 0; index < mLoops; index++) {
            final String key = mArrKeys[index];
            final String tmp = sqlIteKV.getString(key);
        }
        sqlIteKV.endTransaction();
        long endTime = System.currentTimeMillis();

        Log.i("MMKV",
                "sqlite read String: loop[" + mLoops + "]: " + (endTime - startTime) + " ms");
    }
}

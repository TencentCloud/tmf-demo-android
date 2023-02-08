/*
 * Tencent is pleased to support the open source community by making
 * WCDB available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company.
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

package com.tencent.tmf.module.storage.db.encryptdb;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.storage.R;
import com.tencent.tmf.module.storage.StorageActivity;
import com.tencent.tmf.module.storage.StorageSp;
import com.tencent.tmf.storage.TMFDatabase;
import com.tencent.tmf.storage.TMFStorage;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EncryptDbActivity extends TopBarActivity {

    private static final String TAG = "WCDB.EncryptDBSample";

    private TMFDatabase mTMFDatabase;
    private SQLiteDatabase mDB;
    private SQLiteOpenHelper mDBHelper;
    private int mDBVersion;

    private ListView mListView;
    private SimpleCursorAdapter mAdapter;
    private Button mPlainBtn;
    private Button mEncryptedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(R.string.db_encrypt);

        String storageType = getIntent().getStringExtra(StorageActivity.KEY_STORAGE_TYPE);
        String storageUID = getIntent().getStringExtra(StorageActivity.KEY_STORAGE_UID);
        if (StorageActivity.STORAGE_TYPE_TEMP.equals(storageType)) {
            mTMFDatabase = TMFStorage.getTemporary().database();
            ;
        } else if (StorageActivity.STORAGE_TYPE_USER.equals(storageType) && !TextUtils.isEmpty(storageUID)) {
            mTMFDatabase = TMFStorage.getByUser(storageUID).database();
        } else {
            mTMFDatabase = TMFStorage.getDefault().database();
        }

        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new SimpleCursorAdapter(this, R.layout.encryptdb_listitem, null,
                new String[]{"content", "_id", "sender"},
                new int[]{R.id.list_tv_content, R.id.list_tv_id, R.id.list_tv_sender},
                0);

        mListView.setAdapter(mAdapter);

        mPlainBtn = findViewById(R.id.btn_init_plain);
        mEncryptedBtn = findViewById(R.id.btn_init_encrypted);
        if (StorageSp.getInstance().getDbEncrypt()) {
            mPlainBtn.setVisibility(View.GONE);
            mEncryptedBtn.setText(getStringById(R.string.module_storage_16));
        }
        mPlainBtn.setOnClickListener(new View.OnClickListener() {
            // Init plain-text button pressed.
            // Create or open database in version 1, then refresh adapter.

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Cursor>() {
                    @Override
                    protected void onPreExecute() {
                        mAdapter.changeCursor(null);
                    }

                    @Override
                    protected Cursor doInBackground(Void... params) {
                        if (mDBHelper != null && mDB != null && mDB.isOpen()) {
                            mDBHelper.close();
                            mDBHelper = null;
                            mDB = null;
                        }
                        mDBHelper = mTMFDatabase.createWCDBHelper(PlainTextDB.DATABASE_NAME, null,
                                PlainTextDB.DATABASE_VERSION, new PlainTextDB());
                        mDBHelper.setWriteAheadLoggingEnabled(true);
                        mDB = mDBHelper.getWritableDatabase();
                        mDBVersion = mDB.getVersion();
                        return mDB.rawQuery("SELECT rowid as _id, content, 'Plain' as sender FROM message;",
                                null);
                    }

                    @Override
                    protected void onPostExecute(Cursor cursor) {
                        mAdapter.changeCursor(cursor);
                    }
                }.execute();
            }
        });

        mEncryptedBtn.setOnClickListener(new View.OnClickListener() {
            // Init encrypted button pressed.
            // Create or open database in version 2, then refresh adapter.
            // If plain-text database exists and encrypted one does not, transfer all
            // data from the plain-text database (which in version 1), then upgrade it
            // to version 2.

            // See EncryptedDB.java for details about data transfer and schema upgrade.

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                mPlainBtn.setVisibility(View.GONE);
                mEncryptedBtn.setText(getStringById(R.string.module_storage_16));
                new AsyncTask<Void, Void, Cursor>() {
                    @Override
                    protected void onPreExecute() {
                        mAdapter.changeCursor(null);
                    }

                    @Override
                    protected Cursor doInBackground(Void... params) {
                        if (mDBHelper != null && mDB != null && mDB.isOpen()) {
                            mDBHelper.close();
                            mDBHelper = null;
                            mDB = null;
                        }
                        String passphrase = "passphrase";
                        mDBHelper = mTMFDatabase.createWCDBHelper(EncryptedDB.DATABASE_NAME, passphrase.getBytes(), null,
                                EncryptedDB.DATABASE_VERSION, null, new EncryptedDB(EncryptDbActivity.this,
                                        passphrase, mTMFDatabase));
                        mDBHelper.setWriteAheadLoggingEnabled(true);
                        mDB = mDBHelper.getWritableDatabase();
                        mDBVersion = mDB.getVersion();
                        return mDB.rawQuery("SELECT rowid as _id, content, sender FROM message;",
                                null);
                    }

                    @Override
                    protected void onPostExecute(Cursor cursor) {
                        StorageSp.getInstance().putDbEncrypt(true);
                        mAdapter.changeCursor(cursor);
                    }
                }.execute();
            }
        });

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            // Insert button pressed.
            // Insert a message to the database.

            // To test data transfer, init plain-text database, insert messages,
            // then init encrypted database.

            final DateFormat dateFORMAT = SimpleDateFormat.getDateTimeInstance();

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Cursor>() {
                    @Override
                    protected void onPreExecute() {
                        mAdapter.changeCursor(null);
                    }

                    @Override
                    protected Cursor doInBackground(Void... params) {
                        if (mDB == null || !mDB.isOpen()) {
                            return null;
                        }

                        String message = "Message inserted on " + dateFORMAT.format(new Date());

                        if (mDBVersion == 1) {
                            mDB.execSQL("INSERT INTO message VALUES (?);",
                                    new Object[]{message});
                            return mDB.rawQuery("SELECT rowid as _id, content, 'Plain' as sender FROM message;",
                                    null);
                        } else {
                            mDB.execSQL("INSERT INTO message VALUES (?, ?);",
                                    new Object[]{message, "Encrypt"});
                            return mDB.rawQuery("SELECT rowid as _id, content, sender FROM message;",
                                    null);
                        }
                    }

                    @Override
                    protected void onPostExecute(Cursor cursor) {
                        if (cursor == null) {
                            return;
                        }
                        mAdapter.changeCursor(cursor);
                    }
                }.execute();
            }
        });
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_encryptdb, null);
    }
}

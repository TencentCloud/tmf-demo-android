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

package com.tencent.tmf.module.storage.db.repairdb;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.storage.R;
import com.tencent.tmf.module.storage.StorageActivity;
import com.tencent.tmf.storage.TMFDatabase;
import com.tencent.tmf.storage.TMFStorage;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteException;
import com.tencent.wcdb.database.SQLiteOpenHelper;
import com.tencent.wcdb.repair.RepairKit;
import com.tencent.wcdb.support.CancellationSignal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Random;

public class RepairDbActivity extends TopBarActivity implements View.OnClickListener {

    private static final String TAG = "WCDB.RepairDBSample";

    private SQLiteDatabase mDB;
    private SQLiteOpenHelper mDBHelper;
    private TMFDatabase mTMFDatabase;

    private ListView mListView;
    private SimpleCursorAdapter mAdapter;
    private RepairKit mRepair;
    private CancellationSignal mCancellationSignal;
    private Button mCancelButton;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mDBHelper = mTMFDatabase.createWCDBHelper(DBCallback.DATABASE_NAME, DBCallback.PASSPHRASE, DBCallback.CIPHER_SPEC,
                null, DBCallback.DATABASE_VERSION, DBCallback.ERROR_HANDLER, new DBCallback());

        // Extract test database from assets.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                File dbFile = mTMFDatabase.getDatabasePath(DBCallback.DATABASE_NAME);

                if (!dbFile.exists()) {
                    dbFile.getParentFile().mkdirs();

                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        byte[] buffer = new byte[1024];
                        in = getAssets().open(DBCallback.DATABASE_NAME);
                        out = new FileOutputStream(dbFile);
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return null;
            }
        }.execute();

        mTopBar.setTitle(R.string.db_repair);

        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new SimpleCursorAdapter(this, R.layout.repairdb_listitem, null,
                new String[]{"a", "b"},
                new int[]{R.id.list_tv_a, R.id.list_tv_b},
                0);

        mListView.setAdapter(mAdapter);

        mCancelButton = (Button) findViewById(R.id.btn_repair_cancel);
        mCancelButton.setOnClickListener(this);
        findViewById(R.id.btn_init_db).setOnClickListener(this);
        findViewById(R.id.btn_corrupt_db).setOnClickListener(this);
        findViewById(R.id.btn_repair_db).setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_repairdb, null);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_init_db) {
            initDB();
        } else if (id == R.id.btn_corrupt_db) {
            corruptDB();
        } else if (id == R.id.btn_repair_db) {
            repairDB();
        } else if (id == R.id.btn_repair_cancel) {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void initDB() {
        new AsyncTask<Void, Void, SQLiteException>() {
            @Override
            protected void onPreExecute() {
                mAdapter.changeCursor(null);
            }

            @Override
            protected SQLiteException doInBackground(Void... params) {
                if (mDB != null && mDB.isOpen()) {
                    mDBHelper.close();
                    mDB = null;
                }

                try {
                    mDBHelper.setWriteAheadLoggingEnabled(true);
                    mDB = mDBHelper.getWritableDatabase();

                    // After successfully opened the database, backup its master info.
                    RepairKit.MasterInfo.save(mDB, mDB.getPath() + "-mbak", DBCallback.PASSPHRASE);
                } catch (SQLiteException e) {
                    // Failed to open database, probably due to corruption.
                    mDB = null;
                    return e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(SQLiteException e) {
                if (e == null) {
                    // Database is successfully opened, query and refresh ListView.
                    Cursor cursor = mDB.rawQuery("SELECT rowid as _id, a, b FROM t1;",
                            null);
                    mAdapter.changeCursor(cursor);

                    Toast.makeText(RepairDbActivity.this, "Database is successfully opened.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Database could not be opened, show toast.
                    Toast.makeText(RepairDbActivity.this, "Database cannot be opened, exception: "
                            + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void corruptDB() {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected void onPreExecute() {
                mAdapter.changeCursor(null);
            }

            @Override
            protected Exception doInBackground(Void... params) {
                if (mDB != null && mDB.isOpen()) {
                    mDBHelper.close();
                    mDB = null;
                }

                // Write random noise to the first page to corrupt the database.
                RandomAccessFile raf = null;
                try {
                    File dbFile = mTMFDatabase.getDatabasePath(DBCallback.DATABASE_NAME);
                    raf = new RandomAccessFile(dbFile, "rw");
                    byte[] buffer = new byte[1024];
                    new Random().nextBytes(buffer);
                    raf.seek(0);
                    raf.write(buffer);
                } catch (IOException e) {
                    return e;
                } finally {
                    if (raf != null) {
                        try {
                            raf.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Exception e) {
                if (e == null) {
                    Toast.makeText(RepairDbActivity.this, "Database is now CORRUPTED!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RepairDbActivity.this, "Unable to overwrite database: "
                            + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void repairDB() {
        Toast.makeText(RepairDbActivity.this, getStringById(R.string.module_storage_40), Toast.LENGTH_LONG).show();
        new AsyncTask<Void, Void, SQLiteException>() {
            @Override
            protected void onPreExecute() {
                mAdapter.changeCursor(null);
                mCancellationSignal = new CancellationSignal();
                mCancelButton.setEnabled(true);
            }

            @Override
            protected SQLiteException doInBackground(Void... params) {
                if (mDB != null && mDB.isOpen()) {
                    mDBHelper.close();
                    mDB = null;
                }

                RepairKit.MasterInfo master = null;
                File dbFile = mTMFDatabase.getDatabasePath(DBCallback.DATABASE_NAME);
                File masterFile = new File(dbFile.getPath() + "-mbak");
                File newDbFile = mTMFDatabase.getDatabasePath(DBCallback.DATABASE_NAME + "-recover");

                if (masterFile.exists()) {
                    try {
                        master = RepairKit.MasterInfo.load(masterFile.getPath(),
                                DBCallback.PASSPHRASE, null);
                    } catch (SQLiteException e) {
                        // Could not load master info. Maybe backup file itself corrupted?
                    }
                }

                mRepair = null;
                try {
                    mRepair = new RepairKit(
                            dbFile.getPath(),       // corrupted database file
                            DBCallback.PASSPHRASE,    // passphrase to the database
                            DBCallback.CIPHER_SPEC,   // cipher spec to the database
                            master                  // backup master info just loaded
                    );

                    if (newDbFile.exists()) {
                        newDbFile.delete();
                    }

                    SQLiteDatabase newDb = SQLiteDatabase.openOrCreateDatabase(newDbFile,
                            DBCallback.PASSPHRASE, DBCallback.CIPHER_SPEC, null,
                            DBCallback.ERROR_HANDLER);
                    mRepair.setCallback(new RepairKit.Callback() {
                        @Override
                        public int onProgress(String table, int root, Cursor cursor) {
                            Log.d(TAG, String.format("table: %s, root: %d, count: %d",
                                    table, root, cursor.getColumnCount()));
                            return RepairKit.RESULT_OK;
                        }
                    });
                    int result = mRepair.output(newDb, 0);
                    if (result != RepairKit.RESULT_OK && result != RepairKit.RESULT_CANCELED) {
                        throw new SQLiteException("Repair returns failure.");
                    }

                    newDb.setVersion(DBCallback.DATABASE_VERSION);
                    newDb.close();
                    mRepair.release();
                    mRepair = null;

                    if (!dbFile.delete() || !newDbFile.renameTo(dbFile)) {
                        throw new SQLiteException("Cannot rename database.");
                    }
                } catch (SQLiteException e) {
                    return e;
                } finally {
                    if (mRepair != null) {
                        mRepair.release();
                        mRepair = null;
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(SQLiteException e) {
                if (e == null) {
                    mDB = mDBHelper.getReadableDatabase();
                    Cursor cursor = mDB.rawQuery("SELECT rowid as _id, a, b FROM t1;",
                            null);
                    mAdapter.changeCursor(cursor);
                    Toast.makeText(RepairDbActivity.this, "Repair succeeded.", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(RepairDbActivity.this, "Repair failed: "
                            + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                mCancelButton.setEnabled(false);
                mCancellationSignal = null;
            }
        }.execute();
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
}

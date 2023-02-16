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

import android.content.Context;
import android.util.Log;
import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.storage.TMFDatabase;
import com.tencent.tmf.storage.TMFWCDBOpenHelper;
import com.tencent.wcdb.DatabaseUtils;
import com.tencent.wcdb.database.SQLiteChangeListener;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;
import com.tencent.wcdb.repair.RepairKit;
import java.io.File;


public class EncryptedDB extends TMFWCDBOpenHelper.DatabaseCallback {

    public static final String TAG = "EncryptedDBHelper";

    public static final String DATABASE_NAME = "encrypted.db";
    public static final String OLD_DATABASE_NAME = "plain-text.db";
    public static final int DATABASE_VERSION = 2;

    private Context mContext;
    private String mPassphrase;
    private TMFDatabase mTMFDatabase;

    public EncryptedDB(Context context, String passphrase, TMFDatabase database) {
        // Save context object for later use.
        mContext = context;
        mPassphrase = passphrase;
        mTMFDatabase = database;
    }

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Check whether old plain-text database exists, if so, export it
        // to the new, encrypted one.
        File oldDbFile = mTMFDatabase.getDatabasePath(OLD_DATABASE_NAME);
        if (oldDbFile.exists()) {

            Log.i(TAG, "Migrating plain-text database to encrypted one.");

            // SQLiteOpenHelper begins a transaction before calling onCreate().
            // We have to end the transaction before we can attach a new database.
            db.endTransaction();

            // Attach old database to the newly created, encrypted database.
            String sql = String.format("ATTACH DATABASE %s AS old KEY '';",
                    DatabaseUtils.sqlEscapeString(oldDbFile.getPath()));
            db.execSQL(sql);

            // Export old database.
            db.beginTransaction();
            DatabaseUtils.stringForQuery(db, "SELECT sqlcipher_export('main', 'old');", null);
            db.setTransactionSuccessful();
            db.endTransaction();

            // Get old database version for later upgrading.
            int oldVersion = (int) DatabaseUtils.longForQuery(db, "PRAGMA old.user_version;", null);

            // Detach old database and enter a new transaction.
            db.execSQL("DETACH DATABASE old;");

            // Old database can be deleted now.
            mTMFDatabase.dropDatabase(OLD_DATABASE_NAME);

            // Before further actions, restore the transaction.
            db.beginTransaction();

            // Check if we need to upgrade the schema.
            if (oldVersion > DATABASE_VERSION) {
                onDowngrade(db, oldVersion, DATABASE_VERSION);
            } else if (oldVersion < DATABASE_VERSION) {
                onUpgrade(db, oldVersion, DATABASE_VERSION);
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "Creating new encrypted database.");
            }

            // Do the real initialization if the old database is absent.
            db.execSQL("CREATE TABLE message (content TEXT, "
                    + "sender TEXT);");
        }

        // OPTIONAL: backup master info for corruption recovery.
        RepairKit.MasterInfo.save(db, db.getPath() + "-mbak", mPassphrase.getBytes());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (BuildConfig.DEBUG) {
            Log.i(TAG, String.format("Upgrading database from version %d to version %d.",
                    oldVersion, newVersion));
        }

        // Add new column to message table on database upgrade.
        db.execSQL("ALTER TABLE message ADD COLUMN sender TEXT;");

        // OPTIONAL: backup master info for corruption recovery.
        RepairKit.MasterInfo.save(db, db.getPath() + "-mbak", mPassphrase.getBytes());
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setAsyncCheckpointEnabled(true);
        db.setChangeListener(new SQLiteChangeListener() {

            private StringBuilder mSB = new StringBuilder();

            private void printIds(String prefix, long[] ids) {
                mSB.append(prefix).append(": ");
                for (long id : ids) {
                    mSB.append(id).append(", ");
                }
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, mSB.toString());
                }
                mSB.setLength(0);
            }

            @Override
            public void onChange(SQLiteDatabase db, String dbName, String table,
                    long[] insertIds, long[] updateIds, long[] deleteIds) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "onChange called: dbName = " + dbName + ", table = " + table);
                }
                printIds("INSERT", insertIds);
                printIds("UPDATE", updateIds);
                printIds("DELETE", deleteIds);
            }
        }, true);
    }
}

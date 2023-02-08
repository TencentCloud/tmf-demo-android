package com.tencent.tmf.module.storage.db.cruddb;

import android.content.Context;
import android.util.Log;

import com.tencent.tmf.storage.TMFWCDBOpenHelper;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;
import java.io.File;

/**
 * 简单的Helper
 */
public class PersonDBCallback extends TMFWCDBOpenHelper.DatabaseCallback {

    /**
     * 表创建
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS person (_id INTEGER PRIMARY KEY AUTOINCREMENT , name "
                + "VARCHAR(20) , address TEXT)";
        db.execSQL(SQL_CREATE);
    }

    /**
     * 版本升级
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
        Log.d("WCDB", "db older ver" + oldVersion);
        Log.d("WCDB", "db latest ver " + newVersion);
    }
}
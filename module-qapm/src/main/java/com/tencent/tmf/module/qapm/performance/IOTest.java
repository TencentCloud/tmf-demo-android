package com.tencent.tmf.module.qapm.performance;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.tencent.tmf.common.BuildConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by toringzhang on 2017/12/11.
 */

public class IOTest {

    private Context mContext;
    private final String logTAG = "IOTest";
    private SQLiteDatabase sqliteDB = null;

    public IOTest(Context context) {
        mContext = context;
    }

    public void fileTest() {
        if (BuildConfig.DEBUG) {
            Log.d(logTAG, "file I/O case");
        }
        File filep;
        File filedir;
        String testFile = "testFile_" + String.valueOf(System.currentTimeMillis()) + ".txt";

        String filePath = mContext.getFilesDir().getAbsolutePath();
        filedir = new File(filePath);
        if (!filedir.exists()) {
            filedir.mkdir();
        }
        try {
            filep = new File(filedir + "/" + testFile);
            if (!filep.exists()) {
                try {
                    filep.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String str = "testing file io iii";
            byte[] bt = new byte[1024];
            bt = str.getBytes();
            for (int i = 0; i < 500; i++) {
                FileOutputStream in = null;
                try {
                    in = new FileOutputStream(filep);
                    in.write(bt, 0, bt.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sqLiteDatabaseTest() {
        if (BuildConfig.DEBUG) {
            Log.d(logTAG, "Sqlite test case");
        }
        File dbf;
        File dbp;
        String dbPath;
        String qqDB = "2872971611.db";

        String filePath = mContext.getFilesDir().getAbsolutePath();
        dbPath = filePath.replace("files", "databases");
        dbp = new File(dbPath);
        if (!dbp.exists()) {
            dbp.mkdir();
        }

        boolean isExist = true;
        try {
            dbf = new File(dbPath + "/" + qqDB);
            if (!dbf.exists()) {
                isExist = false;
                try {
                    dbf.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (sqliteDB == null) {
                sqliteDB = SQLiteDatabase.openOrCreateDatabase(dbf, null);
            }

            if (!isExist) {
                String sqlCreate = "create table if not exists events(_id INTEGER PRIMARY KEY, content TEXT, status "
                        + "INTEGER, send_count INTEGER, timestamp INTEGER)";
                sqliteDB.execSQL(sqlCreate);
            }

            sqliteDB.beginTransaction();
            String sql = "insert into events  (content, status, send_count, timestamp) values ('test', 0," + String
                    .valueOf((int) (Math.random() * 100)) + " , 31)";

            try {

                for (int i = 0; i < 10; i++) {
                    sqliteDB.execSQL(sql);
                }
                ContentValues values = new ContentValues();
                long curtime = 0;
                //这里设置60000循环可以验证是否有引用泄漏
                for (int j = 0; j < 10; j++) {
                    values.put("status", j);
                    sqliteDB.update("events", values, "content=?", new String[]{"test"});
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sqliteDB.endTransaction();
            }

            sqliteDB.close();
            sqliteDB = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StackTraceElement[] getStackTrace() {
        HandlerThread ht = new HandlerThread("IOTest2222");
        ht.start();
        Handler h = new Handler(ht.getLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    for (int i = 0; i < 3; i++) {
                        fileTest();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        return ht.getStackTrace();
    }

}

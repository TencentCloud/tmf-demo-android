package com.tencent.tmf.module.storage.db.cruddb;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.tencent.tmf.storage.TMFDatabase;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class PersonDBManager {
    // 数据库 db 文件名称
    private static final String DEFAULT_NAME = "person.db";

    // 默认版本号
    private static final int DEFAULT_VERSION = 1;

    private TMFDatabase mTMFDatabase;
    private SQLiteOpenHelper mDBHelper;
    private SQLiteDatabase mDB;

    public PersonDBManager(Context context, TMFDatabase database) {
//        mDBHelper = new PersonDBHelper(context);//没有加密
        mTMFDatabase = database;
        mDBHelper = database.createWCDBHelper(DEFAULT_NAME, "1234".getBytes(), null, DEFAULT_VERSION, null,
                new PersonDBCallback());//加密
        mDB = mDBHelper.getWritableDatabase();
    }

    /**
     * 插入数据
     *
     * @param person
     */
    public boolean addPersonData(Person person) {
        try {
            // 开启事务
            mDB.beginTransaction();

            // 执行插入语句
            final String sql = "INSERT INTO person VALUES(NULL,?,?)";
            Object[] objects = new Object[]{person.getName(), person.getAddress()};
            mDB.execSQL(sql, objects);

            // 设置事务完成成功
            mDB.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            // 关闭事务
            mDB.endTransaction();
        }
        return true;
    }

    /**
     * 批量插入数据
     *
     * @param list
     * @return
     */
    public boolean addPersonList(List<Person> list) {
        try {
            // 开启事务
            mDB.beginTransaction();

            // 执行插入语句
            for (Person person : list) {
                Object[] objects = new Object[]{person.getName(), person.getAddress()};
                final String sql = "INSERT INTO person VALUES(NULL,?,?)";
                mDB.execSQL(sql, objects);
            }

            // 设置事务完成成功
            mDB.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            // 关闭事务
            mDB.endTransaction();
        }
        return true;
    }

    /**
     * 删除特定姓名的人员数据
     *
     * @param name
     * @return
     */
    public boolean delPersonByName(String name) {
        try {
            // 开启事务
            mDB.beginTransaction();
            mDB.delete("person", "name=?", new String[]{name});
            // 设置事务完成成功
            mDB.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            // 关闭事务
            mDB.endTransaction();
        }
        return true;
    }

    /**
     * 通过姓名获取数据库数据
     *
     * @param name
     * @return
     */
    public List<Person> getPersonByName(String name) {
        ArrayList<Person> persons = new ArrayList<>();
        String sql = "select * from person where name=?";
        Log.d("WCDB", sql);
        Cursor cursor = mDB.rawQuery(sql, new String[]{name});
        while (cursor.moveToNext()) {
            Person person = new Person();
            person.setName(cursor.getString(cursor.getColumnIndex("name")));
            person.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            persons.add(person);
        }
        return persons;
    }

    /**
     * 获取全部数据库数据
     */
    public List<Person> getPersonListData() {
        List<Person> listData = new ArrayList<>();
        Cursor c = getAllPersonInfo();
        while (c.moveToNext()) {
            Person person = new Person();
            person.setName(c.getString(c.getColumnIndex("name")));
            person.setAddress(c.getString(c.getColumnIndex("address")));
            listData.add(person);
        }
        c.close();
        return listData;
    }

    public boolean updatePersonByName(String name, String address) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        int zhuiTao = mDB.update("person", values, "name=?", new String[]{name});
        if (zhuiTao > 0) {
            return true;
        } else {
            return false;
        }
    }

    private Cursor getAllPersonInfo() {
        return mDB.rawQuery("SELECT * FROM person", null);
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        mDB.close();
    }

    /**
     * 删除数据库文件
     */
    public void deleteDatabase() {
        mTMFDatabase.dropDatabase(DEFAULT_NAME);
    }

    /**
     * 删除数据库中所有数据
     */
    public void deleteAllData() {
        mDB.execSQL("DELETE FROM person;");
    }

}

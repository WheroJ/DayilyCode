package com.github.wheroj.goover2017_03_15.db;

import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by shopping on 2017/4/26 11:44.
 *
 * @description
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "book";
    private static final String TABLE_BOOK = "book";
    private static final String TABLE_USER = "user";

    private static final String CRE_BOOK = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOK + "(_id INTEGER PRIMARY KEY, " + "name TEXT)";
    private static final String CRE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(_id INTEGER PRIMARY KEY, " + "name TEXT," + "sex INT)";
    private static final int DB_VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRE_BOOK);
        db.execSQL(CRE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

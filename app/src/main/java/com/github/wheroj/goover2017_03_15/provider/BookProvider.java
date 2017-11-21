package com.github.wheroj.goover2017_03_15.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.wheroj.goover2017_03_15.db.DBOpenHelper;

/**
 * Created by shopping on 2017/4/25 14:38.
 *
 * @description
 */

public class BookProvider extends ContentProvider {

    private static final String AUTHORITY = "com.github.wheroj.goover2017_03_15.provider.BookProvider";

    private static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    private static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "user");

    private static final int USER_CODE = 1;
    private static final int BOOK_CODE = 2;

    static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, "book", USER_CODE);
        uriMatcher.addURI(AUTHORITY, "user", BOOK_CODE);
    }

    private DBOpenHelper dbOpenHelper;

    @Override
    public boolean onCreate() {
        init();
        return false;
    }

    private void init() {
        dbOpenHelper = new DBOpenHelper(getContext());
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_id", 1);
        values.put("name", "第一本书");
        database.insert("book", null, values);
        database.execSQL("insert into book(_id, name) values(2, '第二本书') ");
        database.execSQL("insert into book(_id, name) values(3, '第三本书') ");

        database.execSQL("insert into user(_id, name, sex) values(1, '雪萍', 1) ");
        database.execSQL("insert into user(_id, name, sex) values(2, 'shopping', 0) ");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);

        if (tableName == null) throw new IllegalArgumentException("不合法的 uri");
        Cursor cursor = dbOpenHelper.getReadableDatabase().query(tableName, null, null, null, null, null, null, null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);

        if (tableName == null) throw new IllegalArgumentException("不合法的 uri");
        long count = dbOpenHelper.getWritableDatabase().insert(tableName, null, values);
        if (count > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);

        if (tableName == null) throw new IllegalArgumentException("不合法的 uri");
        int count = dbOpenHelper.getWritableDatabase().delete(tableName, selection, selectionArgs);
        if (count > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);

        if (tableName == null) throw new IllegalArgumentException("不合法的 uri");
        int count = dbOpenHelper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
        if (count > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    private String getTableName(Uri uri){
        switch (uriMatcher.match(uri)){
            case USER_CODE:
                return "user";
            case BOOK_CODE:
                return "book";
            default:
                return null;
        }
    }
}

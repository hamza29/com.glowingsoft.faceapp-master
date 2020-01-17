package com.makemeold.agefaceeditor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBAdapter {

    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "Suitdb.sql";

    // mainexercise table
    private static final String TABLE_NAME = "Change_fg_bg";
    private static final String ID = "id";
    private static final String FG_BG = "fg_bg";
    private static final String IMG_PATH = "image_path";

    private static final String TABLE_FAVOURITE ="Favourite_Images";

    private static final String KEY_ID = "Id";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_IMAGE_PATH = "image_path";

    private final Context context;
    public static DatabaseHelper DBHelper;
    private static SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    // ---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }




    public void saveMessageData(byte[] image_url, String image_path) {

        try {

             db = DBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_IMAGE_URL, image_url);
            values.put(KEY_IMAGE_PATH, image_path);

            db.insert(TABLE_FAVOURITE, null, values);
            Log.e("added","added");
            db.close();

        } catch (Throwable t) {
            Log.e("Database", "Exception caught: " + t.getMessage(), t);
        }
    }

    public Table_Favourite_Images_model getFavouritr(String image_path) {

        db = DBHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITE, new String[]{KEY_ID, KEY_IMAGE_URL,KEY_IMAGE_PATH}, KEY_IMAGE_PATH + "=? ", new String[]{image_path}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Table_Favourite_Images_model bullets1_model = new Table_Favourite_Images_model(cursor.getInt(0), cursor.getBlob(1),cursor.getString(2));
        return bullets1_model;
    }

    public long GetRowCountofTable() {

        db = DBHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_FAVOURITE;
        SQLiteStatement statement = db.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }


    public String getSingleFavData(String name) {

        String data;
        db = DBHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITE, new String[]{KEY_ID, KEY_IMAGE_URL,KEY_IMAGE_PATH}, KEY_IMAGE_PATH + "=? ", new String[]{name}, null, null, null, null);
        if (cursor.moveToFirst()) {

            data = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
        }else{
            data=null;
        }

        return data;
    }

    public void deleteDrawDetails(String image_path) {
        db = DBHelper.getReadableDatabase();

        try {

            db.delete(TABLE_FAVOURITE, KEY_IMAGE_PATH + " = ?", new String[]{image_path});
            Log.e("deleted", "deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cursor getFavData() {

        db = DBHelper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public void deleteFavData() {

        db = DBHelper.getReadableDatabase();
        try {

            db.delete(TABLE_FAVOURITE, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
package com.jobnotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_REPORT = "tbl_report";
    public static final String TABLE_NOTES = "tbl_add_notes";
    public static final String TABLE_PHOTOS = "tbl_photos";
    public static final String TABLE_WORK_STATUS = "tbl_work_status";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REPORT_DATA = "report_data";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROPERTY_ID = "property_id";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_PATH = "image_path";
    public static final String COLUMN_STATUS = "work_status";
    public static final String COLUMN_REPORT_ID = "report_id";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_DATE_TIME = "date_time";

    private static final String DATABASE_NAME = "JOBNOTES.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_REPORT + "(" + COLUMN_ID
            + " integer primary key autoincrement," + COLUMN_REPORT_DATA + " text not null,"+ COLUMN_REPORT_ID + " text not null,"+ COLUMN_LOCATION_ID+ " text not null,"+ COLUMN_PROPERTY_ID +" text not null,"+ COLUMN_DATE_TIME +" text not null);";

    private static final String DATABASE_NOTES = "create table "
            + TABLE_NOTES + "(" + COLUMN_ID
            + " integer primary key autoincrement," + COLUMN_PROPERTY_ID + " text not null," + COLUMN_USER_ID + " text not null," + COLUMN_NOTES + " text not null);";

    private static final String DATABASE_PHOTOS = "create table "
            + TABLE_PHOTOS + "(" + COLUMN_ID
            + " integer primary key autoincrement," + COLUMN_PROPERTY_ID + " text not null," + COLUMN_PATH + " text not null);";

    private static final String DATABASE_WORK_STATUS = "create table "
            + TABLE_WORK_STATUS + "(" + COLUMN_ID
            + " integer primary key autoincrement," + COLUMN_PROPERTY_ID + " text not null," + COLUMN_USER_ID + " text not null, " + COLUMN_STATUS + " text not null);";

    public MySQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_NOTES);
        database.execSQL(DATABASE_PHOTOS);
        database.execSQL(DATABASE_WORK_STATUS);
    }

    // );
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS" + DATABASE_WORK_STATUS);
        onCreate(db);
    }


}
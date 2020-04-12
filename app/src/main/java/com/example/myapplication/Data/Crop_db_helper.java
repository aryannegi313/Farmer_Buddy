package com.example.myapplication.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Crop_db_helper extends SQLiteOpenHelper {

    public static final String DB_NAME= "Crop.db";
    public static final int VERSION=1;
    ;
    public static final String comma=" ,";
    public static final String Text=" TEXT";
    public static final String bracket=" );";
    public static String CREATE_TABLE="CREATE TABLE " + CropContract.CropEntry.TABLE_NAME +
            " (" + CropContract.CropEntry._ID + " INTEGER PRIMARY KEY," +
            CropContract.CropEntry.COLUMN_CROP_NAME + Text + comma +
            CropContract.CropEntry.COLUMN_CROP_INFO + Text + comma +
            CropContract.CropEntry.COLUMN_CROP_STATE + Text + bracket;

    public Crop_db_helper(Context context) {
        super(context,DB_NAME, null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

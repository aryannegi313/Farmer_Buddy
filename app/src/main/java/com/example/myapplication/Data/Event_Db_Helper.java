package com.example.myapplication.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Event_Db_Helper extends SQLiteOpenHelper {

        public static final String comma=" ,";
        public static final String Text=" TEXT";
        public static final String Int=" INTEGER";
        public static String CREATE_TABLE="CREATE TABLE " + EventContract.EventEntry.TABLE_NAME +
                " (" + EventContract.EventEntry._ID + " INTEGER PRIMARY KEY," +
                EventContract.EventEntry.COLUMN_EVENT_NAME + Text + comma +
                EventContract.EventEntry.COLUMN_EVENT_INFO + Text + comma +
                EventContract.EventEntry.COLUMN_EVENT_DATE + Text + comma +
                EventContract.EventEntry.COLUMN_EVENT_TIME + Text + " )";
        private static String DB_NAME="Event.db";
        private static int DB_Version=1;

        public Event_Db_Helper(Context context) {
            super(context, DB_NAME, null, DB_Version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        }
    }



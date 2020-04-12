package com.example.myapplication.Data;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class EventContract {
    public static final String CONTENT_AUTHORITY = "com.example.myapplication";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_EVENT = "Event";

    public static abstract class EventEntry implements BaseColumns {
        public static final String CONTENT_LIST_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;
        public static final String CONTENT_ITEM_TYPE= ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EVENT);
        public static final String TABLE_NAME = "Event";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_EVENT_NAME = "Name";
        public static final String COLUMN_EVENT_INFO = "Information";
        public static final String COLUMN_EVENT_DATE = "Date";
        public static final String COLUMN_EVENT_TIME="Time";
    }
}

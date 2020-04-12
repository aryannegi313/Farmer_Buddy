package com.example.myapplication.Data;
import android.provider.BaseColumns;

public class CropContract {
    public static abstract class CropEntry implements BaseColumns {
        public static final String TABLE_NAME = "Crops";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CROP_NAME = "Name";
        public static final String COLUMN_CROP_INFO = "Information";
        public static final String COLUMN_CROP_STATE = "State";
    }
}

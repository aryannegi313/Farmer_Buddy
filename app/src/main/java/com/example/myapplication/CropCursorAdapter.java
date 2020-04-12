package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.myapplication.Data.CropContract;

public class CropCursorAdapter extends CursorAdapter {


    public CropCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name=(TextView) view.findViewById(R.id.name);
        TextView breed=(TextView) view.findViewById(R.id.state);

        String body=cursor.getString(cursor.getColumnIndexOrThrow(CropContract.CropEntry.COLUMN_CROP_NAME));
        String des=cursor.getString(cursor.getColumnIndexOrThrow(CropContract.CropEntry.COLUMN_CROP_STATE));

        name.setText(body);
        breed.setText(des);
    }
}

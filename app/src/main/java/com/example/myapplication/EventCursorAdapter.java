package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.myapplication.Data.EventContract;
import com.example.myapplication.R;

public class EventCursorAdapter extends CursorAdapter {
    public EventCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name=(TextView) view.findViewById(R.id.event_name);
        TextView breed=(TextView) view.findViewById(R.id.event_info);

        String body=cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_EVENT_NAME));
        String des=cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_EVENT_INFO));

        name.setText(body);
        breed.setText(des);
    }
}

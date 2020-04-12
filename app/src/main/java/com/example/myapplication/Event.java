package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.Data.CropContract;
import com.example.myapplication.Data.Crop_db_helper;
import com.example.myapplication.Data.EventContract;
import com.example.myapplication.Data.Event_Db_Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class Event extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    FloatingActionButton Fab;
    private EventCursorAdapter ccursorAdap;
    private static final int EVENT_LOADER=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Fab=(FloatingActionButton) findViewById(R.id.fab);

        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Event.this,EditEvent_main.class);
                startActivity(intent);;
            }
        });

        ListView lvItems = (ListView) findViewById(R.id.list_view_event);

        //displayDatabaseInfo();
        ccursorAdap =new EventCursorAdapter(this, null,false);
        lvItems.setAdapter(ccursorAdap);
        View emptyView = findViewById(R.id.empty_view);
        lvItems.setEmptyView(emptyView);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(Event.this,EditEvent_main.class);
                Uri cur_uri= ContentUris.withAppendedId(EventContract.EventEntry.CONTENT_URI,id);
                intent.setData(cur_uri);
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(EVENT_LOADER,null,this );
    }
    public void insertdummyEvent()
    {

        ContentValues cv=new ContentValues();
        cv.put(EventContract.EventEntry.COLUMN_EVENT_NAME, "Harvest");
        cv.put(EventContract.EventEntry.COLUMN_EVENT_INFO, "Harvest of Wheat");
        cv.put(EventContract.EventEntry.COLUMN_EVENT_DATE, "24/4/2000");
        cv.put(EventContract.EventEntry.COLUMN_EVENT_TIME, "3:55");

        getContentResolver().insert(EventContract.EventEntry.CONTENT_URI,cv);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_insert_dummy_data:
                insertdummyEvent();
                break;
            case R.id.action_delete_all_entries:
                deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_event_catalog, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayDatabaseInfo() {

        String projection[] = {EventContract.EventEntry._ID, EventContract.EventEntry.COLUMN_EVENT_NAME, EventContract.EventEntry.COLUMN_EVENT_INFO};

        Cursor cursor =getContentResolver().query(EventContract.EventEntry.CONTENT_URI,projection,null,null,null,null);
        ListView lvItems = (ListView) findViewById(R.id.list_view_event);
        View emptyView = findViewById(R.id.empty_view);
        lvItems.setEmptyView(emptyView);

        EventCursorAdapter todoAdapter = new EventCursorAdapter(this, cursor, false);
        lvItems.setAdapter(todoAdapter);


    }

    public void deleteAll()
    {
        getContentResolver().delete(EventContract.EventEntry.CONTENT_URI,null,null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String projection[]={EventContract.EventEntry._ID,EventContract.EventEntry.COLUMN_EVENT_NAME,EventContract.EventEntry.COLUMN_EVENT_INFO};
        return new CursorLoader(this, EventContract.EventEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
       ccursorAdap.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ccursorAdap.swapCursor(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Event.this,MainActivity.class));
        finish();
    }
}

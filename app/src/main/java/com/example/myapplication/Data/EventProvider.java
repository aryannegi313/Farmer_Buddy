package com.example.myapplication.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Event;

public class EventProvider extends ContentProvider {
    public static final String LOG_TAG = EventProvider.class.getSimpleName();
    private static final int EVENTS = 100;

    private static final int EVENT_ID = 101;
    Event_Db_Helper dbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(EventContract.CONTENT_AUTHORITY,EventContract.PATH_EVENT,EVENTS);
        sUriMatcher.addURI(EventContract.CONTENT_AUTHORITY,EventContract.PATH_EVENT+"/#",EVENT_ID);
    }

    @Override
    public boolean onCreate() {

        dbHelper=new Event_Db_Helper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (match) {
            case EVENTS:
                cursor=db.query(EventContract.EventEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case EVENT_ID:
                selection = EventContract.EventEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(EventContract.EventEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match=sUriMatcher.match(uri);
        switch(match)
        {
            case EVENTS:
                return insertEvents(uri,contentValues);
            default:
                throw new IllegalArgumentException("value cannotbe added at this " + uri);
        }
}


    public Uri insertEvents(Uri uri,ContentValues cv)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String name=cv.getAsString(EventContract.EventEntry.COLUMN_EVENT_NAME);

        getContext().getContentResolver().notifyChange(uri,null);

        long a=db.insert(EventContract.EventEntry.TABLE_NAME,null,cv);
        if(a==-1)
        {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri,a);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS:
                return updateEvent(uri, contentValues, selection, selectionArgs);
            case EVENT_ID:
                selection = EventContract.EventEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateEvent(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateEvent(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int rowsUpdated = db.update(EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);}
        return rowsUpdated;

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsUpdated=0;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EVENTS:
                rowsUpdated=database.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
                if(rowsUpdated!=0){
                    getContext().getContentResolver().notifyChange(uri,null);}
                return rowsUpdated;

            case EVENT_ID:
                selection = EventContract.EventEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated=database.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
                if(rowsUpdated!=0){
                    getContext().getContentResolver().notifyChange(uri,null);}
                return rowsUpdated;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS:
                return EventContract.EventEntry.CONTENT_LIST_TYPE;
            case EVENT_ID:
                return EventContract.EventEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
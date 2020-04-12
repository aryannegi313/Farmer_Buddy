package com.example.myapplication;

import android.content.ContentUris;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Data.CropContract;
import com.example.myapplication.Data.Crop_db_helper;

public class Crop extends AppCompatActivity {
    CropCursorAdapter ccursorAdap;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.crop);
        ListView lvItems = (ListView) findViewById(R.id.list_view_crop);

        //displayDatabaseInfo();
        ccursorAdap =new CropCursorAdapter(this, null,false);
        lvItems.setAdapter(ccursorAdap);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(Crop.this,Information_Activity.class);
                intent.addFlags((int) id);
                startActivity(intent);
            }
        });
        displayDatabaseInfo();
    }

    public void onBackPressed(){
        super.onBackPressed();

        Crop_db_helper dbHelper=new Crop_db_helper(getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int a=db.delete(CropContract.CropEntry.TABLE_NAME,null,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_act,menu);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayDatabaseInfo() {

        String projection[] = {CropContract.CropEntry._ID, CropContract.CropEntry.COLUMN_CROP_NAME, CropContract.CropEntry.COLUMN_CROP_STATE};
        Crop_db_helper dbHelper=new Crop_db_helper(getApplicationContext());
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        Cursor cursor =db.query(CropContract.CropEntry.TABLE_NAME,projection,null,null,null,null,null);
        ListView lvItems = (ListView) findViewById(R.id.list_view_crop);

        CropCursorAdapter todoAdapter = new CropCursorAdapter(this, cursor, false);
        lvItems.setAdapter(todoAdapter);
    }
}
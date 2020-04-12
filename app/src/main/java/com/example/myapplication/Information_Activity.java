package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Data.CropContract;
import com.example.myapplication.Data.Crop_db_helper;

public class Information_Activity extends AppCompatActivity {
    TextView tv;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        tv=(TextView)findViewById(R.id.information);
        imageView=(ImageView)findViewById(R.id.crop) ;

        Intent intent=getIntent();
        int i=intent.getFlags();

        Crop_db_helper dbHelper=new Crop_db_helper(getApplicationContext());
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        String projection[]={CropContract.CropEntry.COLUMN_CROP_INFO};
        String selection = CropContract.CropEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(i)};

        Cursor c=db.query(CropContract.CropEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
        c.moveToFirst();
        String info=c.getString(c.getColumnIndex(CropContract.CropEntry.COLUMN_CROP_INFO));

        new ScrollView(getApplicationContext());
        tv.setText(info);

        if(i==1)
        {
            imageView.setImageResource(R.drawable.ic_plant);
        }
        if(i==2)
        {
            imageView.setImageResource(R.drawable.ic_corn);
        }
        /*if(i==4)
        {
            imageView.setImageResource(R.drawable.ic_rice);
        }*/
        if(i==3)
        {
            imageView.setImageResource(R.drawable.ic_sugar_cane);
        }
    }
}

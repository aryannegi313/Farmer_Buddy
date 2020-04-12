package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.myapplication.Data.EventContract;
import java.util.Calendar;
import com.example.myapplication.AlarmScheduler;

public class EditEvent_main extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG="EditEvent";
    EditText e_name,e_info;
    private DatePicker dpick;
    private TimePicker tpick;
    TextView timeTV,dateTV;
    ImageButton cal,clo;
    Calendar calendar;
    private static final int EXISTING_EVENT_LOADER = 0 ;
    Button setdate,settime;
    private Uri cur_uri;


    private static final String KEY_TITLE = "Name_key";
    private static final String KEY_TIME = "Time_key";
    private static final String KEY_DATE = "Date_key";
    public static final String KEY_INFO="Information_key";
    private int mYear, mMonth, mHour,minute, mDay;


    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);

        e_name=(EditText) findViewById(R.id.event_name);
        e_info=(EditText) findViewById(R.id.event_info);
        dpick=(DatePicker) findViewById(R.id.datePicker);
        tpick=(TimePicker) findViewById(R.id.timePicker);
        cal=(ImageButton) findViewById(R.id.date);
        clo=(ImageButton) findViewById(R.id.time) ;
        timeTV=(TextView)findViewById(R.id.timetv);
        dateTV=(TextView) findViewById(R.id.datetv);
        setdate=(Button) findViewById(R.id.setD);
        settime=(Button) findViewById(R.id.setT);
        dpick.setVisibility(View.INVISIBLE);
        tpick.setVisibility(View.INVISIBLE);
        settime.setVisibility(View.INVISIBLE);
        setdate.setVisibility((View.INVISIBLE));

        Intent intent=getIntent();
        cur_uri=intent.getData();
        if(cur_uri==null)
        {
            setTitle("ADD AN EVENT");
            invalidateOptionsMenu();
        }
        else
        {
            setTitle("EDIT EVENT");
            getSupportLoaderManager().initLoader(EXISTING_EVENT_LOADER, null, this);
        }

        cal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dpick.setVisibility(View.VISIBLE);
                    tpick.setVisibility(View.INVISIBLE);
                    setdate.setVisibility(View.VISIBLE) ;
                }
            });

            clo.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    tpick.setVisibility(View.VISIBLE);
                    dpick.setVisibility(View.INVISIBLE);
                    settime.setVisibility(View.VISIBLE);
                }
            });

            setdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateTV.setText(getCurrentDate());
                }
            });


        settime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                timeTV.setText(getCurrentTime());
            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void insertEvent()
    {
        if(TextUtils.isEmpty(e_name.getText())){
            e_name.setError("name cannot be empty.");
            return;
        }
        if(TextUtils.isEmpty(e_info.getText())){
            e_info.setError("info cannot be empty,.");
            return;
        }
        if(TextUtils.isEmpty(dateTV.getText())){
            dateTV.setError("set a date");
            return;
        }
        if(TextUtils.isEmpty(timeTV.getText()))
        {
            timeTV.setError("set time");
            return;
        }

        String dateSt=String.valueOf(dateTV.getText());
        String timeSt=String.valueOf(timeTV.getText());
        String dt[]=dateSt.split("/");
        String ti[]=timeSt.split(":");
        mMonth= Integer.parseInt(dt[0]);
        mDay=Integer.parseInt(dt[1]);
        mYear=Integer.parseInt(dt[2]);
        mHour=Integer.parseInt(ti[0]);
        minute=Integer.parseInt(ti[1]);
        calendar=Calendar.getInstance();
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        long selectedTimestamp =  calendar.getTimeInMillis();
        ContentValues cv=new ContentValues();
            cv.put(EventContract.EventEntry.COLUMN_EVENT_NAME, String.valueOf(e_name.getText()));
            cv.put(EventContract.EventEntry.COLUMN_EVENT_INFO, String.valueOf(e_info.getText()));
            cv.put(EventContract.EventEntry.COLUMN_EVENT_DATE, String.valueOf(dateTV.getText()));
            cv.put(EventContract.EventEntry.COLUMN_EVENT_TIME, String.valueOf(timeTV.getText()));

            if(cur_uri==null) {
                Uri uri = getContentResolver().insert(EventContract.EventEntry.CONTENT_URI, cv);
                if (uri == null) {
                    Context context = getApplicationContext();
                    CharSequence text = "value not inserted some error";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                } else {
                    Context context = getApplicationContext();
                    }
            }
            else{
                int row=getContentResolver().update(cur_uri,cv,null,null);
                if(row<=0)
                {
                    Context context = getApplicationContext();
                    CharSequence text = "value not updated";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else
                {
                    CharSequence text = "value updated";
                }
            }

        AlarmScheduler as=new AlarmScheduler();
            as.set_alarm(getApplicationContext(),selectedTimestamp,cur_uri);

        Toast.makeText(this, "Alarm time is " + mHour+":" +minute+" on "+mDay+"/"+mMonth+"/"+mYear,
                Toast.LENGTH_LONG).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getCurrentTime() {
        StringBuilder builder=new StringBuilder();;
        builder.append((tpick.getHour() )+":");
        builder.append(tpick.getMinute());
        return builder.toString();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertEvent();
                startActivity(new Intent(EditEvent_main.this,Event.class));
                break;
            case R.id.action_delete:
                deleteEvent();
                startActivity(new Intent(EditEvent_main.this,Event.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentDate(){
        StringBuilder builder=new StringBuilder();;
        builder.append((dpick.getMonth() + 1)+"/");
        builder.append(dpick.getDayOfMonth()+"/");
        builder.append(dpick.getYear());
        return builder.toString();
    }

    public void deleteEvent()
    {
        int rowsDeleted=getContentResolver().delete(cur_uri,null,null);
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, "could not delete",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this,"deleted",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_editor,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (cur_uri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id,Bundle args) {
        return new CursorLoader(this,cur_uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_NAME));
            String info = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_INFO));
            String date = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_DATE));
            String time = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_TIME));

            e_name.setText(name);
            e_info.setText(info);
            dateTV.setText(date);
            timeTV.setText(time);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        dateTV.setText("");
        timeTV.setText("");
        e_name.setText("");
        e_info.setText("");
    }

}


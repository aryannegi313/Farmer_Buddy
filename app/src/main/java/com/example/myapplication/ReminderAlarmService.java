package com.example.myapplication;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.example.myapplication.Data.EventContract;

public class ReminderAlarmService extends IntentService {

    public static final String TAG=ReminderAlarmService.class.getSimpleName();
    public static final int NOTIFICATION_ID=42;
    private static final String CHANNEL_ID ="Events" ;
    Cursor cursor;
    NotificationManager manager;

    public static PendingIntent getReminderPendingIntent(Context context, Uri flag)
    {
        Intent action=new Intent(context,ReminderAlarmService.class);
        action.setData(flag);
        return PendingIntent.getService(context,0,action,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent) {

      manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            CharSequence Ename = "Event";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, Ename, importance);
                manager.createNotificationChannel(channel);
        Uri i=intent.getData();
        Intent action= new Intent(this,EditEvent_main.class);
        action.setData(i);

        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            cursor= getContentResolver().query(i, null, null, null, null, null);

        String description="";
        String name="";

        try {
            if (cursor != null && cursor.moveToFirst()) {
                name=cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_NAME));
                description = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_INFO));
            }
        }finally {
            if(cursor!=null)
            {
                cursor.close();
            }
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(name)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_timetable);

            manager.notify(NOTIFICATION_ID, notification.build());

    }
}


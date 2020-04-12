package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;

public class AlarmScheduler {

    public boolean set_alarm(Context context, long alarmtime,Uri flag)
    {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation=ReminderAlarmService.getReminderPendingIntent(context,flag);
        manager.set(AlarmManager.RTC_WAKEUP,alarmtime,operation);


        return false;
    }

    public void cancelAlarm(Context context, Uri reminderTask){
        AlarmManager manager=AlarmManagerProvider.getAlarmManager(context);
        PendingIntent operation=ReminderAlarmService.getReminderPendingIntent(context,reminderTask);

        manager.cancel(operation);
    }

}

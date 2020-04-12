package com.example.myapplication;

import android.app.AlarmManager;
import android.content.Context;

public class AlarmManagerProvider {
    public static final String TAG=AlarmManagerProvider.class.getSimpleName();
    public static AlarmManager manager;
    public static synchronized void injectAlarmManager(AlarmManager alarmManager)
    {
        if(manager!=null)
        {
            throw new IllegalStateException("Alarm Already Set");
        }
        manager=alarmManager;
    }
    static synchronized AlarmManager getAlarmManager(Context context)
    {
        if(manager==null)
        {
            manager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return manager;
    }

}

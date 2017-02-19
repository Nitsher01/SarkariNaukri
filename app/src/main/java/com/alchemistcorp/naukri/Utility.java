package com.alchemistcorp.naukri;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Ali on 5/23/2016.
 */
public class Utility {

    public static void setFont(TextView tv,AssetManager assets){
        Typeface face = Typeface.createFromAsset(assets,
                "Raleway-Regular.ttf");
        tv.setTypeface(face);
    }

    public static void scheduleAlarm(Context context) {

        boolean alarmUp = (PendingIntent.getBroadcast(context, 1234, new Intent(context, HoroscopesBroadcastReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmUp)
            return;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 0);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1234, new Intent(context, HoroscopesBroadcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY , pi);
    }
}

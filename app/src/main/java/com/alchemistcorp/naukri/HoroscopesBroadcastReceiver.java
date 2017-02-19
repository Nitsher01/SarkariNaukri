package com.alchemistcorp.naukri;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.alchemistcorp.naukri.Activity.MainActivity;

public class HoroscopesBroadcastReceiver extends BroadcastReceiver {
    public HoroscopesBroadcastReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Utility.scheduleAlarm(context);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
         boolean showNoti = preferences.getBoolean("enable_notifications",true);
        if(!showNoti)
            return;
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.aquarius)
                        .setContentTitle("Sarkari Naurki")
                        .setContentText("Check new government jobs!! Tap to view...")
                        .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#512DA8"));
        if(preferences.getBoolean("enable_vibrations",false)) {
            mBuilder.setVibrate(new long[]{100, 100, 100, 100, 100});
        }

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);

    }
}

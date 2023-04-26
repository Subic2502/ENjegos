package com.gorskivijenac.enjegos;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String notificationMessage = intent.getStringExtra("notification_message");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My App Notification")
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission((Activity) context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY},
                    1);

        }else{
            notificationManager.notify(0, builder.build());
        }


    }

}

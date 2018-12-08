package com.creatu.sapnakoghar.push_notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.creatu.sapnakoghar.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Created by roshan on 6/3/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String title;
    String desc;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


          Intent intent=
                  new Intent(this, Notification.class);

            title =remoteMessage.getNotification().getTitle();
            desc = remoteMessage.getNotification().getBody();


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationbuilder=new NotificationCompat.Builder(this);
        notificationbuilder.setContentTitle(title);
        notificationbuilder.setContentText(Html.fromHtml(desc));
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationbuilder.setSound(alarmSound);


        notificationbuilder.setAutoCancel(true);
        notificationbuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        notificationbuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("Default", "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        if (notificationManager != null) {
            notificationManager.notify(0,notificationbuilder.build());
        }
    }
}

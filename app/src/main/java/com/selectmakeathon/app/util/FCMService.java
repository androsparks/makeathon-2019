package com.selectmakeathon.app.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {

            String body = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();

            FcmNotification.notify(this, body, title);
        }

    }

    @Override
    public void onNewToken(String s) {

        Log.i("FCM TOKEN", s);

    }
}

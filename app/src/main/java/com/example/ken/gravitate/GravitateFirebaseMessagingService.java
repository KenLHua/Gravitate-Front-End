package com.example.ken.gravitate;
/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ken.gravitate.Event.ScheduledEvents;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class GravitateFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "GravitateNotifcations";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData().toString());
        }

    }

    private void handleNow(){
        Log.d(TAG, "Short task done");
    }

    // Create and show a simple notification from the server
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, ScheduledEvents.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId1 = getString(R.string.orbit_notif_channel);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId1)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        String channelId2 = getString(R.string.flight_notif_channel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel orbitChannel = new NotificationChannel(channelId2,
                    "Orbit Confirmed",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(orbitChannel);

            NotificationChannel flightChannel = new NotificationChannel("2",
                    "Flight Updates",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(flightChannel);
            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(channelId2, "Flight"));

        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

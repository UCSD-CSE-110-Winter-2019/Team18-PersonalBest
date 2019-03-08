package com.android.personalbest.messaging;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import static com.android.personalbest.fitness.GoogleFitAdaptor.TAG;

public class MessagingAdaptor  extends FirebaseMessagingService implements IMessaging  {
    CollectionReference chat;
    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY;
    String MESSAGES_KEY = "messages";
    Activity activity;


    public MessagingAdaptor(Activity activity, String chatID) {
        this.activity = activity;
        DOCUMENT_KEY = chatID;
    }

    public void setup() {
        chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);
        Toast.makeText(activity, chat.getPath().toString(), Toast.LENGTH_LONG).show();
        Log.wtf("@@@@@@@@@@@@@", chat.getPath());
    }



    public void subscribeToNotificationsTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(DOCUMENT_KEY)
            .addOnCompleteListener(task -> {
                        String msg = "Subscribed to notifications";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe to notifications failed";
                        }
                        Log.wtf(TAG, msg);
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
            );
    }

    public void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = DOCUMENT_KEY;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle("notification")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}

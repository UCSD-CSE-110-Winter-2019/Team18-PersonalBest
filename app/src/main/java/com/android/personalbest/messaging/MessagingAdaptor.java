package com.android.personalbest.messaging;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

import static com.android.personalbest.fitness.GoogleFitAdaptor.TAG;

public class MessagingAdaptor  extends FirebaseMessagingService implements IMessaging  {
    CollectionReference chat;
    DocumentReference goal_for_user;
    String COLLECTION_KEY;
    String DOCUMENT_KEY;
    String MESSAGES_KEY;
    Activity activity;

//    private FirebaseFunctions mFunctions;


    public MessagingAdaptor(Activity activity,String collection, String document, String messages_key) {
        this.activity = activity;
        DOCUMENT_KEY = document;
        COLLECTION_KEY = collection;
        MESSAGES_KEY = messages_key;
    }

    public MessagingAdaptor(Activity activity,String collection, String document) {
        this.activity = activity;
        DOCUMENT_KEY = document;
        COLLECTION_KEY = collection;
        MESSAGES_KEY = "";
    }

    public void setup() {
        if (MESSAGES_KEY.equals("")) {
            goal_for_user = FirebaseFirestore.getInstance()
                    .collection(COLLECTION_KEY)
                    .document(DOCUMENT_KEY);
        }
        else {
            chat = FirebaseFirestore.getInstance()
                    .collection(COLLECTION_KEY)
                    .document(DOCUMENT_KEY)
                    .collection(MESSAGES_KEY);
            Toast.makeText(activity, chat.getPath(), Toast.LENGTH_LONG).show();
            Log.wtf("@@@@@@@@@@@@@", chat.getPath());
        }
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



//    public Task<String> addMessage(String text) {
//        // Create the arguments to the callable function.
//        Map<String, Object> data = new HashMap<>();
//        data.put("text", text);
//        data.put("push", true);
//
//
//        mFunctions = FirebaseFunctions.getInstance();
//        Log.wtf("//////////////",mFunctions.toString());
//
//        return mFunctions
//                .getHttpsCallable("addMessage")
//                .call(data)
//                .continueWith(task -> {
//                    // This continuation runs on either success or failure, but if the task
//                    // has failed then getResult() will throw an Exception which will be
//                    // propagated down.
//                    Log.wtf("--------------",task.getResult().toString());
//                    String result = (String) task.getResult().getData();
//
//                    return result;
//                });
//    }

    public void sendNotification(String messageBody, Context context) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    // Log and toast
                    Log.wtf("!!!!!!!!!!!",token);
                    Toast.makeText(context, token, Toast.LENGTH_SHORT).show();

                });



//        String channelId = "cwguan@ucsd.edu";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setContentTitle("notification")
//                        .setContentText(messageBody)
//                        .setSound(defaultSoundUri);
//
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//
//        Log.wtf("!!!!!!!!!!!!!!", notificationManager.toString());
//
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "goal",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//            Log.wtf("@@@@@@@@@@@@@@@", channel.toString());
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }
}

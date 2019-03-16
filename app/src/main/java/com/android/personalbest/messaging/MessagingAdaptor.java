package com.android.personalbest.messaging;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import static com.android.personalbest.fitness.GoogleFitAdaptor.TAG;

public class MessagingAdaptor  extends FirebaseMessagingService implements IMessaging  {
    CollectionReference chat;
    DocumentReference goal_for_user;
    String COLLECTION_KEY;
    String DOCUMENT_KEY;
    String MESSAGES_KEY;
    Activity activity;

    // constructor with a message key
    public MessagingAdaptor(Activity activity,String collection, String document, String messages_key) {
        this.activity = activity;
        DOCUMENT_KEY = document;
        COLLECTION_KEY = collection;
        MESSAGES_KEY = messages_key;
    }

    // constructor without a message key
    public MessagingAdaptor(Activity activity,String collection, String document) {
        this.activity = activity;
        DOCUMENT_KEY = document;
        COLLECTION_KEY = collection;
        MESSAGES_KEY = "";
    }

    // setup an entry path for firestore
    public void setup() {
        if (MESSAGES_KEY.equals("")) {
            goal_for_user = FirebaseFirestore.getInstance()
                    .collection(COLLECTION_KEY)
                    .document(DOCUMENT_KEY);
            Log.d("path", goal_for_user.getPath());
            Toast.makeText(activity, goal_for_user.getPath(), Toast.LENGTH_LONG).show();
        }
        else {
            chat = FirebaseFirestore.getInstance()
                    .collection(COLLECTION_KEY)
                    .document(DOCUMENT_KEY)
                    .collection(MESSAGES_KEY);
            Log.d("path", chat.getPath());
            Toast.makeText(activity, chat.getPath(), Toast.LENGTH_LONG).show();

        }
    }


    // subscribe to a specific topic to get notification
    public void subscribeToNotificationsTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(DOCUMENT_KEY)
            .addOnCompleteListener(task -> {
                        String msg = "Subscribed to notifications " + DOCUMENT_KEY;
                        if (!task.isSuccessful()) {
                            msg = "Subscribe to notifications failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
            );
    }
}

package com.android.personalbest.firestore;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.personalbest.MainActivity;
import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


public class FirestoreAdaptor implements IFirestore {
    private static final String TAG = "[FirestoreAdaptor]";
    private Activity activity;
    String userEmail;
    FirebaseFirestore fs;
    private boolean taskCompleted = false;

    String USERS_COLLECTION_KEY = "users";

    String CHATS_COLLECTION_KEY = "chats";
    String MESSAGES_KEY = "messages";
    String TIMESTAMP_KEY = "timestamp";
    String FROM_EMAIL_KEY = "fromEmail";
    String FROM_NAME_KEY = "fromName";
    String TEXT_KEY = "text";

    public String user_name;

    public FirestoreAdaptor(Activity activity, String userEmail) {
        this.activity = activity;
        this.userEmail = "cwguan@ucsd.edu";
        this.fs = FirebaseFirestore.getInstance();
    }


    // IDs for chats between friends are generated via concatenating emails in alphabetical order
    public String getChatID(String otherUserEmail) {
        String email1 = this.userEmail.replace("@","");
        String email2 = otherUserEmail.replace("@", "");
        int comparison = email1.compareToIgnoreCase(email2);

        // If userEmail comes before otherUserEmail, compareToIgnoreCase will return -1,
        // concatenate userEmail in front of otherUserEmail
        return comparison < 0 ? email1.concat(email2) : email2.concat(email1);
    }


    public void setName(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);
        userRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    @Override
    public void setGoal(int goal) {
        Map<String, Object> data = new HashMap<>();
        data.put("goal", goal);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);
        userRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }


    @Override
    public void setHeightFt(int heightFt) {
        Map<String, Object> data = new HashMap<>();
        data.put("heightFt", heightFt);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);
        userRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }


    @Override
    public void setHeightIn(int heightIn) {
        Map<String, Object> data = new HashMap<>();
        data.put("heightIn", heightIn);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);
        userRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }


    // Adds listener for any new messages and appends to the given TextView
    public void initMessageUpdateListener(TextView chatView, String otherUserEmail) {
        CollectionReference chat = fs.collection(CHATS_COLLECTION_KEY).document(getChatID(otherUserEmail)).collection(MESSAGES_KEY);

        chat.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();

                        for (DocumentChange change : documentChanges) {
                            QueryDocumentSnapshot document = change.getDocument();
                            sb.append(document.get(FROM_EMAIL_KEY));
                            sb.append(":\n");
                            sb.append(document.get(TEXT_KEY));
                            sb.append("\n");
                            sb.append("---\n");
                        }


                        chatView.append(sb.toString());
                    }
                });

    }


    public void addSentMessageToDatabase(EditText editText, String otherUserEmail) {
        CollectionReference chat = fs.collection(CHATS_COLLECTION_KEY).document(getChatID(otherUserEmail)).collection(MESSAGES_KEY);

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_EMAIL_KEY, userEmail);
        newMessage.put(TEXT_KEY, editText.getText().toString());

        chat.add(newMessage).addOnSuccessListener(result -> {
            editText.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }

    @Override
    public void initMainActivity(MainActivity mainActivity, HomeUI homeUI) {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    User user = document.toObject(User.class);
                    MainActivity.setCurrentUser(user);
                    mainActivity.setUpMessaging();
                    mainActivity.loadFragment(homeUI);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }


    // Method used to notify all observers that the User object may have been updated
    public void updatedUser() {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        User user = document.toObject(User.class);
                        Log.d(TAG, "" + user);
                        MainActivity.setCurrentUser(user);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

}

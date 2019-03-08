package com.android.personalbest.firestore;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.personalbest.R;
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
        this.userEmail = userEmail;


        this.fs = FirebaseFirestore.getInstance();
    }


    // IDs for chats between friends are generated via concatenating emails in alphabetical order
    private String getChatID(String otherUserEmail) {
        int comparison = this.userEmail.compareToIgnoreCase(otherUserEmail);

        // If userEmail comes before otherUserEmail, compareToIgnoreCase will return -1,
        // concatenate userEmail in front of otherUserEmail
        return comparison < 0 ? userEmail.concat(otherUserEmail) : otherUserEmail.concat(userEmail);
    }


    // Given a TextView that needs to display the user's name, goes to Firestore to retrieve and display
    // Passing in TextView is the current workaround for asynchronous nature
//    public void displayName(final TextView textView) {
//        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(this.userEmail);
//
//        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        textView.setText((String) document.get("name"));
//                        return;
//                    } else {
//                        Log.e(TAG, "No such document");
//                    }
//                } else {
//                    Log.w(TAG, "get failed with ", task.getException());
//                }
//
//                // Default case
//                textView.setText("User");
//            }
//        });
//    }

    public String displayName() {
        CountDownLatch done = new CountDownLatch(1);
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(this.userEmail);
//        String user_name;
        try {
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            user_name = document.get("name").toString();
                            done.countDown();

                        } else {
                            Log.e(TAG, "No such document");
                        }
                    } else {
                        Log.w(TAG, "get failed with ", task.getException());
                    }
                }
            });

            done.await();
        } catch (InterruptedException e) {}
        return user_name;
    }


    public void setName(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);
        userRef.set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
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

}

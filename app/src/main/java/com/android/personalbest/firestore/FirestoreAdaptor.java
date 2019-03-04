package com.android.personalbest.firestore;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class FirestoreAdaptor implements IFirestore {
    private static final String TAG = "[FirestoreAdaptor]";
    private Activity activity;
    String userId;
    FirebaseFirestore fs;
    private boolean taskCompleted = false;

    String USERS_COLLECTION_KEY = "users";

    String CHATS_COLLECTION_KEY = "chats";
    String MESSAGES_KEY = "messages";


    public FirestoreAdaptor(Activity activity, String userId) {
        this.activity = activity;
        this.userId = userId;


        this.fs = FirebaseFirestore.getInstance();
    }


    // TODO NEED TO THINK ABOUT HOW TO DEAL WITH ASYNCHRONOUS
    public String getName() {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return "";
    }

    public void setName(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userId);
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
}

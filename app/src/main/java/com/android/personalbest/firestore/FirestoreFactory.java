package com.android.personalbest.firestore;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class FirestoreFactory {
    private static final String TAG = "[FirestoreFactory]";

    private static Map<String, FirestoreFactory.BluePrint> blueprints = new HashMap<>();

    public static void put(String key, FirestoreFactory.BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static IFirestore create(String key, Activity activity, String userEmail) {
        Log.i(TAG, String.format("creating FirestoreService with key %s", key));
        return blueprints.get(key).create(activity, userEmail);
    }

    public interface BluePrint {
        IFirestore create(Activity activity, String userEmail);
    }
}

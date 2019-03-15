package com.android.personalbest;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.personalbest.firestore.FirestoreAdaptor;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.fitness.FitServiceFactory;
import com.android.personalbest.fitness.IFitService;
import com.android.personalbest.messaging.IMessaging;
import com.android.personalbest.messaging.MessagingFactory;


public class FitnessService extends Service{
    IFitService gFit;
    IMessaging messaging;
    IFirestore firestore;
    Activity activity;

    final String TAG = "SERIVCE";
    public static final String FIRESTORE_SERVICE_KEY = "FIRESTORE_SERVICE_KEY";
    public static final String FIRESTORE_ADAPTOR_KEY = "FIRESTORE_ADAPTOR";

    User user;

    public FitnessService() {
        activity = MainActivity.getActivity();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        user = MainActivity.getCurrentUser();

        gFit = FitServiceFactory.create(TAG,activity);

        String firestoreKey = intent.getStringExtra(FIRESTORE_SERVICE_KEY);
        if (firestoreKey == null) {
            FirestoreFactory.put(FIRESTORE_ADAPTOR_KEY, (activity, userEmail) -> new FirestoreAdaptor(activity, userEmail));
            // Default Firestore implementation using our adaptor
            firestore = new FirestoreAdaptor(activity, user.getEmail());
        } else {
            firestore = FirestoreFactory.create(firestoreKey, activity, user.getEmail());
        }

        messaging = MessagingFactory.create(
                TAG, activity, "goal", user.getEmail().replace("@", ""), "");
        messaging.setup();
        messaging.subscribeToNotificationsTopic();

        Thread thread = new Thread(new MyThread(startId));
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy(){
        Toast.makeText(FitnessService.this, "Service Stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    final class MyThread implements Runnable {
        int startId;
        Boolean hasShown = false;

        public MyThread (int startId) {
            this.startId = startId;
        }


        @Override
        public void run() {
            synchronized (this) {
                while(!hasShown) {
                    try {
                        user.setGoal(80);
                        Thread.sleep(50000);
                        long updated_steps = gFit.getTotalDailySteps();

                        Log.wtf("Goal", Integer.toString(user.getGoal()));
                        Log.wtf("Current Step", Long.toString(updated_steps));

                        user.setGoal(0);
                        if (updated_steps == user.getGoal()) {
                            Log.wtf("yes", "yes");

                            firestore.addGoalToDatabase();
                            hasShown = true;
                        }

                    } catch (InterruptedException e) {}
                }
            }
        }
    }
}


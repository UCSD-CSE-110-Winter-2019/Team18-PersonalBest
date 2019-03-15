package com.android.personalbest;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.personalbest.fitness.FitServiceFactory;
import com.android.personalbest.fitness.IFitService;
import com.android.personalbest.messaging.IMessaging;
import com.android.personalbest.messaging.MessagingFactory;


public class FitnessService extends Service{
    IFitService gFit;
    IMessaging messaging;
    Activity activity;
    final String TAG = "SERIVCE";
    User user;

    public FitnessService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        gFit = FitServiceFactory.create(TAG, activity);
        user = MainActivity.getCurrentUser();
        messaging = MessagingFactory.create(TAG, activity, "goals", user.getEmail(), "");

        Thread thread = new Thread(new MyThread(startId));
        thread.start();

        Log.wtf("[[[[[[[[[[[[[[[[", "started");
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy(){
        Toast.makeText(FitnessService.this, "Service Stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    final class MyThread implements Runnable {
        int startId;

        public MyThread (int startId) {
            this.startId = startId;
        }

        @Override
        public void run() {
            synchronized (this) {
                long updated_steps = gFit.getTotalDailySteps();

                if (updated_steps == user.getGoal()) {

                }
            }
        }
    }
}


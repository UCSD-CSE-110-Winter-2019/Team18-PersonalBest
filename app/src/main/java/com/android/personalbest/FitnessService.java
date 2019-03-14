package com.android.personalbest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class FitnessService extends Service{

    public FitnessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
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
                try {
                    wait (15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


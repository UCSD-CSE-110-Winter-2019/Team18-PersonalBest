package com.android.personalbest;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


public class DisplayEncouragement {
    private Encouragement encouragement;
    private Boolean isCancelled;
    private AsyncTaskRunner runner;

    public DisplayEncouragement(Activity activity) {
        encouragement = new Encouragement(activity);
        Log.d("msg","reach");
        runner=new AsyncTaskRunner();
        runner.execute("0");
    }
    public void show(){
        runner.cancel(true);
        encouragement.showChangeGoal();
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
    //boolean isCancelled = false;

    @Override
    protected String doInBackground(String... params) {
        for (int i = 0; i < 10; i++) {

            try {
                publishProgress(Integer.toString(i));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(i==5){
                return("5");
            }
            if(isCancelled){
                return(Integer.toString(i));
            }
            if(isCancelled()){break;}
        }
        return ("10");
    }

    @Override
    protected void onPostExecute(String result) {
        show();

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... count) {
        Log.d("msg",String.valueOf(count[0]));
    }
}
}



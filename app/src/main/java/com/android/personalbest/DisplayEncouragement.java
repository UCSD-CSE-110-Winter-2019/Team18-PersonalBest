package com.android.personalbest;

import android.app.Activity;
import android.os.AsyncTask;


public class DisplayEncouragement extends AsyncTask<String, String, String> {
    private Encouragement encouragement;
    //private GoogleFit googlefit;

    public DisplayEncouragement(Activity activity) {
        encouragement = new Encouragement(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        while (true) {
            try {
                //int steps = googlefit.getDailySteps();
                // int goal = get from shared preference

                //temp value
                int steps = 5593;
                int goal = 5000;

                int difference = steps - encouragement.getPreviousDayStep();

                if (steps > goal) {
                    encouragement.showChangeGoal();
                }


                if (encouragement.getTime() == "20:00" && difference >= 1000) {
                    encouragement.displayImprovement();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... count) {

    }

}

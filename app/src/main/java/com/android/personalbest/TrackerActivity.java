package com.android.personalbest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.personalbest.fitness.FitnessService;
import com.android.personalbest.fitness.FitnessServiceFactory;
import com.android.personalbest.fitness.GoogleFit;

import java.text.DecimalFormat;

public class TrackerActivity extends AppCompatActivity {
    Dialog myDialog;
    TrackTime timer;
    public boolean stopTimer = false;
    public TextView total_time;
    public TextView real_time;
    public static TextView total_steps;

    public static TextView display_velocity;
    public TextView display_avg_velocity;
    public TextView summary_steps;

    public String display_total_steps;
    public long curr_step;
    public long start_step;
    private double sum_velocity = 0;
    private double curr_velocity = 0;
    private int curr_time = 0;
    public long difference = 0;
    private static DecimalFormat df = new DecimalFormat("#.00");


    static int height_inch;

    //GoogleFit gFit;
    private FitnessService fitnessService;



    // create the tracker page for planned activities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        start_step = HomeFragment.curr_steps;

        //gFit = new GoogleFit(this);



        setContentView(R.layout.activity_tracker);
        myDialog = new Dialog(this);
        real_time = findViewById(R.id.time_elapsed);
        display_velocity = findViewById(R.id.velocity);
        total_steps = findViewById(R.id.steps);

        String get_value = getIntent().getStringExtra("home to tracker");
        fitnessService = FitnessServiceFactory.create(get_value, this);

        // start the timer for intentional activities
        timer = new TrackTime();
        timer.execute("0");

        height_inch = SharedPrefData.getHeightFt(this)*12 + SharedPrefData.getHeightIn(this);

        ((TextView)findViewById(R.id.steps)).setText(Long.toString(curr_step));

        // show the summary page when clicking on the "end walk/run" button
        Button exit = findViewById(R.id.exit_btn);
        final Context context = this.getApplicationContext();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                display_total_steps = ((TextView)findViewById(R.id.steps)).getText().toString();
                SharedPrefData.saveIntentionalSteps(context, (int) difference);
                ShowPopup(view);
            }
        });
    }

    @Override
    public void onPause(){
        timer.cancel(true);
        super.onPause();
    }

    public void ShowPopup(View v) {
        Button btnClose;
        myDialog.setContentView(R.layout.activity_summary);

        total_time = myDialog.findViewById(R.id.total_time);
        display_avg_velocity = myDialog.findViewById(R.id.avg_velocity);
        summary_steps = myDialog.findViewById(R.id.total_steps);
        stopTimer = true;


        // go back to home page when clicking the close button
        btnClose = myDialog.findViewById(R.id.back_to_home);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                launchActivity();
            }
        });

        // show the popup
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void launchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("intentional_steps", Long.toString(curr_step));
        startActivity(intent);
    }

    public static void setStepCount(long difference) {
        total_steps.setText(Long.toString(difference));
    }

    public static void setInstantVelocity(double velocity) {
        display_velocity.setText(df.format(velocity));
    }


    // class to track time elapsed
    private class TrackTime extends AsyncTask<String, String, String> {
        int min = 0;
        int sec = 0;
        String time;

        @Override
        protected String doInBackground(String... params) {
            while (true) {
                try {
                    publishProgress(Integer.toString(curr_time));


                    curr_step = fitnessService.getTotalDailySteps();
                    if (curr_step > 0)
                        difference = curr_step - start_step;

                    double step_per_mile = 5280/(height_inch * 0.413);

                    if (curr_time == 0)
                        curr_velocity = 0;
                    else
                        curr_velocity = (difference / step_per_mile) / curr_time;


                    Thread.sleep(1000);
                    curr_time ++;

                    // get the timer
                    min = curr_time / 60;
                    sec = curr_time % 60;
                    time = String.format("%d:%02d", min, sec);

                    // get the sum velocity
                    sum_velocity += curr_velocity;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(stopTimer){
                    return(time);
                }
                if(isCancelled()){
                    break;
                }
            }
            return (time);
        }

        @Override
        protected void onPostExecute(String result) {
            total_time.setText(time);

            double avg_velocity = sum_velocity / curr_time;
            display_avg_velocity.setText(df.format(avg_velocity));

            summary_steps.setText(Long.toString(difference));
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... count) {
            real_time.setText(time);

            setStepCount(difference);
            setInstantVelocity(curr_velocity);
        }
    }
}

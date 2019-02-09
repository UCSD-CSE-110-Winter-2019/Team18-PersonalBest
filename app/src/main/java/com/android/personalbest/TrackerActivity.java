package com.android.personalbest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TrackerActivity extends AppCompatActivity {
    Dialog myDialog;
    TrackTime timer;
    public boolean stopTimer = false;
    public TextView total_time;
    public TextView real_time;
    public String steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tracker);
        myDialog = new Dialog(this);
        real_time = findViewById(R.id.time_elapsed);

        timer = new TrackTime();
        timer.execute("0");

        Button exit = findViewById(R.id.exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                steps = ((TextView)findViewById(R.id.steps)).getText().toString();
                ShowPopup(view);
            }
        });
    }

    public void ShowPopup(View v) {
        Button btnClose;
        myDialog.setContentView(R.layout.activity_summary);

        total_time = myDialog.findViewById(R.id.total_time);
        stopTimer = true;

        TextView total_steps = myDialog.findViewById(R.id.total_steps);
        total_steps.setText(steps);

        btnClose = myDialog.findViewById(R.id.back_to_home);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                launchActivity();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void launchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // class to track time elapsed
    private class TrackTime extends AsyncTask<String, String, String> {
        int min = 0;
        int sec = 0;
        String time;

        @Override
        protected String doInBackground(String... params) {
            int i = 0;

            while (true) {
                try {
                    publishProgress(Integer.toString(i));
                    Thread.sleep(1000);
                    i ++;
                    min = i / 60;
                    sec = i % 60;
                    time = String.format("%d:%02d", min, sec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(stopTimer){
                    return(time);
                }
                if(isCancelled()){break;}
            }
            return (time);
        }

        @Override
        protected void onPostExecute(String result) {
            total_time.setText(time);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... count) {
            real_time.setText(time);
        }
    }


}

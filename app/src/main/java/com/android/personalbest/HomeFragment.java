package com.android.personalbest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class HomeFragment extends Fragment {
    private static int goal;

    static long curr_steps;
    GoogleFit gFit;
    static AsyncTaskRunner runner;

    Activity activity;
    static boolean first=true;
    static LayoutInflater temp;
    static Context ct;
    static TextView display_goal;
    static TextView display_steps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        temp=inflater;
        gFit = new GoogleFit(this.getActivity());
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleFit gFit = new GoogleFit(this.getActivity());
        gFit.setup();
        display_goal = ((TextView)getView().findViewById(R.id.goal));
        display_steps = ((TextView)getView().findViewById(R.id.display));


        if(first){
            runner = new AsyncTaskRunner();
            //runner.execute("0");
            first=false;
        }


        goal = SharedPrefData.getGoal(getContext());

        ct=getContext();

        curr_steps = gFit.getTotalDailySteps();
        activity=getActivity();


        Intent intent = getActivity().getIntent();
//        if (intent.getStringExtra("intentional_steps") != null) {
//            intentional_steps = Integer.parseInt(intent.getStringExtra("intentional_steps"));
//            curr_steps = curr_steps + intentional_steps;
//        }




        // display goal and current steps
        ((TextView)getView().findViewById(R.id.goal)).setText(Integer.toString(goal));
        ((TextView)getView().findViewById(R.id.display)).setText(Long.toString( gFit.getTotalDailySteps()));

        Button start_btn = getView().findViewById(R.id.start);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runner.cancel(true);

                launchActivity();
            }
        });

    }

    @Override
    public void onPause(){

        Log.d("msg:", "pause");
        //runner.cancel(true);
        Log.d("status",runner.getStatus().toString());

        super.onPause();
    }

    @Override
    public void onResume(){
        Log.d("reach", "yes");

        goal=SharedPrefData.getGoal(getContext());
        runner.cancel(true);
        runner=new AsyncTaskRunner();
        runner.execute("0");
        super.onResume();
        temp.inflate(R.layout.fragment_home, null);
    }
    private void launchActivity() {
        Intent intent = new Intent(getActivity(), TrackerActivity.class);
        startActivity(intent);
    }


    public static void async() {
        goal=SharedPrefData.getGoal(ct);
        runner.execute("0");
    }

    public void show(){
        runner.cancel(true);
        runner=new AsyncTaskRunner();

        Encouragement e =new Encouragement(activity);
        e.showChangeGoal();
    }


    public void improve(int numStepsOver){
        runner.cancel(true);
        runner=new AsyncTaskRunner();

        Encouragement e =new Encouragement(activity);
        e.displayImprovement(numStepsOver);
    }

    public static void killRunner(){
        if(!runner.isCancelled())
            runner.cancel(true);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        long updated_steps = gFit.getTotalDailySteps();
        int numStepsOver = 0;

        @Override
        protected String doInBackground(String... params) {
            int i=0;
            while(true) {
                i++;
                Encouragement en=new Encouragement(getActivity());
                if(en.getTime().equals("23:59:59")){
                    Encouragement.first_pg=true;
                    Encouragement.first_pi=true;
                }

                updated_steps=gFit.getTotalDailySteps();
                gFit.readWeeklyStepData();
                gFit.readYesterdayStepData();

                try {
                    publishProgress();
                    Thread.sleep(1000);
                    goal = SharedPrefData.getGoal(ct);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isCancelled()){break;}

                if(updated_steps >=goal){
                    return("5");
                }

                Log.d("time", en.getTime());

                // Checks for displaying encouragement at 8pm every night
                numStepsOver = calculateImprovementInterval((int) updated_steps, en.getPreviousDayStep());
                if (en.getTime().equals("20:00:00") && numStepsOver >= 500) {
                    return ("6");
                }

            }
            return ("10");
        }


        @Override
        protected void onPostExecute(String result) {
            display_goal.setText(Long.toString(goal));
            display_steps.setText(Long.toString(updated_steps));

            if(Integer.parseInt(result)==5)
                show();
            if(Integer.parseInt(result)==6)
                improve(numStepsOver);

        }

        @Override
        protected void onPreExecute() {
            gFit.subscribeForWeeklySteps();
        }

        @Override
        protected void onProgressUpdate(String... count) {
            display_goal.setText(Integer.toString(goal));
            display_steps.setText(Long.toString(updated_steps));

            for(int i = 0; i < GoogleFit.weekSteps.length; i++)
            {
                HistoryFragment.TOTAL_STEPS[i] = GoogleFit.weekSteps[i];
            }
        }

        // Calculate what interval encouragement pop-up should display (increased 500, 1000, etc.)
        private int calculateImprovementInterval(int todaySteps, int yesterdaySteps) {
            return ((todaySteps - yesterdaySteps) / 500) * 500;
        }
    }
}


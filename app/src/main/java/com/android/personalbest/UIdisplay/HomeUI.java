package com.android.personalbest.UIdisplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.personalbest.R;
import com.android.personalbest.SharedPrefData;
import com.android.personalbest.fitness.FitServiceFactory;
import com.android.personalbest.fitness.GoogleFitAdaptor;
import com.android.personalbest.fitness.IFitService;

public class HomeUI extends Fragment {
    IFitService gFit;

    private static int goal;
    static long curr_steps;
    static AsyncTaskRunner runner;
    Activity activity;
    static boolean first=true;
    static LayoutInflater temp;
    static Context ct;
    static TextView display_goal;
    static TextView display_steps;

    private static String fitnessServiceKey = "GOOGLE_FIT";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        temp=inflater;
        //gFit = FitServiceFactory.create("Home", this.getActivity());

        //gFit = new GoogleFitAdaptor(this.getActivity());

        activity=getActivity();
        Bundle args = getArguments();
        fitnessServiceKey = args.getString("key");
        if(fitnessServiceKey==null)
            fitnessServiceKey="Google_Fit";
        Log.wtf("key",fitnessServiceKey);
        Toast.makeText(activity, fitnessServiceKey,Toast.LENGTH_LONG).show();
        gFit = FitServiceFactory.create(fitnessServiceKey, this.getActivity());
        gFit.setup();
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        gFit = FitServiceFactory.create("Home", this.getActivity());
//        gFit.setup();
        gFit = FitServiceFactory.create(fitnessServiceKey, this.getActivity());
        gFit.setup();
        ct=getContext();
        activity=getActivity();
        Bundle args = getArguments();
        String text = args.getString("key");
        Log.wtf("key",text);
        Toast.makeText(activity, text,Toast.LENGTH_LONG).show();


        display_goal = ((TextView)getView().findViewById(R.id.goal));
        display_steps = ((TextView)getView().findViewById(R.id.display));


        if(first){
            runner = new AsyncTaskRunner();
            //runner.execute("0");
            first=false;
        }
        goal = SharedPrefData.getGoal(getContext());
        curr_steps = gFit.getTotalDailySteps();

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
        Intent intent = new Intent(getActivity(), TrackerActivityUI.class);
        intent.putExtra("home to tracker", "GOOGLE_FIT");
        startActivity(intent);
    }

    public static void  async(){

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
                gFit.printRecentSteps();

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
                if(fitnessServiceKey.equals("test"))
                    break;
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
            if(Integer.parseInt(result)==10)
                killRunner();
        }

        @Override
        protected void onPreExecute() {
            gFit.subscribeForWeeklySteps();
        }

        @Override
        protected void onProgressUpdate(String... count) {
            display_goal.setText(Integer.toString(goal));
            if(gFit.getIsTimeChanged())
            {
                display_steps.setText(Integer.toString(gFit.getRecentSteps()[1]));
            }else {
                display_steps.setText(Long.toString(updated_steps));
            }

            for(int i = 0; i < gFit.getWeekSteps().length; i++)
            {
                ChartUI.TOTAL_STEPS[i] = gFit.getWeekSteps()[i];
            }
        }

        // Calculate what interval encouragement pop-up should display (increased 500, 1000, etc.)
        private int calculateImprovementInterval(int todaySteps, int yesterdaySteps) {
            return ((todaySteps - yesterdaySteps) / 500) * 500;
        }
    }
}


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

import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.User;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.fitness.FitServiceFactory;
import com.android.personalbest.fitness.GoogleFitAdaptor;
import com.android.personalbest.fitness.IFitService;
import com.android.personalbest.time.ITime;
import com.android.personalbest.time.TimeFactory;

import static com.android.personalbest.fitness.GoogleFitAdaptor.monthMap;

public class HomeUI extends Fragment {
    IFitService gFit;
    IFirestore firestore;
    static User user;
    static TextView display_goal;
    static TextView display_steps;
    private static int goal;
    private int ticks;
    static long curr_steps;
    static AsyncTaskRunner runner;
    Activity activity;
    static boolean first=true;
    static LayoutInflater temp;
    static Context ct;
    ITime time;

    private static String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = HomeUI.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        temp=inflater;

        activity=getActivity();
        Bundle args = getArguments();
        fitnessServiceKey = args.getString("key");
        if(fitnessServiceKey==null)
            fitnessServiceKey="Google_Fit";
        Log.wtf("key",fitnessServiceKey);

        gFit = FitServiceFactory.create(fitnessServiceKey, this.getActivity());
        gFit.setup();

        firestore=MainActivity.getFirestore();

        user= MainActivity.getCurrentUser();

        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ct=getContext();
        activity=getActivity();

        ticks = 0;
        time = TimeFactory.create("test");
        display_goal = getView().findViewById(R.id.goal);
        display_steps = getView().findViewById(R.id.display);


        if(first){
            runner = new AsyncTaskRunner();
            first=false;
        }

        curr_steps = gFit.getMonthSteps()[27];

        goal = user.getGoal();
        ((TextView)getView().findViewById(R.id.goal)).setText(Integer.toString(goal));
        ((TextView)getView().findViewById(R.id.display)).setText(Long.toString( curr_steps));

        Button start_btn = getView().findViewById(R.id.start);

        start_btn.setOnClickListener(view1 -> {
            runner.cancel(true);
            launchActivity();
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
        goal=user.getGoal();
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

    public static void async(){
        goal=user.getGoal();
        runner.execute("0");
    }

    public void show(){
        runner.cancel(true);
        runner=new AsyncTaskRunner();


        Encouragement e =new Encouragement(activity);
        e.showChangeGoal(user);
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

    public void testkillRunner(){
        if(!runner.isCancelled())
            runner.cancel(true);
        runner=new AsyncTaskRunner();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        long updated_steps = gFit.getTotalDailySteps();
        int numStepsOver = 0;
        Boolean hasShown = false;

        @Override
        protected String doInBackground(String... params) {

            while(true) {
                Encouragement en=new Encouragement(getActivity());
                if(en.getTime().equals("23:59:59")){
                    Encouragement.first_pg=true;
                    Encouragement.first_pi=true;
                }

                updated_steps=gFit.getTotalDailySteps();
                gFit.readWeeklyStepData();
                gFit.readMonthlyStepData();
                gFit.getYesterdaySteps();

                user.setTotalSteps(gFit.getMonthMap());

                if(ticks > 10)
                {
                    ticks = 0;
                    firestore.setTotalSteps(user);
                }else
                {
                    ticks++;
                }



                try {
                    publishProgress();

                    Thread.sleep(1000);

                    goal = user.getGoal();
                    //firestore.addGoalToDatabase();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isCancelled()){break;}

                if(updated_steps >=goal){
                    return("5");
                }

                Log.wtf("time", en.getTime());

                // Checks for displaying encouragement at 8pm every night
                numStepsOver = calculateImprovementInterval((int) updated_steps, GoogleFitAdaptor.weekSteps[5]);
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

            if(Integer.parseInt(result)==5){
                show();
            }
            if(Integer.parseInt(result)==6)
                improve(numStepsOver);
            if(Integer.parseInt(result)==10)
                testkillRunner();
        }

        @Override
        protected void onPreExecute() {

            gFit.subscribeForWeeklySteps();
        }

        @Override
        protected void onProgressUpdate(String... count) {
            display_goal.setText(Integer.toString(goal));
            display_steps.setText(Long.toString(updated_steps));

            for(int i = 0; i < gFit.getWeekSteps().length; i++)
            {

                HistoryFragment.TOTAL_STEPS[i] = gFit.getWeekSteps()[i];;

            }
            for (int i = 0; i < 28; i++) {
                ChartMonthDisplay.TOTAL_STEPS[i] = gFit.getMonthSteps()[i];

            }
        }

        // Calculate what interval encouragement pop-up should display (increased 500, 1000, etc.)
        private int calculateImprovementInterval(int todaySteps, int yesterdaySteps) {
            return ((todaySteps - yesterdaySteps) / 500) * 500;
        }

    }
}


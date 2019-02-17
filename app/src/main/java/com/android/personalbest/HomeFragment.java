package com.android.personalbest;

import android.app.Activity;
import android.app.Dialog;
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
    private int curr_steps;
    private static int goal;
    protected int intentional_steps = 0;
    Dialog myDialog;
    //Button btn;
    boolean ishown=false;
    FakeApi api;
    static AsyncTaskRunner runner;
    Activity activity;
    static boolean first=true;
    static LayoutInflater temp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        temp=inflater;
        super.onCreate(savedInstanceState);

        return inflater.inflate(R.layout.fragment_home, null);
    }
    public View getActivityView(){
        return getView();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        api=new FakeApi();
        if(first){
            runner = new AsyncTaskRunner();
            runner.execute("0");
            first=false;}
        // temp value
        goal = 5000;
        curr_steps = 2000;
        activity=getActivity();
        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("intentional_steps") != null) {
            intentional_steps = Integer.parseInt(intent.getStringExtra("intentional_steps"));
            curr_steps = curr_steps + intentional_steps;
        }

        // display goal and current steps
        ((TextView)getView().findViewById(R.id.goal)).setText(Integer.toString(goal));
        ((TextView)getView().findViewById(R.id.display)).setText(Integer.toString(curr_steps));

        Button start_btn = getView().findViewById(R.id.start);
        //btn=getView().findViewById(R.id.button);
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
        goal=Encouragement.getGoal();
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

    public static void  async(){
        goal=Encouragement.getGoal();

        runner.execute("0");
    }
    public void show(){
        Log.wtf("value",activity.toString());

        Encouragement e =new Encouragement(activity);
        e.showChangeGoal();

        runner.cancel(true);
        runner=new AsyncTaskRunner();
        Log.d("goal", String.valueOf(goal));
    }

    public void improve(){
        Encouragement e =new Encouragement(activity);
        e.displayImprovement();
        runner.cancel(true);
        runner=new AsyncTaskRunner();
    }
private class AsyncTaskRunner extends AsyncTask<String, String, String> {


    @Override
    protected String doInBackground(String... params) {
        int i=0;
        while(true) {
            i++;
            Encouragement en=new Encouragement(getActivity());
            int step=api.getStep();
            try {
                publishProgress(Integer.toString(step));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isCancelled()){break;}
            if(step>=goal){
                return("5");
            }
            Log.d("time", en.getTime());
            if(en.getTime().equals("20:00")&& step-en.getPreviousDayStep()>1000)
                return("6");


        }
        return ("10");
    }

    @Override
    protected void onPostExecute(String result) {
        if(Integer.parseInt(result)==5)
            show();
        if(Integer.parseInt(result)==6)
            improve();
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... count) {
//        btn.findViewById(R.id.button);
//        btn.setText(String.valueOf(count[0]));
//        Log.d("button value", String.valueOf(count[0]));
    }
}
}

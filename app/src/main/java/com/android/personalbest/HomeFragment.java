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
    private DisplayEncouragement encouragement;
    Dialog myDialog;
    Button btn;
    static boolean isCancelled = false;
    FakeApi api;
    static AsyncTaskRunner runner;
    Activity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //mTextMessage = getView().findViewById(R.id.message);
        //getActivity().getContentView(R.layout.fragment_home);

        return inflater.inflate(R.layout.fragment_home, null);
    }
    public View getActivityView(){
        return getView();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //DisplayEncouragement displayEncouragement=new DisplayEncouragement(getActivity());
        api=new FakeApi();
        runner = new AsyncTaskRunner();
        runner.execute("0");
        // temp value
        goal = 5000;
        curr_steps = 2000;
        activity=getActivity();
        Intent intent = getActivity().getIntent();
        //Log.wtf("value",this.getActivity().toString());
        if (intent.getStringExtra("intentional_steps") != null) {
            intentional_steps = Integer.parseInt(intent.getStringExtra("intentional_steps"));
            curr_steps = curr_steps + intentional_steps;
        }

        // display goal and current steps
        ((TextView)getView().findViewById(R.id.goal)).setText(Integer.toString(goal));
        ((TextView)getView().findViewById(R.id.display)).setText(Integer.toString(curr_steps));

        Button start_btn = getView().findViewById(R.id.start);
        btn=getView().findViewById(R.id.button);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runner.cancel(true);
                launchActivity();
            }
        });

//        encouragement = new DisplayEncouragement(this.getActivity());
//        encouragement.execute();


//        myDialog = new Dialog(this.getActivity());
//        myDialog.setContentView(R.layout.activity_encouragement_reachgoal);
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();
    }
    @Override
    public void onPause(){

        runner.cancel(true);
        Log.d("status",runner.getStatus().toString());
        super.onPause();
    }
    @Override
    public void onResume(){
        Log.d("reach", "yes");
        goal=Encouragement.getGoal();
        //runner.cancel(true);
        runner=new AsyncTaskRunner();
        runner.execute("0");
        super.onResume();
    }
    private void launchActivity() {
        Intent intent = new Intent(getActivity(), TrackerActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void update(Observable observable, Object a) {
//        this.reachGoal = (boolean) a;
//        this.showImprovement = (boolean) b;
//
//
//    }
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

private class AsyncTaskRunner extends AsyncTask<String, String, String> {
    //boolean isCancelled = false;

    @Override
    protected String doInBackground(String... params) {
        int i=0;
        while(true) {
            i++;
            int step=api.getStep();
            try {
                publishProgress(Integer.toString(step));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isCancelled()){break;}
            if(isCancelled){
                return("10");
            }
            if(step>=goal){
                return("5");
            }


        }
        return ("10");
    }

    @Override
    protected void onPostExecute(String result) {
        if(Integer.parseInt(result)==5)
            show();
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... count) {
        btn.findViewById(R.id.button);
        btn.setText(String.valueOf(count[0]));
    }
}
}

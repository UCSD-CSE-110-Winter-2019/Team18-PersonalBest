package com.android.personalbest;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Encouragement {
    private String time = null;
    Dialog myDialog;
    Activity activity;
    static int goal=5000;
    static int count=0;
    public Encouragement(){};
    public Encouragement(Activity activity) {
        this.activity = activity;
    }

    public String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        time = sdf.format(cal.getTime());
        return time;
    }

    public int getPreviousDayStep() {
        return 1000;
    }


    // call the set goal function from GoogleFit class
    public static void incGoal(int inc) {
        goal=goal+inc;
        Log.d("inc goal", String.valueOf(goal));
    }

    public static int getGoal(){
        Log.d("ec goal",String.valueOf(goal));
        return goal;
    }
     public void showChangeGoal () {
         myDialog = new Dialog(activity);
         myDialog.setContentView(R.layout.activity_encouragement_reachgoal);
         myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         myDialog.show();
         Button incGoal=myDialog.findViewById(R.id.inc_goal_btn);
         Button back=myDialog.findViewById(R.id.back_home_btn);
         incGoal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 myDialog.dismiss();
                 incGoal(500);
                 HomeFragment.async();
             }
         });
         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 myDialog.dismiss();
                 HomeFragment.async();
             }
         });
     }


    public void displayImprovement() {
        myDialog = new Dialog(activity);
        myDialog.setContentView(R.layout.encouragement_improvement);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        Button getHome = myDialog.findViewById(R.id.back_home_btn);
        getHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                HomeFragment.async();
            }
        });

    }


}

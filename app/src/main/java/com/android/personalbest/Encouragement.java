package com.android.personalbest;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
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
    static boolean first_pg=true;
    static boolean first_pi=true;

    public Encouragement(){};
    SharedPreferences sharedPreferences;
    public Encouragement(Activity activity) {
        this.activity = activity;
    }

    public String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        time = sdf.format(cal.getTime());
        Log.wtf("time", time);
        return time;
    }

    public int getPreviousDayStep() {
        return 1000;
    }
    public void setGoal(int goal){
        SharedPrefData.setGoal(HomeFragment.ct, goal);
    }

    // call the set goal function from GoogleFit class
//    public static void incGoal(int inc) {
//        goal=goal+inc;
//        this.setGoal(goal);
//        Log.d("inc goal", String.valueOf(goal));
//    }


     public void showChangeGoal () {

         myDialog = new Dialog(activity);
         myDialog.setContentView(R.layout.activity_encouragement_reachgoal);

         if(first_pg){
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
            first_pg=false;
         }

         Button incGoal=myDialog.findViewById(R.id.inc_goal_btn);
         Button back=myDialog.findViewById(R.id.back_home_btn);
         incGoal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 myDialog.dismiss();
                 setGoal(500+SharedPrefData.getGoal(HomeFragment.ct));
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
        if(first_pi){
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
            first_pi=false;
        }

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

}

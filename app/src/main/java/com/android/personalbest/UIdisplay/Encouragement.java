package com.android.personalbest.UIdisplay;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.User;
import com.android.personalbest.fitness.GoogleFitAdaptor;
import com.android.personalbest.time.ITime;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Encouragement {


    private String time = null;
    Dialog myDialog;
    Activity activity;
    static boolean first_pg=true;
    static boolean first_pi=true;

    public Encouragement(){};

    public Encouragement(Activity activity) {
        this.activity = activity;
    }

    public String getTime() {
        ITime cal = MainActivity.getMainActivity().getITime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        time = sdf.format(cal.getTime());
        Log.wtf("time", time);
        return time;
    }

//    public int getPreviousDayStep() {
//        return GoogleFitAdaptor.recentSteps[0];
//    }




    // call the set goal function from GoogleFitAdaptor class
//    public static void incGoal(int inc) {
//        goal=goal+inc;
//        this.setGoal(goal);
//        Log.d("inc goal", String.valueOf(goal));
//    }


     public void showChangeGoal (User user) {

         myDialog = new Dialog(activity);
         myDialog.setContentView(R.layout.activity_encouragement_reachgoal);

         if(first_pg){
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
            first_pg=false;
         } else {
             HomeUI.async();
         }

         Button incGoal=myDialog.findViewById(R.id.inc_goal_btn);
         Button back=myDialog.findViewById(R.id.back_home_btn);

         // Displays the correct goal in pop-up
         TextView currentGoalView = myDialog.findViewById(R.id.current_goal);
         currentGoalView.setText(Integer.toString(user.getGoal()));

         incGoal.setOnClickListener(v -> {
             myDialog.dismiss();
             int cur_goal=500+user.getGoal();
             user.setGoal(cur_goal);
             MainActivity.getFirestore().setGoal(cur_goal);
             HomeUI.async();
         });
         back.setOnClickListener(v -> {
             myDialog.dismiss();
             HomeUI.async();
         });

     }


    public void displayImprovement(int numStepsOver) {
        myDialog = new Dialog(activity);
        myDialog.setContentView(R.layout.encouragement_improvement);
        if(first_pi){
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
            first_pi=false;
        } else {
            HomeUI.async();
        }

        Button getHome = myDialog.findViewById(R.id.back_home_btn);
        TextView exceedStepsView = myDialog.findViewById(R.id.exceed_steps);
        exceedStepsView.setText(Integer.toString(numStepsOver));

        getHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                HomeUI.async();
            }
        });

    }

     }



package com.android.personalbest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;
import static java.lang.System.out;
import static java.security.AccessController.getContext;

public class Encouragement {
    private String time = null;
    Dialog myDialog;
    Activity activity;
    int goal=5000;

    public Encouragement(Activity activity) {
        this.activity = activity;
        HomeFragment.isCancelled=true;
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


//    // call the set goal function from GoogleFit class
    public void incGoal(int inc) {
        this.goal=this.goal+inc;
        Log.d("inc goal", String.valueOf(goal));
    }
//
//    //go to profile to set new goal
//    public void setNewGoal(){
//
//        Intent intent= new Intent(activity, ProfileFragment.class);
//        startActivity(intent);
//    }
//
//    //if dismiss, go to home page
//    public void dismiss(){
//
//        Intent intent= new Intent(activity, HomeFragment.class);
//    }
    public int getGoal(){
        Log.d("ec goal",String.valueOf(goal));
        return goal;
    }
     public void showChangeGoal () {
         myDialog = new Dialog(activity);
         myDialog.setContentView(R.layout.activity_encouragement_reachgoal);
         myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         myDialog.show();
         Button incGoal=myDialog.findViewById(R.id.inc_goal_btn);
         Button newGoal=myDialog.findViewById(R.id.new_goal_btn);
         Button back=myDialog.findViewById(R.id.back_home_btn);
         incGoal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 myDialog.dismiss();
                 HomeFragment.isCancelled=false;
                 incGoal(2000);
             }
         });
         newGoal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 myDialog.dismiss();
             }
         });
         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 myDialog.dismiss();
             }
         });
     }


     public void displayImprovement() {
         myDialog = new Dialog(activity);
         myDialog.setContentView(R.layout.encouragement_improvement);
         myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         myDialog.show();

     }


}

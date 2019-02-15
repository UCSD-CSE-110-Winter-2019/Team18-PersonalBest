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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;
import static java.lang.System.out;

public class Encouragement {
    private String time = null;
    Dialog myDialog;
    Activity activity;

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
    public void setGoal() {


    }


     public void showChangeGoal () {
         myDialog = new Dialog(activity);
         myDialog.setContentView(R.layout.activity_encouragement_reachgoal);
         myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         myDialog.show();
     }


     public void displayImprovement() {
         myDialog = new Dialog(activity);
         myDialog.setContentView(R.layout.encouragement_improvement);
         myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         myDialog.show();

     }


}

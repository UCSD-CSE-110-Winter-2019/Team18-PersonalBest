package com.android.personalbest;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Encouragement extends AppCompatActivity {
    Dialog popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setGoal(int goal){
        SharedPreferences sharedPreferences=getSharedPreferences("info",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("goal",goal);
    }
    public int getGoal(){return 5000;}

    //go to profile to set new goal
    public void setNewGoal(View view){
        Intent intent= new Intent(Encouragement.this, ProfileFragment.class);
        startActivity(intent);
    }

    //if dismiss, go to home page
    public void dismiss(View view){
        Intent intent= new Intent(Encouragement.this, HomeFragment.class);
    }

    public void increaseGoal(View view){
        TextView goal=findViewById(R.id.current_goal);
        setGoal(Integer.parseInt(goal.toString())+500);
        Intent intent=new Intent(Encouragement.this, HomeFragment.class);
    }
}

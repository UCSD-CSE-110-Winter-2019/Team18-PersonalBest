package com.android.personalbest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.EditText;

public class CheckInvalid extends AppCompatActivity {

    public static boolean checkForName(Editable name){
        if(name.toString().isEmpty()){
            return false;
        }
        else
            return true;
    }
    public static int checkForHeightft(Editable height){
        int heightft=-1;
        try{
            heightft=Integer.parseInt(height.toString());
        }
        catch (Throwable e){
            return -1;
        }
        if(heightft<0||heightft>8){
            return -1;
        }
        return heightft;
    }
    public static int checkForHeightin(Editable height){
        int heightin=-1;
        try{
            heightin=Integer.parseInt(height.toString());
        }
        catch (Throwable e){
            return -1;
        }
        if(heightin<0||heightin>=12){
            return -1;
        }
        return heightin;
    }
    public static int checkForGoal(Editable goal_input){
        int goal=-1;
        try{
            goal=Integer.parseInt(goal_input.toString());
        }
        catch (Throwable e){
            return -1;
        }
        if(goal<0){
            return -1;
        }
        return goal;
    }
}

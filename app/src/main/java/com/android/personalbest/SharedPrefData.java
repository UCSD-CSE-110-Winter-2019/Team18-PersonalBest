package com.android.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Map;

public class SharedPrefData extends AppCompatActivity {

    private static final int NUM_DAYS_IN_WEEK = 7;
    private static final int NUM_MILLISECONDS_IN_DAY = 86400000;

    public SharedPrefData() {

    }

    // Retrieves the timestamp of the current day at 12:00am in milliseconds
    public static long getTodayInMilliseconds() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        return cal.getTimeInMillis();
    }


    // Gets all of the intentional step from SharedPreferences as an array with Sunday's steps at index 0
    public static int[] getIntentionalStepsFromSharedPrefs(Context context) {
        int[] intentionalSteps = new int[NUM_DAYS_IN_WEEK];
        Calendar cal = Calendar.getInstance();

        // Subtract 1 to match Sunday to 0 (Calendar API has Sunday to 1)
        int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        long sundayOfThisWeek = getTodayInMilliseconds() - (currentDayOfWeek * NUM_MILLISECONDS_IN_DAY);

        // TODO Ask about Google Accounts & used that as the first parameter instead
        long curDay = sundayOfThisWeek;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        for (int i = 0; i < intentionalSteps.length; i++) {
            String curDayKey = Long.toString(curDay);
            // Default value of 0 will handle later days of the week
            intentionalSteps[i] = sharedPreferences.getInt(curDayKey, 0);
            curDay += NUM_MILLISECONDS_IN_DAY;
        }

        return intentionalSteps;
    }


    // When the user presses the End Activity button, save the steps to SharedPreferences
    public static void saveStepsToSharedPreferences(Context context, int numSteps) {
        // Uses the current day at 12:00am as a key in SharedPreferences to keep track of all
        // intentional steps taken on this specific day
        String currentDayKey = Long.toString(getTodayInMilliseconds());

        // TODO Ask about Google Accounts & used that as the first parameter instead
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // If user has already had activities today, we need to get the previous total and sum
        if (sharedPreferences.contains(currentDayKey)) {
            int prevSteps = sharedPreferences.getInt(currentDayKey, 0);
            numSteps += prevSteps;
        }

        editor.putInt(currentDayKey, numSteps);
        editor.apply();
    }
}

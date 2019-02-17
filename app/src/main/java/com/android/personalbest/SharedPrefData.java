package com.android.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Calendar;
import java.util.Map;

public class SharedPrefData extends AppCompatActivity {

    private static final int NUM_DAYS_IN_WEEK = 7;
    private static final int NUM_MILLISECONDS_IN_DAY = 86400000;
    private static boolean infoIsInTemp = true;
    private static final String TAG = "SharedPrefData";

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


    private static String getLoggedInUserId(Context context) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        return account.getId();
    }


    // Gets all of the intentional step from SharedPreferences as an array with Sunday's steps at index 0
    public static int[] getIntentionalSteps(Context context) {
        int[] intentionalSteps = new int[NUM_DAYS_IN_WEEK];
        Calendar cal = Calendar.getInstance();

        // Subtract 1 to match Sunday to 0 (Calendar API has Sunday to 1)
        int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        long sundayOfThisWeek = getTodayInMilliseconds() - (currentDayOfWeek * NUM_MILLISECONDS_IN_DAY);

        long curDay = sundayOfThisWeek;
        String accountId = getLoggedInUserId(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);

        for (int i = 0; i < intentionalSteps.length; i++) {
            String curDayKey = Long.toString(curDay);
            // Default value of 0 will handle later days of the week
            intentionalSteps[i] = sharedPreferences.getInt(curDayKey, 0);
            curDay += NUM_MILLISECONDS_IN_DAY;
        }

        return intentionalSteps;
    }


    // When the user presses the End Activity button, save the steps to SharedPreferences
    public static void saveIntentionalSteps(Context context, int numSteps) {
        // Uses the current day at 12:00am as a key in SharedPreferences to keep track of all
        // intentional steps taken on this specific day
        String currentDayKey = Long.toString(getTodayInMilliseconds());

        String accountId = getLoggedInUserId(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // If user has already had activities today, we need to get the previous total and sum
        if (sharedPreferences.contains(currentDayKey)) {
            int prevSteps = sharedPreferences.getInt(currentDayKey, 0);
            numSteps += prevSteps;
        }

        editor.putInt(currentDayKey, numSteps);
        editor.apply();
    }


    public static int getGoal(Context context) {
        String accountId = getLoggedInUserId(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("goal", 5000);
    }


    public static void setGoal(Context context, int stepGoal) {
        String accountId = getLoggedInUserId(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("goal", stepGoal);
        editor.apply();
    }


    public static String getName(Context context) {
        if (infoIsInTemp) {
            transferInfoFromTempToAccountId(context);
        }

        String accountId = getLoggedInUserId(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", "");
    }


    public static void setName(Context context, String name) {
        String accountId = getLoggedInUserId(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.apply();
    }


    public static int getHeightFt(Context context) {
        if (infoIsInTemp) {
            transferInfoFromTempToAccountId(context);
        }

        String accountId = getLoggedInUserId(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("heightft", 0);
    }


    public static void setHeightFt(Context context, int heightFt) {
        String accountId = getLoggedInUserId(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("heightft", heightFt);
        editor.apply();
    }

    public static int getHeightIn(Context context) {
        if (infoIsInTemp) {
            transferInfoFromTempToAccountId(context);
        }

        String accountId = getLoggedInUserId(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("heightin", 0);
    }


    public static void setHeightIn(Context context, int heightIn) {
        String accountId = getLoggedInUserId(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("heightin", heightIn);
        editor.apply();
    }


    // Because on account creation, we do not have a logged in user's ID to associate their SharedPreferences
    // due to asynchronous nature, we store it to a temp SharedPreferences, using a flag to transfer
    // it over once the user is logged in
    public static void setInfoFromCreateAccount(Context context, String name, int heightFt, int heightIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("temp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.putInt("heightft", heightFt);
        editor.putInt("heightin", heightIn);
        editor.apply();
    }


    public static void transferInfoFromTempToAccountId(Context context) {
        Log.d(TAG, "transferInfoFromTempToAccountId: Transferring Info");

        SharedPreferences tempSharedPref = context.getSharedPreferences("temp", Context.MODE_PRIVATE);

        String accountId = getLoggedInUserId(context);
        SharedPreferences accountSharedPref = context.getSharedPreferences(accountId, Context.MODE_PRIVATE);
        SharedPreferences.Editor accountEditor = accountSharedPref.edit();

        accountEditor.putString("name", tempSharedPref.getString("name", ""));
        accountEditor.putInt("heightft", tempSharedPref.getInt("heightft",0));
        accountEditor.putInt("heightin", tempSharedPref.getInt("heightin", 0));
        accountEditor.apply();

        infoIsInTemp = false;
    }
}

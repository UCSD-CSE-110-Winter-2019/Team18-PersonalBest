package com.android.personalbest.UIdisplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import com.android.personalbest.Chart;
import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefData;
import com.android.personalbest.User;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.fitness.FitServiceFactory;
import com.android.personalbest.fitness.IFitService;
import com.android.personalbest.time.ITime;
import com.android.personalbest.time.TimeFactory;
import com.github.mikephil.charting.charts.BarChart;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ChartMonthDisplay extends AppCompatActivity {
    private final static int NUM_DAYS_IN_WEEK = 7;
    private final static int NUM_MILLISECONDS_IN_DAY = 86400000;
    private final static int NUM_DAYS_IN_MONTH = 28;

    private int[] total_steps;
    private int[] intentional_steps;
    private int[] incidental_steps;
    private int goal;

    IFirestore firestore;
    User user;
    ITime time;

    public static int[] TOTAL_STEPS = new int[28];
    public static int[] FRIEND_TOTAL_STEPS = new int[28];
    IFitService gFit;
    String id;

    boolean isFriendChart;

    private final String IS_FRIEND_CHART_KEY = "IS_FRIEND_CHART_KEY";
    private final String MONTH_TOTAL_STEPS_KEY = "MONTH_TOTAL_STEPS";
    private final String MONTH_INTENTIONAL_STEPS_KEY = "MONTH_INTENTIONAL_STEPS";
    private final String GOAL_KEY = "GOAL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_month);

        //Creates a time object that represents the current time.
        time = TimeFactory.create("test");

        // Get instance of Firestore from MainActivity and get the current logged in user
        firestore = MainActivity.getFirestore();
        user = MainActivity.getCurrentUser();

        intentional_steps = new int[NUM_DAYS_IN_MONTH];
        incidental_steps = new int[NUM_DAYS_IN_MONTH];

        //Create Chart UI
        Toolbar header = findViewById(R.id.header);

        header.setTitle("Monthly Report");
        header.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        header.setNavigationOnClickListener(view -> {
            finish();
        });

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            id =(String) b.get("key");
        }


        //Initialize gFit
//        gFit = FitServiceFactory.create(MainActivity.fitness_indicator, this);
        gFit = FitServiceFactory.create("real", this);


        // Flag for whether UI should use TOTAL_STEPS (Google Fit for current logged in user OR
        // Passed in total steps array because displaying a friend
        isFriendChart = iin.getBooleanExtra(IS_FRIEND_CHART_KEY, false);
        if (isFriendChart) {
            FRIEND_TOTAL_STEPS = iin.getIntArrayExtra(MONTH_TOTAL_STEPS_KEY);
        }


        //Get intentional and incidental steps
        intentional_steps = iin.getIntArrayExtra(MONTH_INTENTIONAL_STEPS_KEY);
        calculateIncidentalSteps();

        goal = iin.getIntExtra(GOAL_KEY, 5000);

        BarChart stepChart = findViewById(R.id.chart_month);

        Chart chart = new Chart("month", intentional_steps, incidental_steps, goal, stepChart);
        chart.createChart();
        displayTotalSteps();
    }


    /**
     * Subtracts intentional steps from total steps to be able to calculate the incidental steps
     */
    public void calculateIncidentalSteps()
    {
        if (isFriendChart) {
            for (int i = 0; i < NUM_DAYS_IN_MONTH; i++) {
                int numIncidentalSteps = FRIEND_TOTAL_STEPS[i] - intentional_steps[i];
                if (numIncidentalSteps < 0) {
                    numIncidentalSteps = 0;
                }
                incidental_steps[i] = numIncidentalSteps;
            }

        } else {
            for (int i = 0; i < NUM_DAYS_IN_MONTH; i++) {
                int numIncidentalSteps = TOTAL_STEPS[i] - intentional_steps[i];
                if (numIncidentalSteps < 0) {
                    numIncidentalSteps = 0;
                }
                incidental_steps[i] = numIncidentalSteps;
            }
        }
    }


    /**
     * Display total steps of the entire month
     */
    public void displayTotalSteps() {
        TextView total_steps = findViewById(R.id.total_month_steps);
        if (isFriendChart) {
            int totalMonthSteps = 0;

            for (int i = 0; i < FRIEND_TOTAL_STEPS.length; i++) {
                totalMonthSteps += FRIEND_TOTAL_STEPS[i];
            }

            total_steps.setText(Integer.toString(totalMonthSteps));

        } else {
            int totalMonthSteps = 0;

            for (int i = 0; i < TOTAL_STEPS.length; i++) {
                totalMonthSteps += TOTAL_STEPS[i];
            }

            total_steps.setText(Integer.toString(totalMonthSteps));
        }

    }


    // Retrieves the timestamp of the current day at 12:00am in milliseconds
    private static long getTodayInMilliseconds() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        return cal.getTimeInMillis();
    }


    // Creates an array of 28 days of total steps to prepare for month view
    public static int[] prepareTotalStepsForMonthView(User curUser) {
        Map<String, Integer> intentionalSteps = curUser.getTotalSteps();
        long curDayInMilliseconds = getTodayInMilliseconds();

        int[] monthTotalSteps = new int[NUM_DAYS_IN_MONTH];
        for (int i = NUM_DAYS_IN_MONTH - 1; i >= 0; i--) {
            if (intentionalSteps.containsKey(Long.toString(curDayInMilliseconds))) {
                monthTotalSteps[i] = intentionalSteps.get(Long.toString(curDayInMilliseconds));
            } else {
                monthTotalSteps[i] = 0;
            }
            curDayInMilliseconds -= NUM_MILLISECONDS_IN_DAY;
        }

        return monthTotalSteps;
    }


    // Creates an array of 28 days of intentional steps to prepare for month view
    public static int[] prepareIntentionalStepsForMonthView(User curUser) {
        Map<String, Integer> intentionalSteps = curUser.getIntentionalSteps();
        long curDayInMilliseconds = getTodayInMilliseconds();

        int[] monthIntentionalSteps = new int[NUM_DAYS_IN_MONTH];
        for (int i = NUM_DAYS_IN_MONTH - 1; i >= 0; i--) {
            if (intentionalSteps.containsKey(Long.toString(curDayInMilliseconds))) {
                monthIntentionalSteps[i] = intentionalSteps.get(Long.toString(curDayInMilliseconds));
            } else {
                monthIntentionalSteps[i] = 0;
            }
            curDayInMilliseconds -= NUM_MILLISECONDS_IN_DAY;
        }

        return monthIntentionalSteps;
    }


}

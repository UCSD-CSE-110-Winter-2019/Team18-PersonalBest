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
import java.util.HashMap;
import java.util.Map;


public class ChartMonthDisplay extends AppCompatActivity {
    private final int NUM_DAYS_IN_MONTH = 28;
    private int[] total_steps;
    private Map<String, Integer> intentional_stepsMap;
    private int[] intentional_steps;
    private int[] incidental_steps;
    private Map<String, Integer> userMonthSteps;
    private Map<String, Integer> monthStepsDisplay;
    private int goal;
    IFirestore firestore;
    User user;
    ITime time;

    public static int[] TOTAL_STEPS = new int[28];
    IFitService gFit;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_month);

        //Creates a time object that represents the current time.
        time = TimeFactory.create(0);

        // Get instance of Firestore from MainActivity and get the current logged in user
        firestore = MainActivity.getFirestore();
        user = MainActivity.getCurrentUser();

        intentional_steps = new int[NUM_DAYS_IN_MONTH];
        incidental_steps = new int[NUM_DAYS_IN_MONTH];

        userMonthSteps = user.getTotalSteps();
        intentional_stepsMap = user.getIntentionalSteps();
        monthStepsDisplay = new HashMap<>();
        goal = user.getGoal();

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


//        gFit = FitServiceFactory.create(MainActivity.fitness_indicator, this);
        gFit = FitServiceFactory.create("real", this);
        gFit.subscribeForWeeklySteps();
        gFit.readMonthlyStepData();
        gFit.readWeeklyStepData();
        calculateIncidentalSteps();
        createMapOfThisMonth();
        setIntSteps();

        BarChart stepChart = findViewById(R.id.chart_month);

        Chart chart = new Chart("month", intentional_steps, incidental_steps, goal, stepChart);
        chart.createChart();
        displayIntentionalSteps();
    }


    // Subtracts intentional steps from total steps to be able to calculate the incidental steps
    public void calculateIncidentalSteps()
    {
        int monthCounter = 27;
        for( int i = 0; i < NUM_DAYS_IN_MONTH; i++ )
        {
            String day = Long.toString( time.getDay(i) );

            if( userMonthSteps.containsKey(day) && intentional_stepsMap.containsKey(day) )
            {
                incidental_steps[monthCounter] = userMonthSteps.get(day) - intentional_stepsMap.get(day);
            }
            monthCounter--;
        }
    }


    public void displayIntentionalSteps() {
        TextView total_steps = findViewById(R.id.total_month_steps);
        int totalMonthSteps = 0;

        for (int i = 0; i < TOTAL_STEPS.length; i++) {
            totalMonthSteps += TOTAL_STEPS[i];
        }

        total_steps.setText(Integer.toString(totalMonthSteps));
    }

    //Create a map with keys that are the past 28 days.
    private void createMapOfThisMonth()
    {
        for( int i = 0; i < NUM_DAYS_IN_MONTH; i++ )
        {
            monthStepsDisplay.put(Long.toString(time.getDay(i)), 0);
        }
    }

    private void setIntSteps()
    {
        int monthCounter = 27;
        for( int i = 0; i < NUM_DAYS_IN_MONTH; i++ )
        {
            long day = time.getDay(i);
            if( intentional_stepsMap.containsKey(day) )
            {
                this.intentional_steps[monthCounter] = intentional_stepsMap.get(day);
            }
            monthCounter--;
        }
    }


}

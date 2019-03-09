package com.android.personalbest.UIdisplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.android.personalbest.Chart;
import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefData;
import com.android.personalbest.fitness.FitServiceFactory;
import com.android.personalbest.fitness.IFitService;
import com.github.mikephil.charting.charts.BarChart;


public class ChartMonthDisplay extends AppCompatActivity {
    private final int NUM_DAYS_IN_MONTH = 28;
    private int[] total_steps;
    private int[] intentional_steps;
    private int[] incidental_steps;
    private int goal;

    public static int[] TOTAL_STEPS = new int[28];
    IFitService gFit;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_month);
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


        gFit = FitServiceFactory.create(MainActivity.fitness_indicator, this);
        // TODO: change to month steps
        // TODO: fetch monthly data based on the key
        gFit.subscribeForWeeklySteps();

//        intentional_steps = SharedPrefData.getIntentionalSteps(this);
        intentional_steps = new int[28];
        goal = SharedPrefData.getGoal(this);
        incidental_steps = new int[NUM_DAYS_IN_MONTH];
        calculateIncidentalSteps();

        BarChart stepChart = findViewById(R.id.chart_month);

        Chart chart = new Chart("month", intentional_steps, incidental_steps, goal, stepChart);
        chart.createChart();
        displayIntentionalSteps();
    }


    // Subtracts intentional steps from total steps to be able to calculate the incidental steps
    public void calculateIncidentalSteps() {
        for (int i = 0; i < NUM_DAYS_IN_MONTH; i++) {
            int numIncidentalSteps = TOTAL_STEPS[i] - intentional_steps[i];
            if (numIncidentalSteps < 0) {
                numIncidentalSteps = 0;
            }
            incidental_steps[i] = numIncidentalSteps;
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


}

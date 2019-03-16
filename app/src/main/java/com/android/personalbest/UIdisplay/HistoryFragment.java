package com.android.personalbest.UIdisplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.personalbest.Chart;
import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.User;
import com.android.personalbest.fitness.FitServiceFactory;
import com.android.personalbest.fitness.IFitService;
import com.android.personalbest.time.ITime;
import com.github.mikephil.charting.charts.BarChart;

import java.util.Map;

public class HistoryFragment extends Fragment{
    private final int NUM_DAYS_IN_WEEK = 7;
    private final int NUM_DAYS_IN_MONTH = 28;
    private final int NUM_MILLISECONDS_IN_DAY = 86400000;
    private int[] total_steps;
    private int[] intentional_steps;
    private int[] incidental_steps;
    private int goal;
    User user;
    ITime time;
    private TextView stepGoalView;
    private TextView intentionalStepsCountView;
    IFitService gFit;
    public static int[] TOTAL_STEPS = new int[7];
    String fitnessServiceKey;
    private final String IS_FRIEND_CHART_KEY = "IS_FRIEND_CHART_KEY";
    private final String MONTH_TOTAL_STEPS_KEY = "MONTH_TOTAL_STEPS";
    private final String MONTH_INTENTIONAL_STEPS_KEY = "MONTH_INTENTIONAL_STEPS";
    private final String GOAL_KEY = "GOAL";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user=MainActivity.getCurrentUser();
        time=MainActivity.getITime();
        Bundle args = getArguments();
        fitnessServiceKey = args.getString("key");
        Map<String, Integer> intentionalSteps = user.getIntentionalSteps();
        long curDayInMilliseconds = getTodayInMilliseconds();
        intentional_steps = new int[NUM_DAYS_IN_WEEK];

        // get the intentional steps for the week
        for (int i = NUM_DAYS_IN_WEEK - 1; i >= 0; i--) {
            if (intentionalSteps.containsKey(Long.toString(curDayInMilliseconds))) {
                intentional_steps[i] = intentionalSteps.get(Long.toString(curDayInMilliseconds));
            } else {
                intentional_steps[i] = 0;
            }
            curDayInMilliseconds -= NUM_MILLISECONDS_IN_DAY;
        }

        goal = user.getGoal();

        // get the incidental steps for the week
        incidental_steps = new int[NUM_DAYS_IN_WEEK];
        calculateIncidentalSteps();
        return inflater.inflate(R.layout.fragment_history, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BarChart stepChart = getView().findViewById(R.id.chart);

        // create a chart and get steps
        Chart chart = new Chart("week", intentional_steps, incidental_steps, goal, stepChart);
        chart.createChart();

        displayStepGoal();
        displayIntentionalSteps();

        gFit = FitServiceFactory.create(fitnessServiceKey, this.getActivity());
        gFit.subscribeForWeeklySteps();

        Button month_chart = getView().findViewById(R.id.monthly_chart);

        // go to the monthly chart page when clicking on the button
        month_chart.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ChartMonthDisplay.class);
            // TODO: PASS USER ID
            intent.putExtra("key","id");

            // Pass in the user's monthly information
            intent.putExtra(IS_FRIEND_CHART_KEY, false);
            intent.putExtra(MONTH_INTENTIONAL_STEPS_KEY, ChartMonthDisplay.prepareIntentionalStepsForMonthView(this.user));
            intent.putExtra(GOAL_KEY, this.user.getGoal());
            startActivity(intent);
        });
    }


    // Retrieves the timestamp of the current day at 12:00am in milliseconds
    private long getTodayInMilliseconds() {
        return time.getTodayInMilliseconds();
    }


    // Display the Step Goal on the History Fragment
    public void displayStepGoal() {
        stepGoalView = getView().findViewById(R.id.stepGoal);
        stepGoalView.setText(Integer.toString(goal));
    }



    // Sums all of the values in the intentionalSteps array to get the total for the week
    private int getTotalIntentionalStepsInWeek() {
        int totalIntentionalStepsInWeek = 0;

        for (int i = 0; i < intentional_steps.length; i++) {
            totalIntentionalStepsInWeek += intentional_steps[i];
        }

        return totalIntentionalStepsInWeek;
    }


    // Display the current week's worth of intentional step goals on the History Fragment
    public void displayIntentionalSteps() {
        intentionalStepsCountView = getView().findViewById(R.id.intentionalStepsCount);
        intentionalStepsCountView.setText(Integer.toString(getTotalIntentionalStepsInWeek()));
    }


    // Subtracts intentional steps from total steps to be able to calculate the incidental steps
    public void calculateIncidentalSteps() {
        for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
            int numIncidentalSteps = TOTAL_STEPS[i] - intentional_steps[i];
            if (numIncidentalSteps < 0) {
                numIncidentalSteps = 0;
            }
            incidental_steps[i] = numIncidentalSteps;
        }
    }

}

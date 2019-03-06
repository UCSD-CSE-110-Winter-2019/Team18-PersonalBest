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
import com.android.personalbest.SharedPrefData;
import com.android.personalbest.fitness.FitServiceFactory;
import com.android.personalbest.fitness.IFitService;
import com.github.mikephil.charting.charts.BarChart;

public class HistoryFragment extends Fragment {
    private final int NUM_DAYS_IN_WEEK = 7;
    private int[] total_steps;
    private int[] intentional_steps;
    private int[] incidental_steps;
    private int goal;


    private TextView stepGoalView;
    private TextView intentionalStepsCountView;
    IFitService gFit;
    public static int[] TOTAL_STEPS = new int[7];


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentional_steps = SharedPrefData.getIntentionalSteps(this.getActivity());
        goal = SharedPrefData.getGoal(this.getActivity());
        incidental_steps = new int[NUM_DAYS_IN_WEEK];
        calculateIncidentalSteps();
        return inflater.inflate(R.layout.fragment_history, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BarChart stepChart = getView().findViewById(R.id.chart);

        Chart chart = new Chart("week", intentional_steps, incidental_steps, goal, stepChart);
        chart.createChart();

        displayStepGoal();
        displayIntentionalSteps();

        gFit = FitServiceFactory.create(MainActivity.fitness_indicator, this.getActivity());
        gFit.subscribeForWeeklySteps();

        Button month_chart = getView().findViewById(R.id.monthly_chart);
        month_chart.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ChartMonthDisplay.class);
            // TODO: PASS USER ID
            intent.putExtra("key","id");
            startActivity(intent);
        });
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

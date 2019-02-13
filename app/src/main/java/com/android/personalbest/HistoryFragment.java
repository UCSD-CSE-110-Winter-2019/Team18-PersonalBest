package com.android.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private TextView stepGoalView;
    private TextView intentionalStepsCountView;


    BarChart stepChart;
    List<BarEntry> entries = new ArrayList<>();
    BarDataSet set;
    BarData data;

    private static final int[] TEST_INCIDENTAL_STEPS = new int[] {1234, 432, 142, 4325, 0 , 0, 0};
//    private static final int[] TEST_INTENTIONAL_STEPS = new int[] {0, 620, 567, 1234, 0, 0, 0};
    private static final int INCIDENTAL_STEP_COLOR = Color.argb(200, 247, 215, 89);
    private static final int INTENTIONAL_STEP_COLOR = Color.argb(200, 41, 155, 242);
    private static final int TEST_STEP_GOAL = 5000;
    private static final int NUM_DAYS_IN_WEEK = 7;
    private static final String[] DAYS_OF_WEEK = new String[] {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};

    private int[] intentionalSteps = new int[NUM_DAYS_IN_WEEK];
    private static final int NUM_MILLISECONDS_IN_DAY = 86400000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_history, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.stepChart = getView().findViewById(R.id.chart);
        createChart();
        displayStepGoal();
        displayIntentionalSteps();
    }

    // Create the BarEntry objects for the stacked bars
    public void createBarEntries(int[] incidentalSteps, int[] intentionalSteps) {
        for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
            entries.add(new BarEntry(i, new float[]{incidentalSteps[i], intentionalSteps[i]}));
        }
    }

    // Creates the BarDataSet from the BarEntries with the right colors and labels
    public void configureBarDataSet() {
        this.set = new BarDataSet(this.entries, "");
        set.setColors(new int[] {INCIDENTAL_STEP_COLOR, INTENTIONAL_STEP_COLOR });
        set.setStackLabels(new String[]{"Incidental Steps", "Intentional Steps"});
    }


    // Creates the BarData & configures the width of the bars
    public void configureBarData() {
        this.data = new BarData(this.set);
        this.data.setBarWidth(0.8f);
    }


    // Set the x-axis labels to be the shortened days of the week
    public void configureXAxisLabels() {
        XAxis xAxis = stepChart.getXAxis();
        xAxis.setValueFormatter(new StepXAxisValueFormatter(this.DAYS_OF_WEEK));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }


    // Adds a red line on the graph for the Step Goal
    public void createLineForStepGoal(int stepGoal) {
        YAxis leftAxis = stepChart.getAxisLeft();
        LimitLine ll = new LimitLine(stepGoal, "Step Goal");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(3f);
        ll.setTextColor(Color.BLACK);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        leftAxis.addLimitLine(ll);
    }


    // Configures the chart, disabling unnecessary function and minor UI features
    public void configureStepChart() {
        stepChart.getAxisRight().setEnabled(false);
        stepChart.setData(data);
        stepChart.setFitBars(true);
        stepChart.setHighlightPerDragEnabled(false);
        stepChart.setHighlightPerTapEnabled(false);
        stepChart.setPinchZoom(false);
        stepChart.setDoubleTapToZoomEnabled(false);
        stepChart.getDescription().setEnabled(false);
        stepChart.invalidate();
    }



    // Main driver method that calls other set-up/configure methods
    public void createChart() {
        getIntentionalStepsFromSharedPrefs();
        createBarEntries(TEST_INCIDENTAL_STEPS, this.intentionalSteps);
        configureBarDataSet();
        configureBarData();
        configureXAxisLabels();
        createLineForStepGoal(getStepGoal());
        configureStepChart();
    }

    // TODO Eventually will get Step Goal from sharedPreferences, currently just returns testing instance variable
    private int getStepGoal() {
        return TEST_STEP_GOAL;
    }


    // Display the Step Goal on the History Fragment
    public void displayStepGoal() {
        stepGoalView = getView().findViewById(R.id.stepGoal);
        stepGoalView.setText(Integer.toString(getStepGoal()));
    }


    private void getIntentionalStepsFromSharedPrefs() {
        Calendar cal = Calendar.getInstance();
        // Subtract 1 to match Sunday to 0 (Calendar API has Sunday to 1)
        int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        long sundayOfThisWeek = getTodayInMilliseconds() - (currentDayOfWeek * NUM_MILLISECONDS_IN_DAY);

        // TODO Ask about Google Accounts & used that as the first parameter instead
        long curDay = sundayOfThisWeek;
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user_name", Context.MODE_PRIVATE);
        for (int i = 0; i < this.intentionalSteps.length; i++) {
            String curDayKey = Long.toString(curDay);
            // Default value of 0 will handle later days of the week
            this.intentionalSteps[i] = sharedPreferences.getInt(curDayKey, 0);
            curDay += NUM_MILLISECONDS_IN_DAY;
        }
    }



    // Retrieves the timestamp of the current day at 12:00am in milliseconds
    private long getTodayInMilliseconds() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        return cal.getTimeInMillis();
    }


    // Sums all of the values in the intentionalSteps array to get the total for the week
    private int getTotalIntentionalStepsInWeek() {
        int totalIntentionalStepsInWeek = 0;
        for (int i = 0; i < this.intentionalSteps.length; i++) {
            totalIntentionalStepsInWeek += intentionalSteps[i];
        }
        return totalIntentionalStepsInWeek;
    }


    // Display the current week's worth of intentional step goals on the History Fragment
    public void displayIntentionalSteps() {
        intentionalStepsCountView = getView().findViewById(R.id.intentionalStepsCount);
        intentionalStepsCountView.setText(Integer.toString(getTotalIntentionalStepsInWeek()));
    }

}

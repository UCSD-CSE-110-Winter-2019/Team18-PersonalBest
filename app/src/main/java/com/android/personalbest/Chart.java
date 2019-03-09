package com.android.personalbest;

import android.graphics.Color;
import android.util.Log;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Chart {
    List<BarEntry> entries = new ArrayList<>();
    BarDataSet set;
    BarData data;


    private static final int INCIDENTAL_STEP_COLOR = Color.argb(200, 247, 215, 89);
    private static final int INTENTIONAL_STEP_COLOR = Color.argb(200, 41, 155, 242);
    private static final String[] WEEK = new String[] {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
    private static final int Y_AXIS_MAX_PADDING = 500;


    private static String[] range_days;

    private int[] intentional_steps;
    private int[] incidental_steps;
    private int goal;
    private String type;
    private BarChart stepChart;


    public Chart(String type, int[] intentional_steps, int[] incidental_steps, int goal, BarChart stepChart) {

        this.intentional_steps = intentional_steps;
        this.incidental_steps = incidental_steps;
        this.goal = goal;
        this.type = type;
        this.stepChart = stepChart;
        Calendar calendar = Calendar.getInstance();

        if (type.equals("month")) {
            range_days = new String[28];

            for (int i = 0; i < range_days.length; i++) {
                Date today = new Date();
                calendar.setTime(today);
                calendar.add(Calendar.DAY_OF_MONTH, -28+i);

                int month = calendar.get(Calendar.MONTH)+1;
                String dayOfMonth = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                range_days[i] = month + "." + dayOfMonth;
                Log.wtf("day", range_days[i]);
            }

        } else if (type.equals("week")) {
            range_days = new String[7];

            int start_day = calendar.get(Calendar.DAY_OF_WEEK);
            for (int i = 0; i < 7; i++) {
                if (start_day + i < 7) {
                    range_days[i] = WEEK[start_day + i];
                } else {

                    range_days[i] = WEEK[start_day + i - 7];
                }
            }
        }
    }

    // Main driver method that calls other set-up/configure methods
    public void createChart() {
        createBarEntries(incidental_steps, intentional_steps);
        configureBarDataSet();
        configureBarData();
        configureXAxisLabels();
        createLineForStepGoal(goal);
        configureStepChart();
    }

    // Create the BarEntry objects for the stacked bars
    public void createBarEntries(int[] incidentalSteps, int[] intentionalSteps) {
        for (int i = 0; i < incidentalSteps.length; i++) {
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
        xAxis.setValueFormatter(new StepXAxisValueFormatter(this.range_days));
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
        stepChart.getAxisLeft().setAxisMaximum(goal + Y_AXIS_MAX_PADDING);
        stepChart.setData(data);
        stepChart.setFitBars(true);
        stepChart.setHighlightPerDragEnabled(false);
        stepChart.setHighlightPerTapEnabled(false);
        stepChart.setPinchZoom(false);
        stepChart.setDoubleTapToZoomEnabled(false);
        stepChart.getDescription().setEnabled(false);
        stepChart.invalidate();
    }
}

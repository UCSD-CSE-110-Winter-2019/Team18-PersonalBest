package com.android.personalbest.fitness;

import android.app.Activity;

import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

public class TestFitService implements IFitService {
    private static final String TAG = "[TestFitService]: ";
    private Activity activity;

    public TestFitService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void setup(){}
    public void subscribeForDailySteps(){}
    public void subscribeForWeeklySteps(){}
    public void readDailyStepData(){}
    public void readYesterdayStepData(){
    }
    public void readWeeklyStepData(){}
    public void updateToday(){}
    public long getTotalDailySteps(){
        return 0;
    }
    public void setTotalDailySteps(long total){}
    public void printWeekData(DataReadResponse dataReadResult){}
    public void printRecentSteps(){}
    public boolean getIsTimeChanged(){
        return false;
    }
    public void setIsTimeChanged(boolean timeChanged){}
}

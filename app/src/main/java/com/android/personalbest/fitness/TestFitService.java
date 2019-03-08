package com.android.personalbest.fitness;

import android.app.Activity;

import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

public class TestFitService implements IFitService {
    private static final String TAG = "[TestFitService]: ";
    private Activity activity;
    private static long TotalDailySteps;
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
    public void setTotalDailySteps(long total){TotalDailySteps=total;}
    public long getTotalDailySteps(){
        return TotalDailySteps;
    }
    public void printWeekData(DataReadResponse dataReadResult){}
    public void printRecentSteps(){}
    public boolean getIsTimeChanged(){
        return false;
    }
    public void setIsTimeChanged(boolean timeChanged){}

    @Override
    public int[] getRecentSteps() {
        int[] recentStep= new int[2];
        for(int i=0;i<2;i++){
            recentStep[i]=100;
        }
        return recentStep;
    }

    @Override
    public int[] getWeekSteps() {
        int[] weekStep= new int[7];
        for(int i=0;i<7;i++){
            weekStep[i]=100;
        }
        return weekStep;
    }
}

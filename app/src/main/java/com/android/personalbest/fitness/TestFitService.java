package com.android.personalbest.fitness;

import android.app.Activity;

import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

import java.util.Map;

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
    public void readWeeklyStepData(){}
    public void readMonthlyStepData(){}
    public void updateToday(){}
    public void setTotalDailySteps(long total){TotalDailySteps=total;}
    public long getTotalDailySteps(){
        return TotalDailySteps;
    }
    public void printWeekData(DataReadResponse dataReadResult){}
    public void printMonthData(DataReadResponse dataReadResult){}
    public boolean getIsTimeChanged(){
        return false;
    }
    public void setIsTimeChanged(boolean timeChanged){}
    public Task<Void> updateData(){return null;}
    public int getYesterdaySteps(){return 0;}

    @Override
    public int[] getMonthSteps() {
        int[] monthStep= new int[28];
        for(int i=0; i<monthStep.length; i++){
            monthStep[i]=100;
        }
        return monthStep;
    }

    @Override
    public int[] getWeekSteps() {
        int[] weekStep= new int[7];
        for(int i=0;i<7;i++){
            weekStep[i]=100;
        }
        return weekStep;
    }

    @Override
    public Map<Long, Integer> getMonthMap() { return null; }
}

package com.android.personalbest.fitness;

import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

import java.util.Map;

public interface IFitService {
    void setup();
    void subscribeForDailySteps();
    void subscribeForWeeklySteps();
    void readDailyStepData();
    void readWeeklyStepData();
    void readMonthlyStepData();
    void updateToday();
    long getTotalDailySteps();
    void setTotalDailySteps(long total);
    void printWeekData(DataReadResponse dataReadResult);
    void printMonthData(DataReadResponse dataReadResult);
    boolean getIsTimeChanged();
    void setIsTimeChanged(boolean timeChanged);
    int[] getWeekSteps();
    int[] getMonthSteps();
    Map<String, Integer> getMonthMap();
    int getYesterdaySteps();
    Task<Void> updateData();
}

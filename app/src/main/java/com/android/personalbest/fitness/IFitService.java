package com.android.personalbest.fitness;

import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

public interface IFitService {
    void setup();
    void subscribeForDailySteps();
    void subscribeForWeeklySteps();
    void readDailyStepData();
    void readWeeklyStepData();
    void updateToday();
    long getTotalDailySteps();
    void setTotalDailySteps(long total);
    void printWeekData(DataReadResponse dataReadResult);
    boolean getIsTimeChanged();
    void setIsTimeChanged(boolean timeChanged);
    int[] getWeekSteps();
    int[] getMonthSteps();
    int getYesterdaySteps();
    Task<Void> updateData();
}

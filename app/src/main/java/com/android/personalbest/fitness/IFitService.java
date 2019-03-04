package com.android.personalbest.fitness;

import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

public interface IFitService {
    void setup();
    void subscribeForDailySteps();
    void subscribeForWeeklySteps();
    void readDailyStepData();
    Task<DataReadResponse> readYesterdayStepData();
    Task<DataReadResponse> readWeeklyStepData();
    Task<Void> updateToday();
    long getTotalDailySteps();
    void setTotalDailySteps(long total);
    void printWeekData(DataReadResponse dataReadResult);
    void printRecentSteps();
    boolean getIsTimeChanged();
    void setIsTimeChanged(boolean timeChanged);
}

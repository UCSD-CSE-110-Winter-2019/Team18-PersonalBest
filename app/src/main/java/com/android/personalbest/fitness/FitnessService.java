package com.android.personalbest.fitness;

import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

public interface FitnessService {
    void setup();
//    void subscribeForDailySteps();
//    void subscribeForWeeklySteps();
//
//    void readDailyStepData();
//    Task<DataReadResponse> readYesterdayStepData();
//    Task<DataReadResponse> readWeeklyStepData();

//    static DataReadRequest queryYesterdayFitnessData();
//    static DataReadRequest queryWeekFitnessData();
//
//    void dumpDataSet(DataSet dataSet, int counter);
//
//    DataSet updateFitnessData();
//    DataSet addSteps();
//
//    void setTotalDailySteps(long total);
    long getTotalDailySteps();
}

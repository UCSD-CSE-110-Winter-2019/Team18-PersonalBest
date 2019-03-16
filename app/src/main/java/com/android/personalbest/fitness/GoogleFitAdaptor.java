package com.android.personalbest.fitness;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.personalbest.UIdisplay.ProfileUI;
import com.android.personalbest.time.ITime;
import com.android.personalbest.time.TimeFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;


public class GoogleFitAdaptor implements IFitService{
    Activity activity;

    public static final String TAG = "GoogleFitTag";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    public long total;
    private ITime time;
    public static int weekSteps[] = new int[7];
    public static int monthSteps[] = new int[28];
    public static Map<String, Integer> monthMap = new HashMap<>();
    public boolean changeTime = false;


    public GoogleFitAdaptor(Activity activity, String key)
    {
        this.activity = activity;
        this.time = TimeFactory.create(key);
    }


    //Asks user for permission to access data if it hasn't been asked for before.
    public void setup()
    {
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
                        .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(activity), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(activity),
                    fitnessOptions);
        } else {
            subscribeForDailySteps();
        }
    }

    /** Records step data by requesting a subscription to background step data. */
    public void subscribeForDailySteps() {
        Fitness.getRecordingClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Successfully subscribed!");
                                } else {
                                    Log.w(TAG, "There was a problem subscribing.", task.getException());
                                }
                            }
                        });
    }

    public boolean getIsTimeChanged() {
        return changeTime;
    }
    public void setIsTimeChanged(boolean changeTime) {
        this.changeTime = changeTime;
    }

    /** Asks for permission to access weekly steps */
    public void subscribeForWeeklySteps()
    {
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                        .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(activity), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(activity),
                    fitnessOptions);
        }
    }

    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
     */
    public void readDailyStepData() {
        Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                                setTotalDailySteps(total);
                            }
                        })
                .addOnFailureListener(
                        e -> Log.w("readDailyStepData", "There was a problem getting the step count.", e));
    }

    /**
     * Asynchronous task to read the history data. When the task succeeds, it will print out the week's data.
     */
    public void readWeeklyStepData() {
        DataReadRequest readRequest = queryWeekFitnessData();

        Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
            .readData(readRequest)
            .addOnSuccessListener(
                    new OnSuccessListener<DataReadResponse>() {
                        @Override
                        public void onSuccess(DataReadResponse dataReadResponse) {
                            printWeekData(dataReadResponse);
                        }
                    })
            .addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("readWeeklyStepData", "There was a problem reading the data.", e);
                        }
                    });
    }

    /**
     * Asynchronous task to read the history data. When the task succeeds, it will print out the month's data.
     */
    public void readMonthlyStepData() {
        DataReadRequest readRequest = queryMonthFitnessData();

        Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                printMonthData(dataReadResponse);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("readMonthlyStepData", "There was a problem reading the data.", e);
                            }
                        });
    }

    /** Returns a {@link DataReadRequest} for all step count changes starting from the past Sunday */
    public DataReadRequest queryWeekFitnessData() {
//        Calendar cal = Calendar.getInstance();
//        Date now = new Date();
//        if(this.changeTime)
//        {
//            cal.setTimeInMillis(ProfileUI.desiredTime);
//        }else {
//            cal.setTime(now);
//        }
//        long endTime = cal.getTimeInMillis();
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        long startTime = cal.getTimeInMillis();
        long startTime = time.timeOneWeekAgo();
        long endTime = time.timeNow();

        java.text.DateFormat dateFormat = getDateInstance();
        Log.i("QueryWeekFitnessData", "Range Start of weekly steps: " + dateFormat.format(startTime));
        Log.i("QueryWeekFitnessData", "Range End of weekly steps: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();
        return readRequest;
    }

    /** Returns a {@link DataReadRequest} for all step count changes starting from the past 28 days */
    public DataReadRequest queryMonthFitnessData() {
//        Calendar cal = Calendar.getInstance();
//        Date now = new Date();
//        if(this.changeTime)
//        {
//            cal.setTimeInMillis(ProfileUI.desiredTime);
//        }else {
//            cal.setTime(now);
//        }
//        long endTime = cal.getTimeInMillis();
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        long startTime = cal.getTimeInMillis();
        long startTime = time.timeOneMonthAgo();
        long endTime = time.timeNow();

        java.text.DateFormat dateFormat = getDateInstance();
        Log.i("QueryMonthFitnessData", "Range Start of monthly steps: " + dateFormat.format(startTime));
        Log.i("QueryMonthFitnessData", "Range End of monthly steps: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();
        return readRequest;
    }

    /** Logs a record of the query results */
    public void printWeekData(DataReadResponse dataReadResult) {
        int counter = 0;
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i("printWeekData",
                    "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpWeekSteps(dataSet, counter);
                    counter++;
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i("printWeekData",
                    "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpWeekSteps(dataSet, counter);
                counter++;
            }
        }
    }

    /** Logs a record of the query results */
    public void printMonthData(DataReadResponse dataReadResult) {
        int counter = 0;
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i("printMonthData",
                    "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpMonthSteps(dataSet, counter);
                    counter++;
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i("printMonthData",
                    "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpMonthSteps(dataSet, counter);
                counter++;
            }
        }
    }


    private void dumpWeekSteps(DataSet dataSet, int counter) {
        Log.i("dumpWeekSteps", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i("dumpWeekSteps", "Data point:");
            Log.i("dumpWeekSteps", "\tType: " + dp.getDataType().getName());
            Log.e("dumpWeekSteps", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.e("dumpWeekSteps", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i("dumpWeekSteps", "\tField: " + field.getName() + " Number of recent steps " + dp.getValue(field));
                weekSteps[counter] = (dp.getValue(field)).asInt();
            }
        }
    }

    private void dumpMonthSteps(DataSet dataSet, int counter) {
        Log.i("dumpMonthSteps", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i("dumpMonthSteps", "Data point:");
            Log.i("dumpMonthSteps", "\tType: " + dp.getDataType().getName());
            Log.e("dumpMonthSteps", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.e("dumpMonthSteps", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i("dumpMonthSteps", "\tField: " + field.getName() + " Number of recent steps " + dp.getValue(field));
                this.monthSteps[counter] = (dp.getValue(field)).asInt();
                this.monthMap.put(Long.toString( dp.getStartTime(TimeUnit.MILLISECONDS) ), (dp.getValue(field)).asInt());
            }
        }
    }

    /** Prints array contents for testing*/
    public void printWeekSteps() {
        for(int i = 0; i < weekSteps.length; i++)
        {
            Log.d("weekStep array contents", "" + weekSteps[i] );
        }
    }



    /**
     * Creates a {@link DataSet},then makes a {@link DataUpdateRequest} to update step data. Adds
     * steps to each day starting from past Sunday to now
     * Used for testing
     */
    public Task<Void> updateData() {
        DataSet dataSet = updateFitnessData();
        long startTime = 0;
        long endTime = 0;

        for (DataPoint dataPoint : dataSet.getDataPoints()) {
            startTime = dataPoint.getStartTime(TimeUnit.MILLISECONDS);
            endTime = dataPoint.getEndTime(TimeUnit.MILLISECONDS);
        }

        Log.i("updateData", "Updating the dataset in the History API.");

        DataUpdateRequest request =
                new DataUpdateRequest.Builder()
                        .setDataSet(dataSet)
                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();

        // Invoke the History API to update data.
        return Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .updateData(request)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // At this point the data has been updated and can be read.
                                    Log.i("updateData", "Data update was successful.");
                                } else {
                                    Log.e("updateData", "There was a problem updating the dataset.", task.getException());
                                }
                            }
                        });
    }

    public void updateToday() {
        // Create a new dataset and update request.
        DataSet dataSet = updateTodayFitnessData();
        long startTime = 0;
        long endTime = 0;

        // Get the start and end times from the dataset.
        for (DataPoint dataPoint : dataSet.getDataPoints()) {
            startTime = dataPoint.getStartTime(TimeUnit.MILLISECONDS);
            endTime = dataPoint.getEndTime(TimeUnit.MILLISECONDS);
        }

        Log.i("updateToday", "Updating the dataset in the History API.");

        DataUpdateRequest request =
                new DataUpdateRequest.Builder()
                        .setDataSet(dataSet)
                        .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();

        // Invoke the History API to update data.
        Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
            .updateData(request)
            .addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // At this point the data has been updated and can be read.
                            Log.i("updateToday", "Data update was successful.");
                        } else {
                            Log.e("updateToday", "There was a problem updating the dataset.", task.getException());
                        }
                    }
                 });
    }

    private DataSet updateTodayFitnessData() {
        Log.i("updateTodayFitnessData", "Creating a new data update request.");

        long startTime = time.timeStartToday();
        long endTime = time.timeNow();

        // Create a data source
        DataSource dataSource =
                new DataSource.Builder()
                        .setAppPackageName(activity)
                        .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .setStreamName("updateTodayFitnessData" + " - step count")
                        .setType(DataSource.TYPE_RAW)
                        .build();

        int stepCountDelta = 500 + weekSteps[6];
        DataSet dataSet = DataSet.create(dataSource);
        // For each data point, specify a start time, end time, and the data value -- in this case,
        // the number of new steps.
        DataPoint dataPoint =
                dataSet.createDataPoint().setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(dataPoint);
        // [END build_update_data_request]

        return dataSet;
    }

    /**
     * Creates and returns a {@link DataSet} of step count data to update.
     * Used for testing
     * */
    private DataSet updateFitnessData() {
        Log.i("updateFitnessData", "Creating a new data update request.");

//        Calendar testCal = Calendar.getInstance();
//        Date now = new Date();
//        testCal.setTime(now);
//        long endTime = testCal.getTimeInMillis();
//        testCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//        testCal.set(Calendar.HOUR_OF_DAY, 0);
//        testCal.set(Calendar.MINUTE, 0);
//        testCal.set(Calendar.SECOND, 0);
//        long startTime = testCal.getTimeInMillis();
        long startTime = time.timeOneWeekAgo();
        long endTime = time.timeNow();

        DataSource dataSource =
                new DataSource.Builder()
                        .setAppPackageName(activity)
                        .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .setStreamName("updateFitnessData" + " - step count")
                        .setType(DataSource.TYPE_RAW)
                        .build();

        Random rand = new Random(); //for testing
        int n = rand.nextInt(10000);

        int stepCountDelta = n;
        DataSet dataSet = DataSet.create(dataSource);
        DataPoint dataPoint =
                dataSet.createDataPoint().setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(dataPoint);

        return dataSet;
    }

    /** Creates a {@link DataSet} and inserts it into user's Google Fit history. */
    public Task<Void> insertData() {
        // Create a new dataset and insertion request.
        DataSet dataSet = insertFitnessData();

        // Then, invoke the History API to insert the data.
        Log.i(TAG, "Inserting the dataset in the History API.");
        return Fitness.getHistoryClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .insertData(dataSet)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                // At this point, the data has been inserted and can be read.
                                Log.wtf(TAG, "Data insert was successful!");
                            } else {
                                Log.wtf(TAG, "There was a problem inserting the dataset.", task.getException());
                            }
                        });
    }

    /**
     * Creates and returns a {@link DataSet} of step count data for insertion using the History API.
     */
    private DataSet insertFitnessData() {
        Log.i(TAG, "Creating a new data insert request.");

        // [START build_insert_data_request]
        // Set a start and end time for our data, using a start time of 1 hour before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        //cal.add(Calendar.HOUR_OF_DAY, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DAY_OF_WEEK, -1);
        long startTime = cal.getTimeInMillis();

        // Create a data source
        DataSource dataSource =
                new DataSource.Builder()
                        .setAppPackageName(activity)
                        .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .setStreamName(TAG + " - step count")
                        .setType(DataSource.TYPE_RAW)
                        .build();

        // Create a data set
        int stepCountDelta = 500;
        DataSet dataSet = DataSet.create(dataSource);
        // For each data point, specify a start time, end time, and the data value -- in this case,
        // the number of new steps.
        DataPoint dataPoint =
                dataSet.createDataPoint().setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(dataPoint);
        // [END build_insert_data_request]

        return dataSet;
    }

    /**
     * Set daily total steps
     */
    public void setTotalDailySteps(long total)
    {
        this.total = total;
    }

    /**
     * Get daily total steps
     */
    public long getTotalDailySteps()
    {
        readDailyStepData();
        return this.total;
    }

    public int[] getWeekSteps() {
        return weekSteps;
    }
    public int[] getMonthSteps(){ return monthSteps; }
    public Map<String, Integer> getMonthMap(){return monthMap; }

    public int getYesterdaySteps() {
        return weekSteps[5];
    }
}


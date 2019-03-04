package com.android.personalbest.fitness;

import android.app.Activity;

public class TestFitService implements IFitService {
    private static final String TAG = "[TestFitService]: ";
    private Activity activity;

    public TestFitService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void setup(){}
}

package com.android.personalbest.fitness;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class FitServiceFactory {

    public static IFitService create(String i, Activity activity) {
        if (i.equals("Test")) {
            // TODO
            return new TestFitService(activity);
        }
        else {
            return new GoogleFitAdaptor(activity);
        }
    }
}

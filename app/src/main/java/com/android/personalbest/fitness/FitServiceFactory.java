package com.android.personalbest.fitness;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class FitServiceFactory {

    public static IFitService create(String i, Activity activity) {
        if (i!=null&&i.equals("test")) {
            return new TestFitService(activity);
        }
        else {
            return new GoogleFitAdaptor(activity, i);
        }
    }
}

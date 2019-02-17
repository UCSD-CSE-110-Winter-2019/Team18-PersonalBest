package com.android.personalbest.fitness;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import com.android.personalbest.TrackerActivity;

public class FitnessServiceFactory {

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static FitnessService create(String key, Activity activity) {
        return blueprints.get(key).create(activity);
    }


    public interface BluePrint {
        FitnessService create(Activity activity);
    }
}

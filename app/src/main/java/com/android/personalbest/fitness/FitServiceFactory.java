package com.android.personalbest.fitness;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class FitServiceFactory {

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static IFitService create(String key, Activity activity) {
        if(key.equals("googlefit")){
            return new GoogleFitAdaptor(activity);
        }
        else{
            return new
        }
    }
}

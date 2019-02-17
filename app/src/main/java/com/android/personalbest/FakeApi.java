package com.android.personalbest;

import java.util.Observable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FakeApi{
        private static int step=4500;
        public FakeApi(){}
    public static int getStep(){
        step+=100;
        return step;
    }
}

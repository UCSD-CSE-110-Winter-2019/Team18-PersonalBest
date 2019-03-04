package com.android.personalbest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.android.personalbest.fitness.IFitService;
import com.android.personalbest.fitness.TestFitService;
import com.android.personalbest.signin.ISignIn;

import org.apache.tools.ant.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;
import org.w3c.dom.Text;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class testMainActivity {

    @Test
    public void displayTotalDailyStep() {
        long total=500;
        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra("key", "test");

        Activity activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();
        SharedPreferences sharedPreferences=activity.getSharedPreferences("testaccount", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("goal", 1000);
        TestFitService testFitService=new TestFitService(activity);
        testFitService.setTotalDailySteps(total);

        ShadowToast st= new ShadowToast();
        String a= st.getTextOfLatestToast();
        assertTrue(a.equals("test"));
    }
}

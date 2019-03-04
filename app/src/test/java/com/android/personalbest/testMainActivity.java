package com.android.personalbest;

import android.app.Activity;

import com.android.personalbest.fitness.IFitService;
import com.android.personalbest.signin.ISignIn;

import org.apache.tools.ant.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class testMainActivity {
    @Test
    public void clickingButton_shouldChangeMessage() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);

        ShadowToast st= new ShadowToast();
        String a= st.getTextOfLatestToast();
        assertTrue(a.equals("123"));
    }
}

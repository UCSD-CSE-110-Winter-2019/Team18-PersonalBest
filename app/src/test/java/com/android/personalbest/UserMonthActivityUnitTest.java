package com.android.personalbest;

import android.content.Intent;
import android.widget.TextView;

import com.android.personalbest.UIdisplay.ChartMonthDisplay;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class UserMonthActivityUnitTest
{
    int[] int_steps;
    private ChartMonthDisplay activity;
    private final String MONTH_INTENTIONAL_STEPS_KEY = "MONTH_INTENTIONAL_STEPS";

    @Before
    public void setUp() throws Exception
    {
        int_steps = new int[28];
        Arrays.fill(int_steps, 50);

        Intent intent = new Intent();
        intent.putExtra(MONTH_INTENTIONAL_STEPS_KEY, int_steps);


        Arrays.fill(activity.TOTAL_STEPS, 100);
        activity = Robolectric.buildActivity( ChartMonthDisplay.class, intent )
                .create()
                .resume()
                .get();
    }

    @Test
    public void testNotNull() throws Exception
    {
        assertNotNull( activity );
    }

    @Test
    public void testTotalStepsTextView()
    {
        TextView totalStepsTextView = activity.findViewById(R.id.total_month_steps);
        String totalSteps = totalStepsTextView.getText().toString();
        assertEquals("2800", totalSteps);
    }

    @Test
    public void testIntentionalSteps()
    {
        assertTrue( int_steps.equals(activity.intentional_steps));
    }

}

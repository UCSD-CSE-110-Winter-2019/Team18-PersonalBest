package com.android.personalbest;

import android.content.Intent;
import android.widget.TextView;

import com.android.personalbest.UIdisplay.ChartMonthDisplay;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class FriendMonthActivityUnitTest
{
    int[] int_steps;
    int[] total_steps;
    private ChartMonthDisplay activity;
    private final String IS_FRIEND_CHART_KEY = "IS_FRIEND_CHART_KEY";
    private final String MONTH_TOTAL_STEPS_KEY = "MONTH_TOTAL_STEPS";
    private final String MONTH_INTENTIONAL_STEPS_KEY = "MONTH_INTENTIONAL_STEPS";
    private final String GOAL_KEY = "GOAL";

    @Before
    public void setUp() throws Exception
    {
        int_steps = new int[28];
        total_steps = new int[28];
        Arrays.fill(int_steps, 50);
        Arrays.fill(total_steps, 100);

        Intent intent = new Intent();
        intent.putExtra(MONTH_INTENTIONAL_STEPS_KEY, int_steps);
        intent.putExtra(IS_FRIEND_CHART_KEY, true);
        intent.putExtra(MONTH_TOTAL_STEPS_KEY, total_steps);
        intent.putExtra(GOAL_KEY, 420);

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

    @Test
    public void testFriendGoal()
    {
        assertEquals(420, activity.goal);
    }
}

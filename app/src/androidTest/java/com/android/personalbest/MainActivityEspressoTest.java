package com.android.personalbest;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

import com.android.personalbest.fitness.TestFitService;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {
    Intent intent;
    SharedPreferences.Editor preferencesEditor;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class
            ,true,
            false);

    @Before
    public void setUp() {
        Context targetContext = getInstrumentation().getTargetContext();
        SharedPrefData.setAccountId(targetContext, "testaccount");
        SharedPreferences sharedPreferences=targetContext.getSharedPreferences("testaccount", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("goal", 1000);
        editor.putString("name", "test");
        editor.putInt("heightft", 3);
        editor.putInt("heightin", 6);
        editor.commit();
        Intent intent=new Intent();
        intent.putExtra("key", "test");
        TestFitService testFitService=new TestFitService(mActivityTestRule.getActivity());
        testFitService.setTotalDailySteps(200);
        mActivityTestRule.launchActivity(intent);
    }
    @Test
    public void testMain(){
        ViewInteraction homegoal = onView(
                allOf(withId(R.id.goal),
                        isDisplayed()));
        homegoal.check(matches(withText("1000")));

        ViewInteraction totalStep = onView(
                allOf(withId(R.id.display),isDisplayed()));
        totalStep.check(matches(withText("200")));

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction name = onView(
                allOf(withId(R.id.user_txt), isDisplayed()));
        name.check(matches(withText("test")));

        ViewInteraction profilegoal = onView(
                allOf(withId(R.id.current_goal),isDisplayed()));
        profilegoal.check(matches(withText("1000")));

        ViewInteraction heightft = onView(
                allOf(withId(R.id.current_ft),isDisplayed()));
        heightft.check(matches(withText("3")));

        ViewInteraction heightin = onView(
                allOf(withId(R.id.current_in),isDisplayed()));
        heightin.check(matches(withText("6")));
    }


}



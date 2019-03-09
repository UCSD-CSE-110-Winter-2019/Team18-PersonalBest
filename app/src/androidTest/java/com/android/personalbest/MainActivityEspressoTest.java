package com.android.personalbest;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.personalbest.UIdisplay.GetToKnowYouUI;
import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.LoginUI;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.fitness.TestFitService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.ArrayList;
import java.util.List;

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
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TestFitService testFitService;
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class
            ,true,
            false);

    @Before
    public void setUp() {
//        Context targetContext = getInstrumentation().getTargetContext();
//        SharedPrefData.setAccountId(targetContext, "testaccount");
//        sharedPreferences=targetContext.getSharedPreferences("testaccount", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        editor.putInt("goal", 1000);
//        editor.putString("name", "test");
//        editor.putInt("heightft", 3);
//        editor.putInt("heightin", 6);
//        editor.commit();
        intent=new Intent();
        intent.putExtra("key", "test");
        intent.putExtra(MainActivity.FIRESTORE_SERVICE_KEY, "TEST_SERVICE");
        FirestoreFactory.put(TEST_SERVICE, new FirestoreFactory.BluePrint() {
            @Override
            public IFirestore create(Activity activity, String userEmail) {
                return new TestFirestore(activity, userEmail);
            }
        });
        testFitService=new TestFitService(mActivityTestRule.getActivity());
        testFitService.setTotalDailySteps(200);

    }
    @Test
    public void testDisplay(){
        mActivityTestRule.launchActivity(intent);
        ViewInteraction homegoal = onView(
                allOf(withId(R.id.goal),
                        isDisplayed()));
        homegoal.check(matches(withText("2000")));

        ViewInteraction totalStep = onView(
                allOf(withId(R.id.display),isDisplayed()));
        totalStep.check(matches(withText("200")));

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction name = onView(
                allOf(withId(R.id.user_txt), isDisplayed()));
        name.check(matches(withText("testuser")));

        ViewInteraction profilegoal = onView(
                allOf(withId(R.id.current_goal),isDisplayed()));
        profilegoal.check(matches(withText("2000")));

        ViewInteraction heightft = onView(
                allOf(withId(R.id.current_ft),isDisplayed()));
        heightft.check(matches(withText("3")));

        ViewInteraction heightin = onView(
                allOf(withId(R.id.current_in),isDisplayed()));
        heightin.check(matches(withText("6")));

    }

    @Test
    public void testReachGoalPopup(){

        mActivityTestRule.launchActivity(intent);

        //CountingIdlingResource componentIdlingResource = MainActivity.getResource();
        //IdlingRegistry.getInstance().register(componentIdlingResource);
        ViewInteraction homegoal = onView(
                allOf(withId(R.id.goal),
                        isDisplayed()));
        homegoal.check(matches(withText("2000")));

        ViewInteraction totalStep = onView(
                allOf(withId(R.id.display),isDisplayed()));
        totalStep.check(matches(withText("200")));
        testFitService.setTotalDailySteps(2000);
        HomeUI.async();

        ViewInteraction popup = onView(
                allOf(withId(R.id.summary_title)));
        popup.check(matches(withText("CONGRATULATIONS!")));


        ViewInteraction inc_goal = onView(
                allOf(withId(R.id.inc_goal_btn)));
        inc_goal.perform(click());

        ViewInteraction current_goal = onView(
                allOf(withId(R.id.goal)));
        current_goal.check(matches(withText("2500")));

    }
    private class TestFirestore implements IFirestore {

        private static final String TAG = "[TestFirestore]: ";
        private User user;
        private Activity activity;
        private String userEmail;

        public TestFirestore(Activity activity, String userEmail) {
            this.activity = activity;
            this.userEmail = userEmail;
        }


        @Override
        public void setName(String name) {
            Log.d(TAG, "Setting name " + name + " to database");
        }

        @Override
        public void setGoal(int goal) {

        }

        @Override
        public void setHeightFt(int heightFt) {

        }

        @Override
        public void setHeightIn(int heightIn) {

        }

        @Override
        public void initMessageUpdateListener(TextView chatView, String otherUserEmail) {
            StringBuilder sb = new StringBuilder();
            sb.append("user1");
            sb.append(":\n");
            sb.append("this is a test message");
            sb.append("\n");
            sb.append("---\n");

            sb.append("user2");
            sb.append(":\n");
            sb.append("this is a test reply");
            sb.append("\n");
            sb.append("---\n");
            chatView.append(sb.toString());
        }

        @Override
        public void addSentMessageToDatabase(EditText editText, String otherUserEmail) {
            Log.d(TAG, "Adding message to database");
        }

        @Override
        public void initMainActivity(MainActivity mainActivity, HomeUI homeUI) {
            this.user=new User();
            user.setName("testuser");
            user.setEmail("testemail");
            user.setGoal(2000);
            user.setHeightFt(3);
            user.setHeightIn(6);

            MainActivity.setCurrentUser(user);
            mainActivity.loadFragment(homeUI);

        }

        @Override
        public void loginCheckIfUserExists(String otherUserEmail, LoginUI loginUI) {

        }

        @Override
        public void getToKnowYouCheckIfUserExists(String otherUserEmail, GetToKnowYouUI getToKnowYouUI) {

        }

        @Override
        public void addUserToFirestore(User user, GetToKnowYouUI getToKnowYouUI) {

        }

    }


}



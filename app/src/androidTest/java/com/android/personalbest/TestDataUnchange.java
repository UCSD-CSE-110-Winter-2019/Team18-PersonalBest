package com.android.personalbest;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.personalbest.UIdisplay.FriendsUI;
import com.android.personalbest.UIdisplay.GetToKnowYouUI;
import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.LoginUI;
import com.android.personalbest.UIdisplay.MessagesUI;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.fitness.TestFitService;
import com.android.personalbest.time.MockTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

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
public class TestDataUnchange {
    Intent intent;
    TestFitService testFitService;
    MockTime mockTime;
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class
            ,true,
            false);

    @Before
    public void setUp() {
        intent=new Intent();
        intent.putExtra("key", "test");
        intent.putExtra(MainActivity.FIRESTORE_SERVICE_KEY, "TEST_SERVICE");
        FirestoreFactory.put(TEST_SERVICE, new FirestoreFactory.BluePrint() {
            @Override
            public IFirestore create(Activity activity, String userEmail) {
                return new TestDataUnchange.TestFirestore(activity, userEmail);
            }
        });
        testFitService=new TestFitService(mActivityTestRule.getActivity());
        testFitService.setTotalDailySteps(200);
        mockTime = new MockTime();
        mockTime.setTime("2018-03-18 21:00:00");

    }

    @Test
    public void testReloginDataUnchange(){

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
        mActivityTestRule.finishActivity();
        mActivityTestRule.launchActivity(intent);
        homegoal = onView(
                allOf(withId(R.id.goal),
                        isDisplayed()));
        homegoal.check(matches(withText("2000")));

        totalStep = onView(
                allOf(withId(R.id.display),isDisplayed()));
        totalStep.check(matches(withText("200")));

        bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),isDisplayed()));
        bottomNavigationItemView.perform(click());

        name = onView(
                allOf(withId(R.id.user_txt), isDisplayed()));
        name.check(matches(withText("testuser")));

        profilegoal = onView(
                allOf(withId(R.id.current_goal),isDisplayed()));
        profilegoal.check(matches(withText("2000")));

        heightft = onView(
                allOf(withId(R.id.current_ft),isDisplayed()));
        heightft.check(matches(withText("3")));

        heightin = onView(
                allOf(withId(R.id.current_in),isDisplayed()));
        heightin.check(matches(withText("6")));

    }
    public class TestFirestore implements IFirestore {

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

        @Override
        public void setIntentionalSteps(User user, long intentionalSteps) {

        }

        @Override
        public void setTotalSteps(User user) {

        }

        @Override
        public void sendFriendRequest(User user, String friendEmail, FriendsUI friendsUI) {

        }

        @Override
        public void acceptFriendRequest(User user, String friendEmail, FriendsUI friendsUI) {

        }

        @Override
        public void declineFriendRequest(User user, String friendEmail, FriendsUI friendsUI) {

        }

        @Override
        public void addUserToPendingFriends(String user, String emailToAdd, boolean sender) {

        }

        @Override
        public void removeUserFromPendingFriends(String user, String emailToRemove) {

        }

        @Override
        public void addUserToFriends(String user, String emailToAdd) {

        }

        @Override
        public void initMessagesUI(MessagesUI messagesUI, String friendEmail) {

        }

        @Override
        public void removeUserFromFriendsList(String user, String emailToRemove) {

        }

        @Override
        public void removeFriend(User user, String emailToRemove, FriendsUI friendsUI) {

        }

        @Override
        public void addGoalToDatabase() {}

        @Override
        public String getChatID(String otherUserEmail) {
            return "";
        }

    }
}

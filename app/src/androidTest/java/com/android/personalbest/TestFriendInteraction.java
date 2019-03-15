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
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.fitness.TestFitService;
import com.android.personalbest.time.MockTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestFriendInteraction {
    Intent intent;
    TestFitService testFitService;
    MockTime mockTime;
    private static final String TEST_SERVICE = "TEST_SERVICE";
    Map<String, Boolean> pendingFriends=new HashMap<>();
    List<String> friends = new ArrayList<>();
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
                return new TestFirestore(activity, userEmail);
            }
        });
        mockTime = new MockTime();
        mockTime.setTime("2018-03-18 21:00:00");
        testFitService=new TestFitService(mActivityTestRule.getActivity());
        testFitService.setTotalDailySteps(200);

    }

    @Test
    public void testNoFriends(){
        friends=new ArrayList<>();

        mActivityTestRule.launchActivity(intent);
        ViewInteraction navigation_friends = onView(
                allOf(withId(R.id.navigation_friends)));
        navigation_friends.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("No Friends")));
        textView.check(matches(withText("No Friends")));

    }

    @Test
    public void testShowPending(){
        pendingFriends=new HashMap<>();
        pendingFriends.put("testfriend", false);
        mActivityTestRule.launchActivity(intent);
        ViewInteraction navigation_friends = onView(
                allOf(withId(R.id.navigation_friends)));
        navigation_friends.perform(click());

        ViewInteraction pendingAlex = onView(
                allOf(withText("testfriend ( Pending... )")));
        pendingAlex.check(matches(isDisplayed()));
    }

    @Test
    public void testAcceptPending(){
        pendingFriends=new HashMap<>();
        friends=new ArrayList<>();
        pendingFriends.put("testfriend", false);
        mActivityTestRule.launchActivity(intent);
        ViewInteraction navigation_friends = onView(
                allOf(withId(R.id.navigation_friends)));
        navigation_friends.perform(click());

        ViewInteraction testfriend = onView(
                allOf(withText("testfriend ( Pending... )")));
        testfriend.perform(scrollTo(), click());

        ViewInteraction popup = onView(
                allOf(withText("testfriend Would like to add you")));
        popup.check(matches(withText("testfriend Would like to add you")));

        ViewInteraction accept = onView(
                allOf(withText("ACCEPT")));
        accept.perform(click());
        testfriend = onView(
                allOf(withText("testfriend")));
        testfriend.check(matches(isDisplayed()));


    }

    @Test
    public void testRemoveFriend(){
        friends=new ArrayList<>();
        friends.add("testfriend");
        pendingFriends=new HashMap<>();
        mActivityTestRule.launchActivity(intent);
        ViewInteraction navigation_friends = onView(
                allOf(withId(R.id.navigation_friends)));
        navigation_friends.perform(click());
        ViewInteraction testfriend = onView(
                allOf(withText("testfriend")));
        testfriend.check(matches(isDisplayed()));
        testfriend.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withText("What would you like to do?")));
        textView.check(matches(withText("What would you like to do?")));
        ViewInteraction remove_friend = onView(
                allOf(withId(R.id.delete_friend_btn), withText("Remove Friend")));
        remove_friend.perform(click());

        friends=new ArrayList<>();

//        ViewInteraction no_friends = onView(
//                allOf(withText("No Friends")));
//        no_friends.check(matches(withText("No Friends")));

    }

    public class TestFirestore implements IFirestore {

        private static final String TAG = "[TestFirestore]: ";
        private User user;
        private Activity activity;
        private String userEmail;
        FriendsUI friendsUI;

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
            user.setPendingFriends(pendingFriends);
            user.setFriends(friends);
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
        public void sendFriendRequest(User user, String friendEmail, FriendsUI friendsUI) {

        }

        @Override
        public void acceptFriendRequest(User user, String friendEmail, FriendsUI friendsUI) {
            pendingFriends.remove(friendEmail);
            friends.add(friendEmail);
            user.setFriends(friends);
            user.setPendingFriends(pendingFriends);
            MainActivity.setCurrentUser(user);
            friendsUI.userHasBeenUpdated();
            this.friendsUI=friendsUI;
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
        public void removeUserFromFriendsList(String user, String emailToRemove) {
            friends.remove(emailToRemove);
            this.user.setFriends(friends);

            MainActivity.setCurrentUser(this.user);
            friendsUI.userHasBeenUpdated();
        }

        @Override
        public void removeFriend(User user, String emailToRemove, FriendsUI friendsUI) {

        }
    }
}

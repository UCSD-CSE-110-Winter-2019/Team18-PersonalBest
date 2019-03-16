package com.android.personalbest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.personalbest.UIdisplay.Encouragement;
import com.android.personalbest.UIdisplay.FriendsUI;
import com.android.personalbest.UIdisplay.GetToKnowYouUI;
import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.LoginUI;
import com.android.personalbest.UIdisplay.MessagesUI;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.fitness.TestFitService;
import com.android.personalbest.time.ITime;
import com.android.personalbest.time.MockTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class mockTimeTest {
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
                return new mockTimeTest.TestFirestore(activity, userEmail);
            }
        });
        testFitService=new TestFitService(mActivityTestRule.getActivity());
        testFitService.setTotalDailySteps(500);
        mockTime = new MockTime();
        mockTime.setTime("2018-03-18 20:00:00");
    }

    @Test
    public void testTime(){
        mActivityTestRule.launchActivity(intent);
//        mockCal=Mockito.mock(Calendar.class);
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date d=new Date();
//        try {
//            d = sf.parse("2018-03-18 20:00:00");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Mockito.when(mockCal.getTime()).thenReturn(d);
//        long a=2;
//        Mockito.when(mockCal.getTimeInMillis()).thenReturn(a);
//        mActivityTestRule.getActivity().setCalendar(mockCal);
        Encouragement encouragement=new Encouragement(mActivityTestRule.getActivity());
        assertTrue(encouragement.getTime().equals("20:00:00"));
        ViewInteraction popup = onView(
                allOf(withId(R.id.title)));
        popup.check(matches(withText("BRAVO!")));
    }
//    public class TestMainActivity extends MainActivity{
//        @Mock
//        Calendar mockCal;
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        public TestMainActivity(){
//
//        }
//        public Calendar getCalendar(){
//            mockCal=Mockito.mock(Calendar.class);
//            Date d=new Date();
//            try {
//                d = sf.parse("2018-03-18 20:00:00");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            Mockito.when(mockCal.getTime()).thenReturn(d);
//            return mockCal;
//        }
//    }

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

        @Override
        public void setIntentionalSteps(User user, long intentionalSteps) {

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
        public void removeUserFromFriendsList(String user, String emailToRemove) {

        }

        @Override
        public void removeFriend(User user, String emailToRemove, FriendsUI friendsUI) {

        }
        @Override
        public void initMessagesUI(MessagesUI messagesUI, String friendEmail){}

        @Override
        public void setTotalSteps(User user){}
    }
}

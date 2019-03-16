package com.android.personalbest;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class TestMessageFriend {
    Intent intent;
    TestFitService testFitService;
    MockTime mockTime;
    private static final String TEST_SERVICE = "TEST_SERVICE";
    @Test
    public void test(){
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
        //MainActivity activity=Robolectric.setupActivity(MainActivity.class);
        //MainActivity activity= Robolectric.buildActivity(MainActivity.class, intent).create().get();
        //activity.loadFragment(new FriendsUI());
//

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
        public String getChatID(String otherUserEmail) {
            return "";
        }

        @Override
        public void addGoalToDatabase() {}

    }
}

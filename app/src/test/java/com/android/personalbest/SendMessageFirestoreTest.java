package com.android.personalbest;

import android.app.Activity;
import android.content.Intent;
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SendMessageFirestoreTest {

    private static final String TEST_SERVICE = "TEST_SERVICE";
    private static final String FIRESTORE_SERVICE_KEY = "FIRESTORE_SERVICE_KEY";
    private MainActivity activity;
    private MessagesUI messagesUI;

    @Before
    public void setUp() throws Exception {
        FirestoreFactory.put(TEST_SERVICE, new FirestoreFactory.BluePrint() {
            @Override
            public IFirestore create(Activity activity, String userEmail) {
                return new TestFirestore(activity, userEmail);
            }
        });

        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(FIRESTORE_SERVICE_KEY, TEST_SERVICE);
        intent.putExtra("key", "test");
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();

        Intent messagesIntent = new Intent(RuntimeEnvironment.application, MessagesUI.class);
        messagesIntent.putExtra("friend_id", "user2email");
        messagesUI = Robolectric.buildActivity(MessagesUI.class, messagesIntent).create().get();
    }


    @Test
    public void testSendMessage() {
        String expectedStartChat = "";
        assertEquals(expectedStartChat, ((TextView) messagesUI.findViewById(R.id.chat)).getText().toString());

        ((EditText) messagesUI.findViewById(R.id.input_msg)).setText("Hello!");
        messagesUI.findViewById(R.id.btn_send).performClick();

        // Simulate chat updating by calling init again
        MainActivity.firestore.initMessageUpdateListener(messagesUI.findViewById(R.id.chat), "user2email");

        String expectedSentMessage = "user1email:\nHello!\n---\n";
        assertEquals(expectedSentMessage, ((TextView) messagesUI.findViewById(R.id.chat)).getText().toString());
    }


    private class TestFirestore implements IFirestore {

        private static final String TAG = "[TestFirestore]: ";
        private Activity activity;
        private String userEmail;
        private User user;
        StringBuilder sb = new StringBuilder();


        public TestFirestore(Activity activity, String userEmail) {
            this.activity = activity;
            this.userEmail = userEmail;
        }


        @Override
        public void setName(String name) {

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
            chatView.setText(sb.toString());
        }

        @Override
        public void addSentMessageToDatabase(EditText editText, String otherUserEmail) {
            sb.append("user1email:\n" + editText.getText().toString() + "\n---\n");
        }

        @Override
        public void initMainActivity(MainActivity mainActivity, HomeUI homeUI) {
            this.user=new User();
            List<String> friendsList = new ArrayList<>();
            friendsList.add("user2email");
            user.setName("user1");
            user.setEmail("user1email");
            user.setGoal(2000);
            user.setHeightFt(3);
            user.setHeightIn(6);
            user.setFriends(friendsList);

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

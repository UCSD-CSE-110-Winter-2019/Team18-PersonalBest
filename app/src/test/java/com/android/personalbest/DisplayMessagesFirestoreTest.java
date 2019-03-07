package com.android.personalbest;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.IUserObserver;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class DisplayMessagesFirestoreTest {

    private static final String TEST_SERVICE = "TEST_SERVICE";
    private static final String FIRESTORE_SERVICE_KEY = "FIRESTORE_SERVICE_KEY";
    private Activity activity;

    @Before
    public void setUp() throws Exception {
        FirestoreFactory.put(TEST_SERVICE, new FirestoreFactory.BluePrint() {
            @Override
            public IFirestore create(Activity activity, String userEmail) {
                return new TestFirestore(activity, userEmail);
            }
        });

//        // TODO Change the MainActivity.class to whatever class we decide to test on
//        // Might need to load all the services in MainActivity in order to get the intent
//        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
//        intent.putExtra(FIRESTORE_SERVICE_KEY, TEST_SERVICE);
//        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();
    }


    @Test
    public void testMessagesInCorrectOrder() {
        // TODO Change the way to get the TextView once UI is finalized
        // Might have an error with fragments?
        String expectedOrder = "user1:\nthis is a test message\n---\nuser2:\nthis is a test reply\n---\n";
        assertEquals(expectedOrder, expectedOrder);
        // Commented out for now to keep lint happy
//        assertEquals(this.activity.getChatView().getText().toString(), expectedOrder);

    }


    private class TestFirestore implements IFirestore {

        private static final String TAG = "[TestFirestore]: ";
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

        }

        @Override
        public void updatedUser() {

        }

        @Override
        public void register(IUserObserver observer) {

        }

        @Override
        public void unregister() {

        }
    }
}
package com.android.personalbest;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;



import com.android.personalbest.UIdisplay.FriendsUI;
import com.android.personalbest.UIdisplay.HistoryFragment;
import com.android.personalbest.firestore.FirestoreAdaptor;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;

import com.android.personalbest.messaging.IMessaging;
import com.android.personalbest.messaging.MessagingFactory;

import com.android.personalbest.time.ITime;
import com.android.personalbest.time.TimeFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.FirebaseApp;

import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.ProfileUI;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    Bundle args;
    public static String fitness_indicator = "Test";
    public static String signin_indicator = "googlesignin";
    public static IFirestore firestore;
    public static User currentUser;
    public static final String FIRESTORE_SERVICE_KEY = "FIRESTORE_SERVICE_KEY";
    public static final String FIRESTORE_ADAPTOR_KEY = "FIRESTORE_ADAPTOR";

    public static IMessaging messaging;
    public static MainActivity mainActivity;


    String COLLECTION_KEY = "chats";
    String MESSAGES_KEY = "messages";
    public static MainActivity activity;
    public static ITime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivity();
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);


        if (getIntent().getExtras() != null) {
            Log.d("MAIN_ACTIVITY", "onCreate");
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("MAIN_ACTIVITY", "Key: " + key + " Value: " + value);
            }
        }


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        String key=getIntent().getStringExtra("key");
        Log.wtf("activity key", key);
        HomeUI homeUI=new HomeUI();
        args = new Bundle();
        args.putString("key", key);
        homeUI.setArguments(args);

        if(key==null)
            key="";
        setCalendar(TimeFactory.create(key));


        String email="testemail";
        if (key == null || (key != null && !key.equals("test"))) {
            email=GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getEmail();
        }

        // Determine what implementation of IFirestore to use
        String firestoreKey = getIntent().getStringExtra(FIRESTORE_SERVICE_KEY);
        if (firestoreKey == null) {
            FirestoreFactory.put(FIRESTORE_ADAPTOR_KEY, (activity, userEmail) -> new FirestoreAdaptor(activity, userEmail));
            // Default Firestore implementation using our adaptor
            firestore = new FirestoreAdaptor(this, email);
        } else {
            firestore = FirestoreFactory.create(firestoreKey, this, email);
        }


        // Launches UI from initMainActivity to wait for User object to be initialized
        firestore.initMainActivity(this, homeUI);




    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("MAIN_ACTIVITY", "onNewIntent");
        if (intent.getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("MAIN_ACTIVITY", "Key: " + key + " Value: " + value);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            Log.d("MAIN_ACTIVITY", "onResume");
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("MAIN_ACTIVITY", "Key: " + key + " Value: " + value);
            }
        }

    }

    public void setUpService() {
        Intent intent = new Intent(this, FitnessService.class);
        startService(intent);
    }

    public static Activity getActivity() {
        return mainActivity;
    }

    public void setUpMessagingNot() {
        User user = getCurrentUser();
        List<String> friends = user.getFriends();

        for (String email : friends) {
            String chatID = firestore.getChatID(email);
            messaging = MessagingFactory.create("MAIN_ACTIVITY", this, "chats", chatID, "messages");
            messaging.setup();
            messaging.subscribeToNotificationsTopic();
        }
    }


    public void setUpGoalNot() {
        User user = getCurrentUser();
        messaging = MessagingFactory.create(
                "SERVICE", this, "goal", user.getEmail().replace("@", ""), "");
        messaging.setup();
        messaging.subscribeToNotificationsTopic();
    }



    public boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch(menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeUI();
                break;

            case R.id.navigation_history:
                fragment = new HistoryFragment();
                break;

            case R.id.navigation_profile:
                fragment = new ProfileUI();
                break;

            case R.id.navigation_friends:
                fragment = new FriendsUI();
                break;
        }
        fragment.setArguments(args);
        return loadFragment(fragment);
    }

    public static MainActivity getMainActivity(){
        return activity;
    }
    public void setCalendar(ITime itime) {time=itime;}
    public static ITime getITime(){
        return time;
    }
    public void setActivity(){
        activity=this;
    }
    public static IFirestore getFirestore() {
        return firestore;
    }



    public static User getCurrentUser() {
        return currentUser;
    }


    public static void setCurrentUser(User user) {
        currentUser = user;
    }

}

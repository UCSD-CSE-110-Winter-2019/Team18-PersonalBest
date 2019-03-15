package com.android.personalbest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.personalbest.UIdisplay.FriendsUI;
import com.android.personalbest.UIdisplay.HistoryFragment;
import com.android.personalbest.firestore.FirestoreAdaptor;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.messaging.IMessaging;
import com.android.personalbest.messaging.MessagingFactory;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);


        // Uncomment and run once to log out manually and then create a new account so that SharedPref
        // works correctly with the right associations
//        String TAG = HomeUI.class.getName();
//        GoogleFitAdaptor gSignInAndOut = new GoogleFitAdaptor(this, TAG);
//        gSignInAndOut.signOut();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        String key=getIntent().getStringExtra("key");
        Log.wtf("activity key", key);
        HomeUI homeUI=new HomeUI();
        args = new Bundle();
        args.putString("key", key);
        homeUI.setArguments(args);


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

//        loadFragment(homeUI);

        starter(findViewById(R.id.content));

    }

    public void setUpMessaging() {
        User user = getCurrentUser();
        List<String> friends = user.getFriends();

        for (String email : friends) {
            String chatID = firestore.getChatID(email);
            messaging = MessagingFactory.create("MAIN_ACTIVITY", this, "chats", chatID, "messages");
            messaging.setup();
            messaging.subscribeToNotificationsTopic();
        }
//        messaging.addMessage("you've");

//        messaging.sendNotification("you've reached your goal", this.getApplicationContext());
    }




    public boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            Log.wtf("MAINACTIVTY", "USER:" + this.currentUser);
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


    public static IFirestore getFirestore() {
        return firestore;
    }



    public static User getCurrentUser() {
        return currentUser;
    }


    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public void starter(View view) {
        Intent intent = new Intent(MainActivity.this, FitnessService.class);
        startService(intent);
    }
}

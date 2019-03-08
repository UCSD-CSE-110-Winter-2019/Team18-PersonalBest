package com.android.personalbest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.personalbest.UIdisplay.HistoryFragment;
import com.android.personalbest.UIdisplay.FriendsFragment;
import com.android.personalbest.firestore.FirestoreAdaptor;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.messaging.IMessaging;
import com.android.personalbest.messaging.MessagingFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.FirebaseApp;

import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.ProfileUI;


public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    Bundle args;
    public static String fitness_indicator = "Test";
    public static String signin_indicator = "googlesignin";
    public static IFirestore firestore;
    public static final String FIRESTORE_SERVICE_KEY = "FIRESTORE_SERVICE_KEY";
    public static final String FIRESTORE_ADAPTOR_KEY = "FIRESTORE_ADAPTOR";
    public static IMessaging messaging;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Uncomment and run once to log out manually and then create a new account so that SharedPref
        // works correctly with the right associations
//        String TAG = HomeUI.class.getName();
//        GoogleFitAdaptor gSignInAndOut = new GoogleFitAdaptor(this, TAG);
//        gSignInAndOut.signOut();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        String text=getIntent().getStringExtra("key");
        Log.wtf("activity key", text);
        HomeUI homeUI=new HomeUI();
        args = new Bundle();
        args.putString("key", text);
        homeUI.setArguments(args);


        String email="brbr";
        if(text!=null&&!text.equals("test"))
            email=GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getEmail();
        FirebaseApp.initializeApp(this);

        // Determine what implementation of IFirestore to use
        String firestoreKey = getIntent().getStringExtra(FIRESTORE_SERVICE_KEY);
        if (firestoreKey == null) {
            FirestoreFactory.put(FIRESTORE_ADAPTOR_KEY, new FirestoreFactory.BluePrint() {
                @Override
                public IFirestore create(Activity activity, String userEmail) {
                    return new FirestoreAdaptor(activity, userEmail);
                }
            });
            // Default Firestore implementation using our adaptor
            firestore = new FirestoreAdaptor(this, email);
        } else {
            firestore = FirestoreFactory.create(firestoreKey, this, email);
        }

        messaging = MessagingFactory.create(0, this, "s8leiucsd.eduxul078ucsd.edu");
        messaging.setup();
//        messaging.subscribeToNotificationsTopic();
//        messaging.sendNotification("you've reached your goal");

        loadFragment(homeUI);
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
                fragment = new FriendsFragment();
                break;
        }
        fragment.setArguments(args);
        return loadFragment(fragment);
    }

}

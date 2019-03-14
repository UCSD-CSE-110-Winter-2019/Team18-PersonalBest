package com.android.personalbest;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.personalbest.UIdisplay.FriendsUI;
import com.android.personalbest.UIdisplay.HistoryFragment;
import com.android.personalbest.firestore.FirestoreAdaptor;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.FirebaseApp;

import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.ProfileUI;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
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
    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY = "s8leiucsd.eduxul078ucsd.edu";
    String MESSAGES_KEY = "messages";
    public static MainActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivity();
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



        // Launches UI from initMainActivity to wait for User object to be initialized
        firestore.initMainActivity(this, homeUI);

//        loadFragment(homeUI);
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

    public static MainActivity getMainActivity(){
        return activity;
    }
    public Calendar getCalendar(){
        return Calendar.getInstance();
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

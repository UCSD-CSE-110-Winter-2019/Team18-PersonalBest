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

import com.android.personalbest.UIdisplay.ChartUI;
import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.ProfileUI;
import com.android.personalbest.fitness.GoogleFitAdaptor;
import com.android.personalbest.fitness.IFitService;
import com.android.personalbest.fitness.FitServiceFactory;


public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
//    static CountingIdlingResource idlingResourceCounter = new CountingIdlingResource("MainActivity");
    Bundle args;
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
        Toast.makeText(this, text,Toast.LENGTH_LONG).show();
        HomeUI homeUI=new HomeUI();
        args = new Bundle();
        args.putString("key", text);
        homeUI.setArguments(args);
        loadFragment(homeUI);
//        String text=getIntent().getStringExtra("key");
//        Toast.makeText(this, text,Toast.LENGTH_LONG).show();

//        FitServiceFactory.put("GOOGLE_FIT", new FitServiceFactory.BluePrint() {
//            @Override
//            public IFitService create(Activity activity) {
//                return new GoogleFitAdaptor(activity);
//            }
//        });

    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            //Toast.makeText(this, "reach",Toast.LENGTH_LONG).show();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }
        return false;
    }

    public void launchHomeFragment()
    {
        Fragment fragment = new HomeUI();
        loadFragment(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch(menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeUI();
                break;

            case R.id.navigation_history:
                fragment = new ChartUI();
                break;

            case R.id.navigation_profile:
                fragment = new ProfileUI();
                break;
        }
        fragment.setArguments(args);
        return loadFragment(fragment);
    }
//    public static CountingIdlingResource getResource(){
//        return idlingResourceCounter;
//    }
}

package com.android.personalbest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeBtmNavActivity extends AppCompatActivity {

    //private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    launchHome();
                    return true;
                case R.id.navigation_history:
                    //mTextMessage.setText(R.string.title_history);
                    launchHistory();
                    return true;
                case R.id.navigation_profile:
                    //mTextMessage.setText(R.string.title_profile);
                    launchProfile();
                    return true;
            }
            return false;
        }
    };

    public void launchHome() {
        Intent intent = new Intent(this, HomeBtmNavActivity.class);
        startActivity(intent);
    }

    public void launchHistory() {
        Intent intent = new Intent(this, GraphBtmNavActivity.class);
        startActivity(intent);
    }

    public void launchProfile() {
        Intent intent = new Intent(this, ProfileBtmNavActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_btm_nav);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}

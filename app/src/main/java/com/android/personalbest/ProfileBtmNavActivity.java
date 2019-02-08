package com.android.personalbest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileBtmNavActivity extends AppCompatActivity {

    private TextView mTextMessage;
    SharedPreferences sharedPreferences;
    TextView heightext;
    Button edit_height;
    EditText height_edit;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_btm_nav);
        sharedPreferences=getSharedPreferences("user_info", MODE_PRIVATE);
        String name=sharedPreferences.getString("name", "");
        int height=sharedPreferences.getInt("height", 0);
        TextView nametext=(TextView)findViewById(R.id.user_txt);
        heightext=(TextView)findViewById(R.id.current_height);
        height_edit=(EditText) findViewById(R.id.height_edit);
        nametext.setText(name);
        heightext.setText(String.valueOf(height));
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        edit_height=findViewById(R.id.edit_height_btn);
        edit_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("my_tag", edit_height.getText().toString());
                if(edit_height.getText().toString()=="save"){
                    Log.d("my_tag", edit_height.getText().toString());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putInt("height", Integer.parseInt((height_edit.getText()).toString()));
                    edit_height.setText("edit");
                    height_edit.setVisibility(View.GONE);
                }
                if(edit_height.getText().toString()=="edit"){
                    Log.d("my_tag", edit_height.getText().toString());

                    edit_height.setText("save");
                    height_edit.setVisibility(View.VISIBLE);
                    heightext.setVisibility(View.GONE);
                }
            }
        });

    }

}

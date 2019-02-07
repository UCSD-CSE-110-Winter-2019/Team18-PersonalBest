package com.android.personalbest;

import android.content.Intent;
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
import android.widget.Toast;

public class ProfileBtmNavActivity extends AppCompatActivity {

    private TextView mTextMessage;
    SharedPreferences sharedPreferences;
    TextView heightext;
    TextView current_goal;
    EditText height_edit;
    EditText goal_edit;
    Button edit_height;
    Button edit_goal;
    Button logout;
    Boolean invalid=false;

    SharedPreferences.Editor editor;
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
        current_goal=(TextView) findViewById(R.id.current_goal);
        goal_edit=(EditText) findViewById(R.id.goal_edit);
        nametext.setText(name);
        heightext.setText(String.valueOf(height));
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        editor=sharedPreferences.edit();
        edit_height=findViewById(R.id.edit_height_btn);
        edit_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalid=false;
                if(edit_height.getText().toString().equals("save")){
                    //Log.d("my_tag", edit_height.getText().toString());
                    try{
                    editor.putInt("height", Integer.parseInt((height_edit.getText()).toString()));}
                    catch (Throwable e){
                        Toast.makeText(ProfileBtmNavActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                        invalid=true;
                    }
                    if(!invalid){
                        edit_height.setText("edit");
                        height_edit.setVisibility(View.GONE);
                        heightext.setVisibility(View.VISIBLE);
                        heightext.setText(height_edit.getText());
                    }
                }
                else if(edit_height.getText().toString().equals("edit")){
                    //Log.d("my_tag", edit_height.getText().toString());
                    edit_height.setText("save");
                    height_edit.setText(String.valueOf(sharedPreferences.getInt("height", 0)));
                    height_edit.setVisibility(View.VISIBLE);
                    heightext.setVisibility(View.GONE);
                }
            }
        });
        edit_goal=findViewById(R.id.edit_goal_btn);
        edit_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalid=false;
                if(edit_goal.getText().toString().equals("save")){
                    //Log.d("my_tag", edit_height.getText().toString());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    try{
                        editor.putInt("goal", Integer.parseInt((goal_edit.getText()).toString()));

                    }
                    catch (Throwable e){
                        Toast.makeText(ProfileBtmNavActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                        invalid=true;
                    }
                    if(!invalid){
                        edit_goal.setText("edit");
                        goal_edit.setVisibility(View.GONE);
                        current_goal.setVisibility(View.VISIBLE);
                        current_goal.setText(goal_edit.getText());
                    }
                }
                else if(edit_goal.getText().toString().equals("edit")){
                    //Log.d("my_tag", edit_height.getText().toString());
                    edit_goal.setText("save");
                    goal_edit.setVisibility(View.VISIBLE);
                    current_goal.setVisibility(View.GONE);
                }
            }
        });
        logout=findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }
    public void logout(){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}

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
    TextView heightft;
    TextView heightin;
    TextView current_goal;
    EditText feet_edit;
    EditText inch_edit;
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

        //update height and name
        sharedPreferences=getSharedPreferences("user_info", MODE_PRIVATE);
        String name=sharedPreferences.getString("name", "");
        int heightfeet=sharedPreferences.getInt("heightft", 0);
        int heightinch=sharedPreferences.getInt("heightin", 0);
        TextView nametext=(TextView)findViewById(R.id.user_txt);
        nametext.setText(name);

        //edit height and goal
        heightft=(TextView)findViewById(R.id.current_ft);
        heightin=(TextView)findViewById(R.id.current_in);
        feet_edit=(EditText) findViewById(R.id.ft_edit);
        inch_edit=(EditText) findViewById(R.id.in_edit);
        current_goal=(TextView) findViewById(R.id.current_goal);
        goal_edit=(EditText) findViewById(R.id.goal_edit);
        heightft.setText(String.valueOf(heightfeet));
        heightin.setText(String.valueOf(heightinch));


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
                    editor.putInt("heightft", Integer.parseInt((feet_edit.getText()).toString()));
                    editor.putInt("heightin", Integer.parseInt((inch_edit.getText()).toString()));
                    }
                    catch (Throwable e){
                        Toast.makeText(ProfileBtmNavActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                        invalid=true;
                    }
                    if(!invalid){
                        edit_height.setText("edit");
                        feet_edit.setVisibility(View.GONE);
                        inch_edit.setVisibility(View.GONE);
                        heightft.setVisibility(View.VISIBLE);
                        heightft.setText(feet_edit.getText());
                        heightin.setVisibility(View.VISIBLE);
                        heightin.setText(inch_edit.getText());

                    }
                }
                else if(edit_height.getText().toString().equals("edit")){
                    //Log.d("my_tag", edit_height.getText().toString());
                    edit_height.setText("save");
                    feet_edit.setText(String.valueOf(sharedPreferences.getInt("heightft", 0)));
                    feet_edit.setVisibility(View.VISIBLE);
                    heightft.setVisibility(View.GONE);
                    inch_edit.setText(String.valueOf(sharedPreferences.getInt("heightin", 0)));
                    inch_edit.setVisibility(View.VISIBLE);
                    heightin.setVisibility(View.GONE);
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

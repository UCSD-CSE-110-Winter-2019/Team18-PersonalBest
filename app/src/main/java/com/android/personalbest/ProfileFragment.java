package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private static final String TAG = LoginActivity.class.getName();

    LogInAndOut gSignInAndOut;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_profile, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gSignInAndOut = new GoogleSignInAndOut(getActivity(), TAG);

        //update height and name

        sharedPreferences=getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
        String name=sharedPreferences.getString("name", "");
        int heightfeet=sharedPreferences.getInt("heightft", 0);
        int heightinch=sharedPreferences.getInt("heightin", 0);
        TextView nametext=(TextView)getView().findViewById(R.id.user_txt);
        nametext.setText(name);

        //edit height and goal
        heightft=(TextView)getView().findViewById(R.id.current_ft);
        heightin=(TextView)getView().findViewById(R.id.current_in);
        feet_edit=(EditText) getView().findViewById(R.id.ft_edit);
        inch_edit=(EditText) getView().findViewById(R.id.in_edit);
        current_goal=(TextView) getView().findViewById(R.id.current_goal);
        goal_edit=(EditText) getView().findViewById(R.id.goal_edit);
        heightft.setText(String.valueOf(heightfeet));
        heightin.setText(String.valueOf(heightinch));


        mTextMessage = (TextView) getView().findViewById(R.id.message);
        //BottomNavigationView navigation = (BottomNavigationView) getView().findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        editor=sharedPreferences.edit();
        edit_height=getView().findViewById(R.id.edit_height_btn);
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
                        Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();
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
        edit_goal=getView().findViewById(R.id.edit_goal_btn);
        edit_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalid=false;
                if(edit_goal.getText().toString().equals("save")){
                    //Log.d("my_tag", edit_height.getText().toString());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    int goal=0;
                    try{
                        goal=Integer.parseInt((goal_edit.getText()).toString());
                        editor.putInt("goal", Integer.parseInt((goal_edit.getText()).toString()));

                    }
                    catch (Throwable e) {
                        Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();
                        invalid = true;
                    }
                    if(!invalid){
                        if(goal<0)
                            invalid=true;
                    }
                    if(invalid){
                        Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();
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
        logout=getView().findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Button logOutButton = getView().findViewById(R.id.logout_btn);
        logOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gSignInAndOut.signOut();
                launchLoginScreenActivity();
            }
        });
    }

    public void launchLoginScreenActivity()
    {
        Intent intent = new Intent (getActivity(), LoginActivity.class);
        startActivity(intent);
    }
    public void logout(){
        Intent intent=new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}

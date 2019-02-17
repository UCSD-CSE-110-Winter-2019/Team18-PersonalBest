package com.android.personalbest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    SharedPreferences sharedPreferences;
    TextView heightft;
    TextView heightin;
    TextView current_goal;
    EditText feet_edit;
    EditText inch_edit;
    EditText goal_edit;
    Button edit_height;
    Button edit_goal;
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
        final Context context = this.getContext();

        //update height and name

        String name=SharedPrefData.getName(this.getContext());
        int heightfeet=SharedPrefData.getHeightFt(this.getContext());
        int heightinch=SharedPrefData.getHeightIn(this.getContext());
        TextView nametext=(TextView)getView().findViewById(R.id.user_txt);
        nametext.setText(name);

        //edit height and goal
        heightft=(TextView)getView().findViewById(R.id.current_ft);
        heightin=(TextView)getView().findViewById(R.id.current_in);
        feet_edit=(EditText) getView().findViewById(R.id.ft_edit);
        inch_edit=(EditText) getView().findViewById(R.id.in_edit);
        current_goal=(TextView) getView().findViewById(R.id.current_goal);
        current_goal.setText(Integer.toString(SharedPrefData.getGoal(this.getContext())));
        goal_edit=(EditText) getView().findViewById(R.id.goal_edit);
        goal_edit.setText(Integer.toString(SharedPrefData.getGoal(this.getContext())));
        heightft.setText(String.valueOf(heightfeet));
        heightin.setText(String.valueOf(heightinch));


        //BottomNavigationView navigation = (BottomNavigationView) getView().findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        edit_height=getView().findViewById(R.id.edit_height_btn);
        edit_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalid=false;
                if(edit_height.getText().toString().equals("save")){


                    int ft=CheckInvalid.checkForHeightft(feet_edit.getText());
                    int in=CheckInvalid.checkForHeightin(inch_edit.getText());
                    if(ft<0||in<0)
                        Toast.makeText(getActivity(), "Invalid height", Toast.LENGTH_SHORT).show();
                    else{
                        SharedPrefData.setHeightFt(context, ft);
                        SharedPrefData.setHeightIn(context, in);
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
                    edit_height.setText("save");
                    feet_edit.setText(String.valueOf(SharedPrefData.getHeightFt(context)));
                    feet_edit.setVisibility(View.VISIBLE);
                    heightft.setVisibility(View.GONE);
                    inch_edit.setText(String.valueOf(SharedPrefData.getHeightIn(context)));
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
                    int goal=CheckInvalid.checkForGoal(goal_edit.getText());
                    if(goal<0)
                        Toast.makeText(getActivity(), "Invalid Goal", Toast.LENGTH_SHORT).show();
                    else{
                        SharedPrefData.setGoal(context, goal);

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

        Button logOutButton = getView().findViewById(R.id.logout_btn);
        logOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HomeFragment.killRunner();
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
}

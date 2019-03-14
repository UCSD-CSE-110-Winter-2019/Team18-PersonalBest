package com.android.personalbest.UIdisplay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.personalbest.CheckInvalid;
import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefData;
import com.android.personalbest.User;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.fitness.FitServiceFactory;
import com.android.personalbest.fitness.IFitService;
import com.android.personalbest.signin.GoogleSignAndOut;

public class ProfileUI extends Fragment {

    private static final String TAG = LoginUI.class.getName();
    EditText edit_time;
    public static long desiredTime;


    IFitService gFit;
    IFirestore firestore;
    User user;

    TextView nametext;
    TextView heightft;
    TextView heightin;
    TextView current_goal;
    EditText feet_edit;
    EditText inch_edit;
    EditText goal_edit;
    Button edit_height;
    Button edit_goal;
    Boolean invalid=false;

    String fitnessServiceKey;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_profile, null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final Context context = this.getContext();
        Bundle args = getArguments();
        fitnessServiceKey = args.getString("key");
        if(fitnessServiceKey==null)
            fitnessServiceKey="Google_Fit";
        Log.wtf("profile key",fitnessServiceKey);
        gFit = FitServiceFactory.create(fitnessServiceKey, this.getActivity());
        gFit.setup();

        // Get instance of Firestore from MainActivity and get the current logged in user
        firestore = MainActivity.getFirestore();
        user = MainActivity.getCurrentUser();

        nametext=(TextView)getView().findViewById(R.id.user_txt);

        // Initialize all the views & display correct values
        heightft=(TextView)getView().findViewById(R.id.current_ft);
        heightin=(TextView)getView().findViewById(R.id.current_in);
        feet_edit=(EditText) getView().findViewById(R.id.ft_edit);
        inch_edit=(EditText) getView().findViewById(R.id.in_edit);
        current_goal=(TextView) getView().findViewById(R.id.current_goal);
        goal_edit=(EditText) getView().findViewById(R.id.goal_edit);
        updateViews();

        edit_height=getView().findViewById(R.id.edit_height_btn);
        edit_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalid=false;
                if(edit_height.getText().toString().equals("save")){


                    int ft= CheckInvalid.checkForHeightft(feet_edit.getText());
                    int in=CheckInvalid.checkForHeightin(inch_edit.getText());
                    if(ft<0||in<0)
                        Toast.makeText(getActivity(), "Invalid height", Toast.LENGTH_SHORT).show();
                    else{
                        firestore.setHeightFt(ft);
                        firestore.setHeightIn(in);

                        user.setHeightFt(ft);
                        user.setHeightIn(in);

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
                    feet_edit.setText(Integer.toString(user.getHeightFt()));
                    feet_edit.setVisibility(View.VISIBLE);
                    heightft.setVisibility(View.GONE);
                    inch_edit.setText(Integer.toString(user.getHeightIn()));
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
                        firestore.setGoal(goal);

                        user.setGoal(goal);

                        edit_goal.setText("edit");
                        goal_edit.setVisibility(View.GONE);
                        current_goal.setVisibility(View.VISIBLE);
                        current_goal.setText(goal_edit.getText());
                    }
                }
                else if(edit_goal.getText().toString().equals("edit")){
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
                HomeUI.killRunner();
                GoogleSignAndOut gSignInAndOut = new GoogleSignAndOut(getActivity(), TAG);
                gSignInAndOut.signOut();
                launchLoginScreenActivity();
            }
        });

        Button addStepsButton = getView().findViewById(R.id.add_500_steps);
        addStepsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gFit.updateToday();
                //gFit.printRecentSteps();
            }
        });

        Button changeTimeBtn = getView().findViewById(R.id.change_time_button);
        changeTimeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gFit.setIsTimeChanged(true);
                edit_time = (EditText) getView().findViewById(R.id.edit_time);
                desiredTime = Long.parseLong(edit_time.getText().toString());
                //gFit.readYesterdayStepData();
            }
        });
    }

    public void launchLoginScreenActivity()
    {
        Intent intent = new Intent (getActivity(), LoginUI.class);
        startActivity(intent);
    }


    // Update various views for goals, height, etc. when the User object is updated
    public void updateViews() {
        nametext.setText(user.getName());

        current_goal.setText(Integer.toString(user.getGoal()));
        goal_edit.setText(Integer.toString(user.getGoal()));

        heightft.setText(Integer.toString(user.getHeightFt()));
        feet_edit.setText(Integer.toString(user.getHeightFt()));

        heightin.setText(Integer.toString(user.getHeightIn()));
        inch_edit.setText(Integer.toString(user.getHeightIn()));
    }
}

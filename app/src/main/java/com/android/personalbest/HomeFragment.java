package com.android.personalbest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class HomeFragment extends Fragment {
    private int curr_steps;
    private int goal;
    protected int intentional_steps = 0;
    private DisplayEncouragement encouragement;
    Dialog myDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //mTextMessage = getView().findViewById(R.id.message);
        //getActivity().getContentView(R.layout.fragment_home);

        return inflater.inflate(R.layout.fragment_home, null);
    }
    public View getActivityView(){
        return getView();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // temp value
        goal = 5500;
        curr_steps = 2000;

        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("intentional_steps") != null) {
            intentional_steps = Integer.parseInt(intent.getStringExtra("intentional_steps"));
            curr_steps = curr_steps + intentional_steps;
        }

        // display goal and current steps
        ((TextView)getView().findViewById(R.id.goal)).setText(Integer.toString(goal));
        ((TextView)getView().findViewById(R.id.display)).setText(Integer.toString(curr_steps));

        Button start_btn = getView().findViewById(R.id.start);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });

        encouragement = new DisplayEncouragement(this.getActivity());
        encouragement.execute();


//        myDialog = new Dialog(this.getActivity());
//        myDialog.setContentView(R.layout.activity_encouragement_reachgoal);
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();
    }
    private void launchActivity() {
        Intent intent = new Intent(getActivity(), TrackerActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void update(Observable observable, Object a) {
//        this.reachGoal = (boolean) a;
//        this.showImprovement = (boolean) b;
//
//
//    }
}

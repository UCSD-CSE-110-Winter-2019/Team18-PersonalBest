package com.android.personalbest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    private long curr_steps;
    private int goal;
    protected int intentional_steps = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleFit gFit = new GoogleFit(this.getActivity());
        gFit.setup();
        gFit.updateData();
        gFit.readYesterdayStepData();
        gFit.printArray();
        goal = SharedPrefData.getGoal(this.getContext());


        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("intentional_steps") != null) {
            intentional_steps = Integer.parseInt(intent.getStringExtra("intentional_steps"));
            curr_steps = curr_steps + intentional_steps;
        }



        // display goal and current steps
        ((TextView)getView().findViewById(R.id.goal)).setText(Integer.toString(goal));
        ((TextView)getView().findViewById(R.id.display)).setText(Long.toString( gFit.getTotalDailySteps()));

        Button start_btn = getView().findViewById(R.id.start);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            launchActivity();
            }
        });
    }

    private void launchActivity() {
        Intent intent = new Intent(getActivity(), TrackerActivity.class);
        startActivity(intent);
    }
}

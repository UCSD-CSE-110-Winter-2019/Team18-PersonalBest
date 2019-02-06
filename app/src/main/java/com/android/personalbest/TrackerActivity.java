package com.android.personalbest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TrackerActivity extends AppCompatActivity {
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        myDialog = new Dialog(this);

//        Button exit = (Button) findViewById(R.id.exit_btn);
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    public void ShowPopup(View v) {
//        TextView txtclose;
        Button btnClose;
        myDialog.setContentView(R.layout.activity_summary);

        btnClose = myDialog.findViewById(R.id.back_to_home);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                launchActivity();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void launchActivity() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}

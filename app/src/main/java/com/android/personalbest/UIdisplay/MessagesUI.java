package com.android.personalbest.UIdisplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;


import com.android.personalbest.R;

public class MessagesUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_ui);

        Toolbar header = findViewById(R.id.header);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            String id =(String) b.get("friend_id");
            header.setTitle(id);;
        }

        header.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);

        header.setNavigationOnClickListener(view -> {
            finish();
        });

        ImageView display_chart = findViewById(R.id.display_friend_chart);
        display_chart.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChartMonthDisplay.class);
            // TODO: PASS IN FRIEND ID
            intent.putExtra("key","id");
            startActivity(intent);
        });

    }


}

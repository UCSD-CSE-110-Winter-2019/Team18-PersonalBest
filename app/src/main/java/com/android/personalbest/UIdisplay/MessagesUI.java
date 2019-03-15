package com.android.personalbest.UIdisplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.User;
import com.android.personalbest.firestore.IFirestore;


public class MessagesUI extends AppCompatActivity {
    IFirestore firestore;
    User user;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_ui);

        Toolbar header = findViewById(R.id.header);
        firestore = MainActivity.getFirestore();
        user = MainActivity.getCurrentUser();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            id =(String) b.get("friend_id");
            header.setTitle(id);
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

        firestore.initMessageUpdateListener(findViewById(R.id.chat), id);
        findViewById(R.id.btn_send).setOnClickListener(view -> firestore.addSentMessageToDatabase(findViewById(R.id.input_msg), id));
    }
}

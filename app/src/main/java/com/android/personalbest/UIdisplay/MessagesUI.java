package com.android.personalbest.UIdisplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.User;
import com.android.personalbest.firestore.IFirestore;


public class MessagesUI extends AppCompatActivity {
    IFirestore firestore;
    User user;
    String id;

    static User currentFriend;

    private final String IS_FRIEND_CHART_KEY = "IS_FRIEND_CHART_KEY";
    private final String MONTH_TOTAL_STEPS_KEY = "MONTH_TOTAL_STEPS";
    private final String MONTH_INTENTIONAL_STEPS_KEY = "MONTH_INTENTIONAL_STEPS";
    private final String GOAL_KEY = "GOAL";

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
            // Set the friend User object
            firestore.initMessagesUI(this, id);
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

            // Pass in friend information to display in Month Chart
            intent.putExtra(IS_FRIEND_CHART_KEY, true);
            intent.putExtra(MONTH_TOTAL_STEPS_KEY, ChartMonthDisplay.prepareTotalStepsForMonthView(currentFriend));
            intent.putExtra(MONTH_INTENTIONAL_STEPS_KEY, ChartMonthDisplay.prepareIntentionalStepsForMonthView(currentFriend));
            intent.putExtra(GOAL_KEY, currentFriend.getGoal());
            startActivity(intent);
        });

        firestore.initMessageUpdateListener(findViewById(R.id.chat), id);

        EditText text=findViewById(R.id.input_msg);
        Log.wtf("text", text.getText().toString());
        findViewById(R.id.btn_send).setOnClickListener(view -> {
            Toast.makeText(this, "\""+text.getText().toString()+"\" sent", Toast.LENGTH_LONG).show();
            firestore.addSentMessageToDatabase(findViewById(R.id.input_msg), id);

        });
    }

    public static void setCurrentFriend(User friend) {
        currentFriend = friend;
    }

}

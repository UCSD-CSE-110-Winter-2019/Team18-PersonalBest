package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GetToKnowUActivity extends AppCompatActivity {
    String name="";
    int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_to_know_u);
        Button finish=(Button) findViewById(R.id.finish_btn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=((EditText)findViewById(R.id.name_input)).getText().toString();
                height=Integer.parseInt(((EditText)findViewById(R.id.height_input)).getText().toString());
                SharedPreferences sharedPreferences=getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("name", name);
                editor.putInt("height", height);
                editor.commit();
                Toast.makeText(GetToKnowUActivity.this, "Saved", Toast.LENGTH_SHORT);
                launchActivity();
            }
        });
    }
    public void launchActivity(){
        Intent intent=new Intent(this, HomeFragment.class);
        startActivity(intent);

    }
}

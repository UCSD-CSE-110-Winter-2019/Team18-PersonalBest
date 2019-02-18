package com.android.personalbest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class GetToKnowUActivity extends AppCompatActivity {
    String TAG = GetToKnowUActivity.class.getName();
    private static int RC_SIGN_IN = 100;
    GoogleSignInAndOut gSignInAndOut;

    EditText name;
    EditText heightft;
    EditText heightin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_to_know_u);
        gSignInAndOut = new GoogleSignInAndOut(this, TAG);
        gSignInAndOut.signIn();

        Button finish=(Button) findViewById(R.id.finish_btn);
        final Context context = GetToKnowUActivity.this;

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=findViewById(R.id.name_input);
                heightft=findViewById(R.id.heightft_input);
                heightin=findViewById(R.id.heightin_input);
                int ft=CheckInvalid.checkForHeightft(heightft.getText());
                int in=CheckInvalid.checkForHeightin(heightin.getText());

                if(!CheckInvalid.checkForName(name.getText())||in<0||ft<0){
                    Log.d("invalid", String.valueOf(CheckInvalid.checkForName(name.getText())));
                    Toast.makeText(GetToKnowUActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();}
                else{
                    SharedPrefData.setName(context, name.getText().toString());
                    SharedPrefData.setHeightFt(context, ft);
                    SharedPrefData.setHeightIn(context, in);
                    SharedPrefData.setGoal(context, 5000);

                    Toast.makeText(GetToKnowUActivity.this, "Saved", Toast.LENGTH_SHORT);
                    launchActivity();
                    }
            }

        });
    }

    public void launchActivity(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            //If sign in was successful then launchActivity
            if ( gSignInAndOut.handleSignInResult(task) == true )
            {
                // No longer need to launch activity, taken care of by finish button
            }
        }
    }
}

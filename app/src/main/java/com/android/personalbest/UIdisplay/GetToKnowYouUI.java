package com.android.personalbest.UIdisplay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.personalbest.CheckInvalid;
import com.android.personalbest.signin.GoogleSignAndOut;
import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.SharedPrefData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class GetToKnowYouUI extends AppCompatActivity {
    String TAG = GetToKnowYouUI.class.getName();
    private static int RC_SIGN_IN = 100;
    GoogleSignAndOut gSignInAndOut;

    EditText name;
    EditText heightft;
    EditText heightin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_to_know_u);
        gSignInAndOut = new GoogleSignAndOut(this, TAG);
        gSignInAndOut.signIn();

        Button finish=(Button) findViewById(R.id.finish_btn);
        final Context context = GetToKnowYouUI.this;

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=findViewById(R.id.name_input);
                heightft=findViewById(R.id.heightft_input);
                heightin=findViewById(R.id.heightin_input);
                int ft= CheckInvalid.checkForHeightft(heightft.getText());
                int in=CheckInvalid.checkForHeightin(heightin.getText());

                if(!CheckInvalid.checkForName(name.getText())||in<0||ft<0){
                    Log.d("invalid", String.valueOf(CheckInvalid.checkForName(name.getText())));
                    Toast.makeText(GetToKnowYouUI.this, "Invalid Input", Toast.LENGTH_SHORT).show();}
                else{
                    SharedPrefData.setName(context, name.getText().toString());
                    SharedPrefData.setHeightFt(context, ft);
                    SharedPrefData.setHeightIn(context, in);
                    SharedPrefData.setGoal(context, 5000);

                    Toast.makeText(GetToKnowYouUI.this, "Saved", Toast.LENGTH_SHORT);
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
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            if ( gSignInAndOut.handleSignInResult(task) == true )
            {
                SharedPrefData.setAccountId(this, GoogleSignIn.getLastSignedInAccount(this).getId());
                // If the user logs in, already has an account, and tries to create a new account
                // redirects them to their home page/the main activity
                if (SharedPrefData.userSharedPrefExists(this)) {
                    launchActivity();
                }
            }
        }
    }
}

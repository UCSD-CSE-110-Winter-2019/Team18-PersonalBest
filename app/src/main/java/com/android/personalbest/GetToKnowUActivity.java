package com.android.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    LogInAndOut gSignInAndOut;
    String name="";
    int heightft;
    int heightin;
    boolean invalid=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_to_know_u);

        gSignInAndOut = new GoogleSignInAndOut(this, TAG);

        Button finish=(Button) findViewById(R.id.finish_btn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalid=false;
                name=((EditText)findViewById(R.id.name_input)).getText().toString();

                try{
                heightft=Integer.parseInt(((EditText)findViewById(R.id.heightft_input)).getText().toString());
                heightin=Integer.parseInt(((EditText)findViewById(R.id.heightin_input)).getText().toString());
                }
                catch(Throwable e){
                    Toast.makeText(GetToKnowUActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                    invalid=true;
                }
                if(name.isEmpty()){
                    invalid=true;
                    Toast.makeText(GetToKnowUActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
                if(!invalid){
                    SharedPreferences sharedPreferences=getSharedPreferences("user_info", MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putInt("heightft", heightft);
                    editor.putInt("heightin", heightin);
                    editor.commit();
                    Toast.makeText(GetToKnowUActivity.this, "Saved", Toast.LENGTH_SHORT);
                    gSignInAndOut.signIn();}
            }
        });
    }

    public void launchActivity(){

        Intent intent=new Intent(this, ProfileBtmNavActivity.class);
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
            launchActivity();
        }
    }
}
}

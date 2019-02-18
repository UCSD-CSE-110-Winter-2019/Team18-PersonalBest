package com.android.personalbest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static int RC_SIGN_IN_KEY = 100;
    String TAG = LoginActivity.class.getName();
    private static int RC_SIGN_IN = RC_SIGN_IN_KEY;
    GoogleSignInAndOut gSignInAndOut;

    /**
     * Begins at the start of the Login Activity to see if user has an account already.
     */
    @Override
    protected void onStart()
    {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null and user can sign in.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(gSignInAndOut.updateUI(account))
        {
            launchHomeScreenActivity();
        }
    }

    /**
     * Checks the result of calling signIn() method.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if ( gSignInAndOut.handleSignInResult(task) )
            {
                // Checks if users sign-in on their first use without creating an account first,
                // redirects them to collect info such as their name and height
                if (SharedPrefData.userSharedPrefExists(this.getApplicationContext())) {
                    launchHomeScreenActivity();
                } else {
                    launchGetToKnowYouActivity();
                }

            }
        }
    }


    /**
     * Creates buttons for create account and google sign in.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gSignInAndOut = new GoogleSignInAndOut(this, TAG);

        //Create account button
        Button createAccountButton = findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Switches to the get personal info activity
                launchGetToKnowYouActivity();
            }
        });

        // Create and set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gSignInAndOut.signIn();
            }
        });

    }

    public void launchGetToKnowYouActivity() {
        Intent intent = new Intent(this, GetToKnowUActivity.class);
        startActivity(intent);

    }

    public void launchHomeScreenActivity()
    {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
}
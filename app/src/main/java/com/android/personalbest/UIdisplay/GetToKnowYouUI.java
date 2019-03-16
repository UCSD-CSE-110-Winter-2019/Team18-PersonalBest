package com.android.personalbest.UIdisplay;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.personalbest.CheckInvalid;
import com.android.personalbest.User;
import com.android.personalbest.firestore.FirestoreAdaptor;
import com.android.personalbest.firestore.FirestoreFactory;
import com.android.personalbest.firestore.IFirestore;
import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.signin.ISignIn;
import com.android.personalbest.signin.SignInFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetToKnowYouUI extends AppCompatActivity {
    String TAG = GetToKnowYouUI.class.getName();
    private static int RC_SIGN_IN = 100;
    ISignIn gSignInAndOut;

    EditText name;
    EditText heightft;
    EditText heightin;

    IFirestore firestore;
    String email = "getToKnowYou@getToKnowYou.com";
    public static final String FIRESTORE_SERVICE_KEY = "FIRESTORE_SERVICE_KEY";
    public static final String FIRESTORE_ADAPTOR_KEY = "FIRESTORE_ADAPTOR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_to_know_u);
        gSignInAndOut = SignInFactory.create(MainActivity.signin_indicator,this, TAG);
        gSignInAndOut.signIn();

        // Determine what implementation of IFirestore to use
        String firestoreKey = getIntent().getStringExtra(FIRESTORE_SERVICE_KEY);
        if (firestoreKey == null) {
            FirestoreFactory.put(FIRESTORE_ADAPTOR_KEY, new FirestoreFactory.BluePrint() {
                @Override
                public IFirestore create(Activity activity, String userEmail) {
                    return new FirestoreAdaptor(activity, userEmail);
                }
            });
            // Default Firestore implementation using our adaptor
            firestore = new FirestoreAdaptor(this, email);
        } else {
            firestore = FirestoreFactory.create(firestoreKey, this, email);
        }

        Button finish=(Button) findViewById(R.id.finish_btn);

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

                    String userName = name.getText().toString();
                    int goal = 5000;
                    String email = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getEmail();
                    List<String> friends = new ArrayList<>();
                    Map<String, Boolean> pendingFriends = new HashMap<>();
                    Map<String, Integer> intentionalSteps= new HashMap<>();
                    Map<String, Integer> totalSteps = new HashMap<>();


                    User user = new User(userName, email, goal, ft, in, intentionalSteps, totalSteps, friends, pendingFriends);
                    firestore.addUserToFirestore(user, GetToKnowYouUI.this);
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
                firestore.getToKnowYouCheckIfUserExists(GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getEmail(), this);
            }
        }
    }
}

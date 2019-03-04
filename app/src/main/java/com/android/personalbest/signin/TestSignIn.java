package com.android.personalbest.signin;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class TestSignIn implements ISignIn{
    private static final String TAG = "[TestSignIn]: ";
    private Activity activity;
    public TestSignIn(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void signIn(){ System.out.println(TAG + "sign in");};
    public void signOut(){ System.out.println(TAG + "sign out");};
    public boolean handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        return true;
    }
    public boolean updateUI(GoogleSignInAccount acc) {
        return true;
    }
}

package com.android.personalbest.signin;

import android.app.Activity;

public class TestSignIn implements ISignIn{
    private static final String TAG = "[TestSignIn]: ";
    private Activity activity;
    public TestSignIn(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void signIn(){ System.out.println(TAG + "sign in");};
    public void signOut(){ System.out.println(TAG + "sign out");};
}

package com.android.personalbest.signin;

import android.app.Activity;

public class SignInFactory {
    public static ISignIn create(String type, Activity activity, String TAG){
        if(type=="googlesignin"){
            return new GoogleSignAndOut(activity, TAG);
        }
        else{
            return new TestSignIn(activity);
        }
    }
}

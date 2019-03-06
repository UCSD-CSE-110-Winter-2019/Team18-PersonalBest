package com.android.personalbest.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface ISignIn {
    void signIn();
    void signOut();
    boolean handleSignInResult(Task<GoogleSignInAccount> completedTask);
    boolean updateUI(GoogleSignInAccount acc);
}

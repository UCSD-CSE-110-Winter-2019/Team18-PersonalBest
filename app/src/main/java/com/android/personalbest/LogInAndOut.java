package com.android.personalbest;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface LogInAndOut
{
    void signIn();
    void signOut();
    boolean updateUI(GoogleSignInAccount acc);
    boolean handleSignInResult(Task<GoogleSignInAccount> completedTask);
}

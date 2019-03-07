package com.android.personalbest.UIdisplay;

import com.android.personalbest.User;

public interface IUserObserver {
    /**
     * Signalled when the user's fields are updated (e.g. goal changed, name changed, etc.)
     *
     * @param user New user with new values
     */
    void onUserChange(User user);
    String getObserverName();
}

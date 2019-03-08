package com.android.personalbest.messaging;

import android.app.Activity;

public class MessagingFactory {
    public static IMessaging create(int i, Activity activity, String chatID) {
        if (i == 0) {
            return new MessagingAdaptor(activity, chatID);
        } else {
            return new MessagingAdaptor(activity, chatID);
        }
    }

}

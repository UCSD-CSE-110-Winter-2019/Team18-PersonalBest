package com.android.personalbest.messaging;

import android.app.Activity;

public class MessagingFactory {
    public static IMessaging create(String i, Activity activity, String collections, String document, String message) {
        if (i.equals("test")) {
            return new MessagingAdaptor(activity, collections, document, message);
        } else if (i.equals("SERIVCE")){
            return new MessagingAdaptor(activity, collections, document);
        } else {
            return new MessagingAdaptor(activity, collections, document, message);
        }
    }

}

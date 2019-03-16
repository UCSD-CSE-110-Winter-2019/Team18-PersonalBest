package com.android.personalbest.messaging;

import android.app.Activity;

public class MessagingFactory {
    public static IMessaging create(String i, Activity activity, String collections, String document, String message) {

        // create a test messaging adaptor if the key is "test"
        if (i!= null && i.equals("test")) {
            return new MessagingAdaptor(activity, collections, document, message);
        }

        // create a messaging adaptor without document key if the key is "service"
        else if (i.equals("SERIVCE")){
            return new MessagingAdaptor(activity, collections, document);
        }

        // create a messaging adaptor with document key otherwise
        else {
            return new MessagingAdaptor(activity, collections, document, message);
        }
    }

}

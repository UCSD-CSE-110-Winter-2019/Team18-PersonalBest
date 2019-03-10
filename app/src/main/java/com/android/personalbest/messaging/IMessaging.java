package com.android.personalbest.messaging;

import android.content.Context;

import com.google.android.gms.tasks.Task;

public interface IMessaging {
    void subscribeToNotificationsTopic();
    void sendNotification(String messageBody, Context context);
    void setup();
    Task<String> addMessage(String text);
}

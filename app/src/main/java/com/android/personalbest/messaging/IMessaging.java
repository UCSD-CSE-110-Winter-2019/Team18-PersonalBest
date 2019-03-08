package com.android.personalbest.messaging;

public interface IMessaging {
    void subscribeToNotificationsTopic();
    void sendNotification(String messageBody);
    void setup();
}

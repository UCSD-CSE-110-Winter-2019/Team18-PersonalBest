package com.android.personalbest.messaging;

import android.content.Context;

import com.google.android.gms.tasks.Task;

public interface IMessaging {
    void subscribeToNotificationsTopic();
    void setup();
}

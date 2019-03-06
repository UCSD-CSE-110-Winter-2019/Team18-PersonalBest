package com.android.personalbest.firestore;

import android.widget.EditText;
import android.widget.TextView;

public interface IFirestore {
    public void displayName(TextView view);
    public void setName(String name);
    public void initMessageUpdateListener(TextView chatView, String otherUserEmail);
    public void addSentMessageToDatabase(EditText editText, String otherUserEmail);
}

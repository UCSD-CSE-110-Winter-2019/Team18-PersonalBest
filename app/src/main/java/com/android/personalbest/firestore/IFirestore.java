package com.android.personalbest.firestore;

import android.widget.EditText;
import android.widget.TextView;

public interface IFirestore {
    String displayName();
    public void setName(String name);
    public void initMessageUpdateListener(TextView chatView, String otherUserEmail);
    public void addSentMessageToDatabase(EditText editText, String otherUserEmail);
}

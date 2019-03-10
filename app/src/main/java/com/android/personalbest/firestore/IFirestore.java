package com.android.personalbest.firestore;

import android.widget.EditText;
import android.widget.TextView;

import com.android.personalbest.MainActivity;
import com.android.personalbest.UIdisplay.HomeUI;

public interface IFirestore{
    void setName(String name);
    void setGoal(int goal);
    void setHeightFt(int heightFt);
    void setHeightIn(int heightIn);
    void initMessageUpdateListener(TextView chatView, String otherUserEmail);
    void addSentMessageToDatabase(EditText editText, String otherUserEmail);
    void initMainActivity(MainActivity mainActivity, HomeUI homeUI);
    String getChatID(String otherUserEmail);
}

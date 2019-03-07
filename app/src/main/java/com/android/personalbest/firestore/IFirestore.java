package com.android.personalbest.firestore;

import android.widget.EditText;
import android.widget.TextView;

import com.android.personalbest.ISubject;
import com.android.personalbest.MainActivity;
import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.IUserObserver;

public interface IFirestore extends ISubject<IUserObserver>{
    public void setName(String name);
    public void setGoal(int goal);
    public void setHeightFt(int heightFt);
    public void setHeightIn(int heightIn);
    public void initMessageUpdateListener(TextView chatView, String otherUserEmail);
    public void addSentMessageToDatabase(EditText editText, String otherUserEmail);
    public void initMainActivity(MainActivity mainActivity, HomeUI homeUI);
    public void updatedUser();
    public void register(IUserObserver observer);
    public void unregister();
}

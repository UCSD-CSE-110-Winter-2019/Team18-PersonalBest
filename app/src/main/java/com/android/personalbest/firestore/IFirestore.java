package com.android.personalbest.firestore;

import android.widget.EditText;
import android.widget.TextView;
import com.android.personalbest.MainActivity;
import com.android.personalbest.UIdisplay.FriendsUI;
import com.android.personalbest.UIdisplay.GetToKnowYouUI;
import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.LoginUI;
import com.android.personalbest.UIdisplay.MessagesUI;
import com.android.personalbest.User;

public interface IFirestore{
    public void setName(String name);
    public void setGoal(int goal);
    public void setHeightFt(int heightFt);
    public void setHeightIn(int heightIn);
    public void initMessageUpdateListener(TextView chatView, String otherUserEmail);
    public void addSentMessageToDatabase(EditText editText, String otherUserEmail);
    public void initMainActivity(MainActivity mainActivity, HomeUI homeUI);
    public void loginCheckIfUserExists(String otherUserEmail, LoginUI loginUI);
    public void getToKnowYouCheckIfUserExists(String otherUserEmail, GetToKnowYouUI getToKnowYouUI);
    public void addUserToFirestore(User user, GetToKnowYouUI getToKnowYouUI);

    public void setIntentionalSteps(User user, long intentionalSteps);
    public void setTotalSteps(User user);

    public void sendFriendRequest(User user, String friendEmail, FriendsUI friendsUI);
    public void acceptFriendRequest(User user, String friendEmail, FriendsUI friendsUI);
    public void declineFriendRequest(User user, String friendEmail, FriendsUI friendsUI);
    public void removeFriend(User user, String friendEmail, FriendsUI friendsUI);

    public void addUserToPendingFriends(String user, String emailToAdd, boolean sender);
    public void removeUserFromPendingFriends(String user, String emailToRemove);
    public void removeUserFromFriendsList(String user, String emailToRemove);

    void addGoalToDatabase();
    String getChatID(String otherUserEmail);
    void getUser();
    public void addUserToFriends(String user, String emailToAdd);
    public void initMessagesUI(MessagesUI messagesUI, String friendEmail);

}

package com.android.personalbest.UIdisplay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.personalbest.MainActivity;
import com.android.personalbest.R;
import com.android.personalbest.User;
import com.android.personalbest.firestore.IFirestore;

import java.util.List;
import java.util.Map;

public class FriendsUI extends Fragment {

    final int TEXTVIEW_SIZE = 25;

    static LayoutInflater layoutInflater;
    LinearLayout myLinearLayout;
    Dialog myDialog;
    IFirestore firestore;
    User user;

    Map<String, Boolean> friendRequestList;
    List<String> friendsList;
    Activity activity;
    FriendsUI currentFriendsUI;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = inflater;
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_friends, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        myDialog = new Dialog(this.getActivity());
        activity = this.getActivity();
        Toast.makeText(activity, "test", Toast.LENGTH_LONG);
        // Get instance of Firestore from MainActivity and get the current logged in user
        firestore = MainActivity.getFirestore();
        user = MainActivity.getCurrentUser();

        friendRequestList = user.getPendingFriends();
        friendsList = user.getFriends();

        myLinearLayout = getView().findViewById(R.id.friend_fragment_layout);

        //Button to add a new friend
        Button addFriend = getView().findViewById(R.id.add_friend);
        addFriend.setOnClickListener(view1 -> ShowAddFriendPopup(view1));

        createPendingFriendsView();
        createFriendsView();

        currentFriendsUI = this;
    }

    public void createFriendsView() {
        friendsList = user.getFriends();

        //Add textview for Friends
        TextView friendsTextView = new TextView(activity);
        friendsTextView.setText("Friends");
        friendsTextView.setTextSize(TEXTVIEW_SIZE);
        myLinearLayout.addView(friendsTextView);
        if(friendsList==null||friendsList.isEmpty()){
            TextView nofriends = new TextView(activity);
            nofriends.setText("No Friends");
            nofriends.setTextSize(TEXTVIEW_SIZE);
            myLinearLayout.addView(nofriends);
        }

        //dynamically add friends
        for (String email : friendsList )
        {
            // create a new button
            final Button friendDisplay = new Button(activity);

            // set some properties of rowTextView or something
            friendDisplay.setText(email);

            // add the button to the linearlayout
            myLinearLayout.addView(friendDisplay);

            friendDisplay.setOnClickListener(view -> {
                showFriendOptionsPopup(view, email);
                Log.wtf("TESTER", ""+ email);
            });
        }
    }


    public void createPendingFriendsView() {
        friendRequestList = user.getPendingFriends();
        //Add TextView for Pending Friend Requests
        TextView friendsReqTextView = new TextView(activity);
        friendsReqTextView.setText("Friend Requests");
        friendsReqTextView.setTextSize(TEXTVIEW_SIZE);
        myLinearLayout.addView(friendsReqTextView);


        //Dynamically add pending friends
        for (Map.Entry<String, Boolean> entry : friendRequestList.entrySet() )
        {
            // create a new button
            final Button friendRequest = new Button(activity);

            //If user received friend request, create listener and setText for pending request
            if(!entry.getValue()) {
                // set some properties of rowTextView or something
                friendRequest.setText(entry.getKey() + " ( Pending... )");

                friendRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowFriendRequestPopup(view, entry.getKey());
                    }
                });
            }else
            {
                // set some properties of rowTextView or something
                friendRequest.setText(entry.getKey() + " ( Request Sent )");
            }

            //set id of button (to user id)
            //friendRequest.setId(i);

            // add the button to the linearlayout
            myLinearLayout.addView(friendRequest);

            // save a reference to the textview for later
            //myFriendRequests[i] = friendRequest;
        }
    }


    /**
     * Shows edit text to input email of friend you want to add and a send
     * friend request button.
     */
    public void ShowAddFriendPopup(View v) {
        Button sendRequest;
        myDialog.setContentView(R.layout.add_friend);

        sendRequest = myDialog.findViewById(R.id.send_add);
        sendRequest.setOnClickListener(v1 -> {
            //Get email from edit text
            EditText editText = myDialog.findViewById(R.id.friend_email);
            String email = editText.getText().toString();

            //Check if email is valid
            if( email.equals("") || !email.contains("@") )
            {
                Toast.makeText(activity, "Not a valid email address.", Toast.LENGTH_SHORT).show();
            } else if (friendRequestList.containsKey(email)){
                Toast.makeText(activity, "Already a pending request with this email", Toast.LENGTH_SHORT).show();
            }
            else
            {
                // Calls Firestore method that checks if requestee email exists in Firestore, sending a friend request and
                // updating both pendingFriends if it does
                firestore.sendFriendRequest(user, email, currentFriendsUI);
            }

            myDialog.dismiss();
        });

        // show the popup
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    /**
     * Show accept and decline buttons when user clicks on a friend request.
     */
    public void ShowFriendRequestPopup(View v, String username) {
        Button acceptbtn;
        Button declinebtn;
        TextView requestText;

        myDialog.setContentView(R.layout.friend_request);

        acceptbtn = myDialog.findViewById(R.id.accept_friend);
        declinebtn = myDialog.findViewById(R.id.decline_friend);
        requestText = myDialog.findViewById(R.id.request);

        requestText.setText(username + " Would like to add you");

        acceptbtn.setOnClickListener(v1 -> {
            // Update Firestore with the new user object
            firestore.acceptFriendRequest(user, username, currentFriendsUI);
            myDialog.dismiss();
        });

        declinebtn.setOnClickListener(v12 -> {
            firestore.declineFriendRequest(user, username, currentFriendsUI);
            myDialog.dismiss();
        });

        // show the popup
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    /**
     * show option to delete friend popup
     */
    public void showFriendOptionsPopup(View v, String username) {
        Button deleteBtn;
        Button goMsg;

        myDialog.setContentView(R.layout.remove_or_go_to_msg);

        deleteBtn = myDialog.findViewById(R.id.delete_friend_btn);
        goMsg = myDialog.findViewById(R.id.go_msg);

        deleteBtn.setOnClickListener(v1 -> {
            firestore.removeFriend(user, username, currentFriendsUI);
            myDialog.dismiss();
        });

        goMsg.setOnClickListener(v12 -> {
            //Launch messagingUI when user clicks on a friend
            Intent intent = new Intent(activity, MessagesUI.class);
            intent.putExtra("friend_id", username);
            startActivity(intent);
        });

        // show the popup
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    public void userHasBeenUpdated() {
        user = MainActivity.getCurrentUser();
        // Removes all views except the Add Friend button & re-draws it
        myLinearLayout.removeViews(1, myLinearLayout.getChildCount() - 1);
        createPendingFriendsView();
        createFriendsView();
    }

}

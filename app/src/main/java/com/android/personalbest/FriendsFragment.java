package com.android.personalbest;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.personalbest.fitness.GoogleFit;


public class FriendsFragment extends Fragment {

    final int TEXTVIEW_SIZE = 25;

    static LayoutInflater layoutInflater;
    Dialog myDialog;

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

        LinearLayout myLinearLayout = getView().findViewById(R.id.friend_fragment_layout);

        Button addFriend = getView().findViewById(R.id.add_friend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAddFriendPopup(view);
            }
        });

        //Add textview for Pending Friend Requests
        TextView friendsReqTextView = new TextView(this.getActivity());
        friendsReqTextView.setText("Friend Requests");
        friendsReqTextView.setTextSize(TEXTVIEW_SIZE);
        myLinearLayout.addView(friendsReqTextView);

        //TODO: INITIALIZE ARRAY WITH PENDING FRIEND REQUESTS
        final int N = 3; // total number of textviews to add

        final Button[] myFriendRequests = new Button[N]; // create an empty array;

        //Dynamically add pending friends
        for (int i = 0; i < N; i++) {

            // create a new button
            final Button friendRequest = new Button(this.getActivity());

            //set id of button (to user id)
            friendRequest.setId(i+1);

            // set some properties of rowTextView or something
            friendRequest.setText("This is friend request #" + i);

            // add the textview to the linearlayout
            myLinearLayout.addView(friendRequest);

            // save a reference to the textview for later
            myFriendRequests[i] = friendRequest;

            friendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowFriendRequestPopup(view);

                    Log.wtf("Testing friends Request", "Id of friend Request: " + friendRequest.getId());
                }
            });
        }

        //TODO: INITILIAZE ARRAY WITH CURRENT FRIENDS FROM FIREBASE
        final Button[] myFriends = new Button[N]; // create an empty array;

        //Add textview for Friends
        TextView friendsButton = new TextView(this.getActivity());
        friendsButton.setText("Friends");
        friendsButton.setTextSize(TEXTVIEW_SIZE);
        myLinearLayout.addView(friendsButton);

        //dynamically add friends
        for (int i = 0; i < N; i++) {

            // create a new textview
            final Button friend = new Button(this.getActivity());

            //set id of button (to user id)
            friend.setId(i+1);

            // set some properties of rowTextView or something
            friend.setText("This is friend #" + i);

            // add the textview to the linearlayout
            myLinearLayout.addView(friend);

            // save a reference to the textview for later
            myFriends[i] = friend;

            friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.wtf("Testing friends", "Id of friend: " + friend.getId());
                }
            });
        }
    }


    public void ShowAddFriendPopup(View v) {
        Button btnClose;
        myDialog.setContentView(R.layout.add_friend);


        // go back to friends page when clicking the send button
        btnClose = myDialog.findViewById(R.id.send_add);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: SEND FRIEND REQUEST THEN DISMISS
                myDialog.dismiss();
            }
        });

        // show the popup
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowFriendRequestPopup(View v) {
        Button acceptbtn;
        Button declinebtn;
        myDialog.setContentView(R.layout.friend_request);


        // go back to home page when clicking the close button
        acceptbtn = myDialog.findViewById(R.id.accept_friend);
        declinebtn = myDialog.findViewById(R.id.decline_friend);

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add user to friends list
                // TODO: remove user from pending list
                myDialog.dismiss();
            }
        });

        declinebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add user to friends list
                //TODO: remove user from pending list
                myDialog.dismiss();
            }
        });

        // show the popup
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}

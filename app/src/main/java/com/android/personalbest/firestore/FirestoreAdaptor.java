package com.android.personalbest.firestore;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.personalbest.MainActivity;
import com.android.personalbest.UIdisplay.FriendsUI;
import com.android.personalbest.UIdisplay.GetToKnowYouUI;
import com.android.personalbest.UIdisplay.HomeUI;
import com.android.personalbest.UIdisplay.LoginUI;
import com.android.personalbest.UIdisplay.MessagesUI;
import com.android.personalbest.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import io.opencensus.trace.MessageEvent;

import static android.content.ContentValues.TAG;



public class FirestoreAdaptor implements IFirestore {
    private static final String TAG = "[FirestoreAdaptor]";
    private Activity activity;
    String userEmail;
    FirebaseFirestore fs;
    private boolean taskCompleted = false;

    String USERS_COLLECTION_KEY = "users";

    String CHATS_COLLECTION_KEY = "chats";
    String MESSAGES_KEY = "messages";
    String TIMESTAMP_KEY = "timestamp";
    String FROM_EMAIL_KEY = "fromEmail";
    String FROM_NAME_KEY = "fromName";
    String TEXT_KEY = "text";
    String FRIENDS_KEY = "friends";
    String PENDING_FRIENDS_KEY = "pendingFriends";
    String INTENTIONAL_STEPS_KEY = "intentionalSteps";
    String TOTAL_STEPS_KEY = "totalSteps";

    public String user_name;

    public FirestoreAdaptor(Activity activity, String userEmail) {
        this.activity = activity;
        this.userEmail = userEmail;
        this.fs = FirebaseFirestore.getInstance();
    }

    // IDs for chats between friends are generated via concatenating emails in alphabetical order

    public String getChatID(String otherUserEmail) {
        String email1 = this.userEmail.replace("@","");
        String email2 = otherUserEmail.replace("@", "");
        int comparison = email1.compareToIgnoreCase(email2);

        // If userEmail comes before otherUserEmail, compareToIgnoreCase will return -1,
        // concatenate userEmail in front of otherUserEmail
        return comparison < 0 ? email1.concat(email2) : email2.concat(email1);
    }

    // Retrieves the timestamp of the current day at 12:00am in milliseconds
    private long getTodayInMilliseconds() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        return cal.getTimeInMillis();
    }


    public void setName(String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);
        userRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Name successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    @Override
    public void setGoal(int goal) {
        Map<String, Object> data = new HashMap<>();
        data.put("goal", goal);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);
        userRef.set(data, SetOptions.merge())

                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e))
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Goal successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    @Override
    public void setHeightFt(int heightFt) {
        Map<String, Object> data = new HashMap<>();
        data.put("heightFt", heightFt);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);
        userRef.set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Height Feet successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    @Override
    public void setHeightIn(int heightIn) {
        Map<String, Object> data = new HashMap<>();
        data.put("heightIn", heightIn);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);
        userRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Height Inch successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }


    /* ******************************** Send Notifications ************************* */
    // Adds listener for any new messages and appends to the given TextView
    public void initMessageUpdateListener(TextView chatView, String otherUserEmail) {
        CollectionReference chat = fs.collection(CHATS_COLLECTION_KEY).document(getChatID(otherUserEmail)).collection(MESSAGES_KEY);

        chat.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();

                        for (DocumentChange change : documentChanges) {
                            QueryDocumentSnapshot document = change.getDocument();
                            sb.append(document.get(FROM_EMAIL_KEY));
                            sb.append(":\n");
                            sb.append(document.get(TEXT_KEY));
                            sb.append("\n");
                            sb.append("---\n");
                        }

                        chatView.append(sb.toString());
                    }
                });

    }

    public void addSentMessageToDatabase(EditText editText, String otherUserEmail) {
        CollectionReference chat = fs.collection(CHATS_COLLECTION_KEY).document(getChatID(otherUserEmail)).collection(MESSAGES_KEY);


        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_EMAIL_KEY, userEmail);
        newMessage.put(TEXT_KEY, editText.getText().toString());

        Log.e(TAG + "-Chat", "Storing message: " + newMessage.toString());
        chat.add(newMessage).addOnSuccessListener(result -> {
            Log.e(TAG + "-Chat", "Sent succesfully!");
            Log.d(TAG, "Successfully added a message to " + getChatID(otherUserEmail) + " chat!");
            editText.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG + "-Chat", error.getLocalizedMessage());
        });
    }

    public void addGoalToDatabase() {
        DocumentReference userRef = fs.collection("goal").document(userEmail.replace("@", ""));

        Map<String, Object> data = new HashMap<>();
        data.put("has_reached_goal", "True");
//        Log.wtf("----------------",userEmail.replace("@", ""));

        userRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.wtf(TAG, "Goal successfully updated!"))
                .addOnFailureListener(e -> Log.wtf(TAG, "Error updating document", e));
    }



    /* ****************************** Activity ********************************************** */
    @Override
    public void initMainActivity(MainActivity mainActivity, HomeUI homeUI) {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    User user = document.toObject(User.class);
                    MainActivity.setCurrentUser(user);
                    mainActivity.setUpMessagingNot();
//                    mainActivity.setUpGoalNot();
//                    mainActivity.setUpService();
                    mainActivity.loadFragment(homeUI);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }


    public void getUser() {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(userEmail);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    User user = document.toObject(User.class);
                    MainActivity.setCurrentUser(user);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }



    @Override
    public void loginCheckIfUserExists(String otherUserEmail, LoginUI loginUI) {
        Log.wtf(TAG, "In loginCheckIfUserExists");
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(otherUserEmail);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "Found " + otherUserEmail + " in database");
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    loginUI.launchHomeScreenActivity();
                } else {
                    Log.d(TAG, "No user with email " + otherUserEmail + " in database");
                    loginUI.launchGetToKnowYouActivity();
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @Override
    public void getToKnowYouCheckIfUserExists(String otherUserEmail, GetToKnowYouUI getToKnowYouUI) {
        Log.wtf(TAG, "In getToKnowYouCheckIfUserExists");
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(otherUserEmail);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Found " + otherUserEmail + " in database");
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        getToKnowYouUI.launchActivity();
                    } else {
                        Log.d(TAG, "No user with email " + otherUserEmail + " in database");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void addUserToFirestore(User user, GetToKnowYouUI getToKnowYouUI) {
        fs.collection(USERS_COLLECTION_KEY).document(user.getEmail()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(getToKnowYouUI, "Saved", Toast.LENGTH_SHORT);
                        getToKnowYouUI.launchActivity();
                }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.wtf(TAG, "Error writing document", e);
                    }
                });
    }

    @Override
    public void setIntentionalSteps(User user, long intentionalSteps) {
        String currentDayKey = Long.toString(getTodayInMilliseconds());
        Map<String, Integer> currentIntentionalSteps = user.getIntentionalSteps();

        if (!currentIntentionalSteps.containsKey(currentDayKey)) {
            currentIntentionalSteps.put(currentDayKey, (int) intentionalSteps);
        } else {
            int oldNumSteps = currentIntentionalSteps.get(currentDayKey);
            currentIntentionalSteps.put(currentDayKey, oldNumSteps + (int) intentionalSteps);
        }

        user.setIntentionalSteps(currentIntentionalSteps);
        MainActivity.setCurrentUser(user);

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(user.getEmail());
        userRef.update(INTENTIONAL_STEPS_KEY, currentIntentionalSteps)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Intentional Steps successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    @Override
    public void setTotalSteps(User user) {
        Map<String, Integer> currentTotalSteps = user.getTotalSteps();

        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(user.getEmail());
        userRef.update(TOTAL_STEPS_KEY, currentTotalSteps)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Intentional Steps successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }



    /* *********************************** Friend ************************************************ */
    @Override
    public void sendFriendRequest(User user, String friendEmail, FriendsUI friendsUI) {
        DocumentReference friendRef = fs.collection(USERS_COLLECTION_KEY).document(friendEmail);

        friendRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                // Friend's email exists in the database
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    Toast.makeText(friendsUI.getActivity(), "Sending Friend Request", Toast.LENGTH_LONG).show();

                    // Update sender's & receiver's pendingFriends list
                    addUserToPendingFriends(userEmail, friendEmail, true);
                    addUserToPendingFriends(friendEmail, userEmail, false);

                    // Update UI for the current user
                    Map<String, Boolean> currentPendingFriends = user.getPendingFriends();
                    currentPendingFriends.put(friendEmail, true);
                    user.setPendingFriends(currentPendingFriends);
                    MainActivity.setCurrentUser(user);
                    friendsUI.userHasBeenUpdated();


                // Email inputted does not exist in the database
                } else {
                    Log.d(TAG, "Cannot send a friend request to " + friendEmail + ". User does not exist");
                    Toast.makeText(friendsUI.getActivity(), "User does not exist.", Toast.LENGTH_SHORT).show();

                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @Override
    public void acceptFriendRequest(User user, String friendEmail, FriendsUI friendsUI) {
        // Update UI for the current user
        Map<String, Boolean> currentPendingFriends = user.getPendingFriends();
        currentPendingFriends.remove(friendEmail);
        user.setPendingFriends(currentPendingFriends);

        List<String> currentFriends = user.getFriends();
        currentFriends.add(friendEmail);
        user.setFriends(currentFriends);

        MainActivity.setCurrentUser(user);
        friendsUI.userHasBeenUpdated();

        Toast.makeText(friendsUI.getActivity(), "Accepted friend request from " + friendEmail, Toast.LENGTH_SHORT).show();

        // Remove each other from respective pendingFriends
        removeUserFromPendingFriends(user.getEmail(), friendEmail);
        removeUserFromPendingFriends(friendEmail, user.getEmail());

        // Add each other to respective friends list
        addUserToFriends(user.getEmail(), friendEmail);
        addUserToFriends(friendEmail, user.getEmail());
    }

    @Override
    public void declineFriendRequest(User user, String friendEmail, FriendsUI friendsUI) {
        // Update UI for the current user
        Map<String, Boolean> currentPendingFriends = user.getPendingFriends();
        currentPendingFriends.remove(friendEmail);
        user.setPendingFriends(currentPendingFriends);

        MainActivity.setCurrentUser(user);
        friendsUI.userHasBeenUpdated();

        Toast.makeText(friendsUI.getActivity(), "Declined friend request from " + friendEmail, Toast.LENGTH_SHORT).show();

        // Remove each other from respective pending friends
        removeUserFromPendingFriends(user.getEmail(), friendEmail);
        removeUserFromPendingFriends(friendEmail, user.getEmail());
    }

    @Override
    public void removeFriend(User user, String friendEmail, FriendsUI friendsUI) {
        // update UI for current user
        List<String> currentFriends = user.getFriends();
        currentFriends.remove(friendEmail);
        user.setFriends(currentFriends);

        MainActivity.setCurrentUser(user);
        friendsUI.userHasBeenUpdated();

        Toast.makeText(friendsUI.getActivity(), friendEmail + " has been removed", Toast.LENGTH_SHORT).show();

        //Remove each other from respective friends lists
        removeUserFromFriendsList(user.getEmail(), friendEmail);
        removeUserFromFriendsList(friendEmail, user.getEmail());
    }

    // Adds emailToAdd to the pendingList of user
    // sender is true if user is the one who sent the friend request
    @Override
    public void addUserToPendingFriends(String user, String emailToAdd, boolean sender) {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(user);

        // Get the user we want to add the request to
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    User user1 = document.toObject(User.class);

                    // Update the pendingFriends map with the new friend request in Firestore
                    Map<String, Boolean> currentPendingFriends = user1.getPendingFriends();
                    currentPendingFriends.put(emailToAdd, sender);
                    userRef.update(PENDING_FRIENDS_KEY, currentPendingFriends)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Pending Friends (Add) successfully updated!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));

                } else {
                    Log.d(TAG, "No user with email " + user + " in database");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    // Removes emailToRemove from the pendingFriends of user
    @Override
    public void removeUserFromPendingFriends(String user, String emailToRemove) {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(user);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        User user = document.toObject(User.class);

                        // Update the pendingFriends map with the new friend request in Firestore
                        Map<String, Boolean> currentPendingFriends = user.getPendingFriends();
                        currentPendingFriends.remove(emailToRemove);
                        userRef.update(PENDING_FRIENDS_KEY, currentPendingFriends)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Pending Friends (Remove) successfully updated!"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));

                    } else {
                        Log.d(TAG, "No user with email " + user + " in database");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    // Removes emailToRemove from the pendingFriends of user
    @Override
    public void removeUserFromFriendsList(String user, String emailToRemove) {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(user);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    User user1 = document.toObject(User.class);

                    List<String> currentFriends = user1.getFriends();
                    currentFriends.remove(emailToRemove);
                    userRef.update(FRIENDS_KEY, currentFriends)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));

                } else {
                    Log.d(TAG, "No user with email " + user + " in database");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    // Adds emailToAdd to user's friends
    @Override
    public void addUserToFriends(String user, String emailToAdd) {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(user);

        // Get the user we want to add the request to
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    User user1 = document.toObject(User.class);

                    // Update the pendingFriends map with the new friend request in Firestore
                    List<String> currentFriends = user1.getFriends();
                    currentFriends.add(emailToAdd);
                    userRef.update(FRIENDS_KEY, currentFriends)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Friends (Add) successfully updated!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));

                } else {
                    Log.d(TAG, "No user with email " + user + " in database");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }


    @Override
    public void initMessagesUI(MessagesUI messagesUI, String friendEmail) {
        DocumentReference userRef = fs.collection(USERS_COLLECTION_KEY).document(friendEmail);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    User friend = document.toObject(User.class);
                    MessagesUI.setCurrentFriend(friend);

                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }
}

package com.android.personalbest;

import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private String email;
    private int goal;
    private int heightFt;
    private int heightIn;
    private Map<String, Integer> intentionalSteps;
    private List<String> friends;
    private Map<String, Boolean> pendingFriends;


    public User() {}

    public User(String name, String email, int goal, int heightFt, int heightIn, Map<String, Integer> intentionalSteps, List<String> friends, Map<String, Boolean> pendingFriends) {
        this.name = name;
        this.email = email;
        this.goal = goal;
        this.heightFt = heightFt;
        this.heightIn = heightIn;
        this.intentionalSteps = intentionalSteps;
        this.friends = friends;
        this.pendingFriends = pendingFriends;
    }


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public int getGoal() {
        return goal;
    }


    public int getHeightFt() {
        return heightFt;
    }


    public int getHeightIn() {
        return heightIn;
    }


    public Map<String, Integer> getIntentionalSteps() {
        return intentionalSteps;
    }


    public List<String> getFriends() {
        return friends;
    }


    public Map<String, Boolean> getPendingFriends() {
        return pendingFriends;
    }

}

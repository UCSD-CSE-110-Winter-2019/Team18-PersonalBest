package com.android.personalbest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private String email;
    private int goal;
    private int heightFt;
    private int heightIn;
    private Map<String, Integer> intentionalSteps;
    private Map<String, Integer> totalSteps;
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
    public void setName(String name) { this.name=name;}

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) { this.email=email;}

    public int getGoal() {
        return goal;
    }
    public void setGoal(int goal) { this.goal = goal;}

    public int getHeightFt() {
        return heightFt;
    }
    public void setHeightFt(int heightFt) { this.heightFt = heightFt;}

    public int getHeightIn() {
        return heightIn;
    }
    public void setHeightIn(int heightIn) { this.heightIn= heightIn;}

    public Map<String, Integer> getIntentionalSteps() {
        return intentionalSteps;
    }
    public Map<String, Integer> getTotalSteps() {
        return totalSteps;
    }
    public void setTotalSteps(Map<String, Integer> totalSteps) {
        this.totalSteps = totalSteps;
    }
    public void setIntentionalSteps(Map<String, Integer> intentionalSteps) { this.intentionalSteps= intentionalSteps;}

    public List<String> getFriends() {
        return friends;
    }
    public  void setFriends(List<String> friends) {this.friends=friends;}

    public Map<String, Boolean> getPendingFriends() {
        return pendingFriends;
    }
    public void setPendingFriends(Map<String, Boolean> pendingFriends) {this.pendingFriends= pendingFriends;}

}

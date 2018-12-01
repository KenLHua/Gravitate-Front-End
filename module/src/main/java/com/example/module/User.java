package com.example.module;

import java.util.Map;

public class User {
    private String uid, memberships, fullName;
    private String phoneNumber;
    // private Image pic;
    private Map<String,User> friendList;
    // private Map<String,Event> eventSchedule;

    public User () {
    } // Needed for Firestore

    // Need to add Picture and EventSchedule
    public User ( String uid, String memberships, String fullName,
                  String phoneNumber, Map<String,User> friendList) {
        this.uid = uid;
        this.memberships = memberships;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber
        // pic = Pic;
        this.friendList = friendList;
        // eventSchedule = EventSchedule;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMembership() {
        return memberships;
    }

    public void setMembership(String memberships) {
        this.memberships = memberships;
    }

}

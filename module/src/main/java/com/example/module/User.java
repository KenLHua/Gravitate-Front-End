package com.example.module;

import java.util.Map;

public class User {
    private String uid, membership, firstName, lastName;
    // private Image pic;
    private Map<String,User> friendList;
    // private Map<String,Event> eventSchedule;

    public User () {
    } // Needed for Firestore

    // Need to add Picture and EventSchedule
    public User ( String UID, String Membership, String FirstName, String LastName,
                  Map<String,User> FriendList) {
        uid = UID;
        membership = Membership;
        firstName = FirstName;
        lastName = LastName;
        // pic = Pic;
        friendList = FriendList;
        // eventSchedule = EventSchedule;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}

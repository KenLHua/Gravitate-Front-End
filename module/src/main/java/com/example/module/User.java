package com.example.module;

import java.util.Map;

public class User {
    private String mUid, mMemberships, mfirstName, mlastName;
    // private Image mPicture;
    private Map<String,User> mFriendList;
    // private Map<String,Event> mEventSchedule;

    public User () {
    } // Needed for Firestore

    // Need to add Picture and EventSchedule
    public User ( String uid, String memberships, String firstName, String lastName,
                  Map<String,User> friendList) {
        mUid = uid;
        mMemberships = memberships;
        mfirstName = firstName;
        mlastName = lastName;
        // mPicture = picture;
        mFriendList = friendList;
        // mEventSchedule = eventSchedule;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmMemberships() {
        return mMemberships;
    }

    public void setmMemberships(String mMemberships) {
        this.mMemberships = mMemberships;
    }

    public String getMfirstName() {
        return mfirstName;
    }

    public void setMfirstName(String mfirstName) {
        this.mfirstName = mfirstName;
    }

    public String getMlastName() {
        return mlastName;
    }

    public void setMlastName(String mlastName) {
        this.mlastName = mlastName;
    }

    public Map<String, User> getmFriendList() {
        return mFriendList;
    }

    public void setmFriendList(Map<String, User> mFriendList) {
        this.mFriendList = mFriendList;
    }
}

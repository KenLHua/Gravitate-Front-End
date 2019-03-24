package com.example.ken.gravitate.Models;


import com.google.firebase.firestore.DocumentReference;

import java.util.List;


// Model class that holds all the firebase ride request information
public class UserEvent extends FirestoreModel {

//    private Long mStartTimestamp;
//    private Long mEndTimestamp;
    private String mEventCategory;
    private boolean mIsClosed;
//    private DocumentReference mLocationRef;
//    private String mEventLocation;
    private List<String> mParticipants;
    private Long mPricing;

    private String mLocalDateString;
    private String mName;
    private String mDescription;
    private String mFbEventId;
    private String mLocationId;

    public UserEvent() {
    } // Needed for Firebase

//    public UserEvent(Long startTimestamp, Long endTimestamp, String eventCategory,
//                     boolean isClosed, DocumentReference locationRef,
//                     String eventLocation, List<String> participants, Long pricing) {
//
//        mEventCategory = eventCategory;
//        mIsClosed = isClosed;
//        mEventLocation = eventLocation;
//        mParticipants = participants;
//        mPricing = pricing;
//    }

    public String getEventCategory() {
        return mEventCategory;
    }

    public void setEventCategory(String mEventCategory) {
        this.mEventCategory = mEventCategory;
    }

    public boolean getIsClosed() {
        return mIsClosed;
    }

    public void setIsClosed(boolean mIsClosed) {
        this.mIsClosed = mIsClosed;
    }
//
//    public String getEventLocation() {
//        return mEventLocation;
//    }
//
//    public void setEventLocation(String mEventLocation) {
//        this.mEventLocation = mEventLocation;
//    }

    public List<String> getParticipants() {
        return mParticipants;
    }

    public void setParticipants(List<String> mParticipants) {
        this.mParticipants = mParticipants;
    }

    public Long getPricing() {
        return mPricing;
    }

    public void setPricing(Long mPricing) {
        this.mPricing = mPricing;
    }

    public String getLocalDateString() {
        return mLocalDateString;
    }

    public void setLocalDateString(String mLocalDateString) {
        this.mLocalDateString = mLocalDateString;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getFbEventId() {
        return mFbEventId;
    }

    public void setFbEventId(String mFbEventId) {
        this.mFbEventId = mFbEventId;
    }

    public String getLocationId() {
        return mLocationId;
    }

    public void setLocationId(String mLocationId) {
        this.mLocationId = mLocationId;
    }
}

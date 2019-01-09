package com.example.ken.gravitate.Models;


import com.google.firebase.firestore.DocumentReference;

import java.util.List;


// Model class that holds all the firebase ride request information
public class Event extends FirestoreModel {

    private Long mStartTimestamp;
    private Long mEndTimestamp;
    private String mEventCategory;
    private boolean mIsClosed;
    private DocumentReference mLocationRef;
    private String mEventLocation;
    private List<String> mParticipants;
    private Long mPricing;

    public Event() {
    } // Needed for Firebase

    public Event(Long startTimestamp, Long endTimestamp, String eventCategory,
                 boolean isClosed, DocumentReference locationRef,
                 String eventLocation, List<String> participants, Long pricing) {
        mStartTimestamp = startTimestamp;
        mEndTimestamp = endTimestamp;
        mEventCategory = eventCategory;
        mIsClosed = isClosed;
        mLocationRef = locationRef;
        mEventLocation = eventLocation;
        mParticipants = participants;
        mPricing = pricing;
    }

    public Long getStartTimestamp() {
        return mStartTimestamp;
    }

    public void setStartTimestamp(Long mStartTimestamp) {
        this.mStartTimestamp = mStartTimestamp;
    }

    public Long getEndTimestamp() {
        return mEndTimestamp;
    }

    public void setEndTimestamp(Long mEndTimestamp) {
        this.mEndTimestamp = mEndTimestamp;
    }

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

    public DocumentReference getLocationRef() {
        return mLocationRef;
    }

    public void setLocationRef(DocumentReference mLocationRef) {
        this.mLocationRef = mLocationRef;
    }

    public String getEventLocation() {
        return mEventLocation;
    }

    public void setEventLocation(String mEventLocation) {
        this.mEventLocation = mEventLocation;
    }

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
}

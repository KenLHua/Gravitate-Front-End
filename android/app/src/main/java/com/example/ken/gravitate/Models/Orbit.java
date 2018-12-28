package com.example.ken.gravitate.Models;

import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

public class Orbit {
    private DocumentReference mChatroomRef;
    private double mCostEstimate;
    private DocumentReference mEventRef;
    private String mOrbitCategory;
    private double mStatus; // TODO: check if int works better

    private Map<String, Object> userTicketPairs;

    public Orbit() {
        /*
        An empty constructor is required by Firestore. 
         */
    }

    public DocumentReference getChatroomRef() {
        return mChatroomRef;
    }

    public void setChatroomRef(DocumentReference chatroomRef) {
        this.mChatroomRef = chatroomRef;
    }

    public double getCostEstimate() {
        return mCostEstimate;
    }

    public void setCostEstimate(double costEstimate) {
        this.mCostEstimate = costEstimate;
    }

    public DocumentReference getEventRef() {
        return mEventRef;
    }

    public void setEventRef(DocumentReference eventRef) {
        this.mEventRef = eventRef;
    }

    public String getOrbitCategory() {
        return mOrbitCategory;
    }

    public void setOrbitCategory(String orbitCategory) {
        this.mOrbitCategory = orbitCategory;
    }

    public double getStatus() {
        return mStatus;
    }

    public void setStatus(double status) {
        this.mStatus = status;
    }

    public Map<String, Object> getUserTicketPairs() {
        return userTicketPairs;
    }

    public void setUserTicketPairs(Map<String, Object> userTicketPairs) {
        this.userTicketPairs = userTicketPairs;
    }
}

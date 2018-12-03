package com.example.ken.gravitate.Models;


import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

public class RideRequest {

    private String mRideCategory;
    private boolean mDriverStatus;
    private String mPickupAddress;
    private boolean mHasCheckedIn;
    private DocumentReference mEventRef; // TODO change String to DocumentReference
    private DocumentReference mOrbitRef;
    private HashMap<String, Object> mTarget;
    private Object mPricing;
    private String mFlightLocalTime;
    private String mFlightNumber;
    private Object mAirportLocation; // TODO change
    private HashMap<String, Object> mBaggages;
    private HashMap<String, Object> mDisabilities;
    private boolean mRequestCompletion;

    public RideRequest() {
    } // Needed for Firebase

    public RideRequest(String rideCategory,
                              boolean driverStatus,
                              String pickupAddress,
                              boolean hasCheckedIn,
                       DocumentReference eventRef,
                       DocumentReference orbitRef,
                              HashMap<String, Object> target,
                              Object pricing,
                              String flightLocalTime,
                              String flightNumber,
                              Object airportLocation,
                       HashMap<String, Object> baggages,
                       HashMap<String, Object> disabilities,
                              boolean requestCompletion) {
        mRideCategory = rideCategory;
        mDriverStatus = driverStatus;
        mPickupAddress = pickupAddress;
        mHasCheckedIn = hasCheckedIn;
        mEventRef = eventRef;
        mOrbitRef = orbitRef;
        mTarget = target;
        mPricing = pricing;
        mFlightLocalTime = flightLocalTime;
        mFlightNumber = flightNumber;
        mAirportLocation = airportLocation;
        mBaggages = baggages;
        mDisabilities = disabilities;
        mRequestCompletion = requestCompletion;
    }

    public String getRideCategory() {
        return mRideCategory;
    }

    public void setRideCategory(String mRideCategory) {
        this.mRideCategory = mRideCategory;
    }

    public boolean isDriverStatus() {
        return mDriverStatus;
    }

    public void setDriverStatus(boolean mDriverStatus) {
        this.mDriverStatus = mDriverStatus;
    }

    public String getPickupAddress() {
        return mPickupAddress;
    }

    public void setPickupAddress(String mPickupAddress) {
        this.mPickupAddress = mPickupAddress;
    }

    public boolean isHasCheckedIn() {
        return mHasCheckedIn;
    }

    public void setHasCheckedIn(boolean mHasCheckedIn) {
        this.mHasCheckedIn = mHasCheckedIn;
    }

    public DocumentReference getEventRef() {
        return mEventRef;
    }

    public void setEventRef(DocumentReference mEventRef) {
        this.mEventRef = mEventRef;
    }

    public DocumentReference getOrbitRef() {
        return mOrbitRef;
    }

    public void setOrbitRef(DocumentReference mOrbitRef) {
        this.mOrbitRef = mOrbitRef;
    }

    public HashMap<String, Object> getTarget() {
        return mTarget;
    }

    public void setTarget(HashMap<String, Object> mTarget) {
        this.mTarget = mTarget;
    }

    public Object getPricing() {
        return mPricing;
    }

    public void setPricing(Object mPricing) {
        this.mPricing = mPricing;
    }

    public String getFlightLocalTime() {
        return mFlightLocalTime;
    }

    public void setFlightLocalTime(String mFlightLocalTime) {
        this.mFlightLocalTime = mFlightLocalTime;
    }

    public String getFlightNumber() {
        return mFlightNumber;
    }

    public void setFlightNumber(String mFlightNumber) {
        this.mFlightNumber = mFlightNumber;
    }

    public Object getAirportLocation() {
        return mAirportLocation;
    }

    public void setAirportLocation(Object mAirportLocation) {
        this.mAirportLocation = mAirportLocation;
    }

    public HashMap<String, Object> getBaggages() {
        return mBaggages;
    }

    public void setBaggages(HashMap<String, Object> mBaggages) {
        this.mBaggages = mBaggages;
    }

    public HashMap<String, Object> getDisabilities() {
        return mDisabilities;
    }

    public void setDisabilities(HashMap<String, Object> mDisabilities) {
        this.mDisabilities = mDisabilities;
    }

    public boolean isRequestCompletion() {
        return mRequestCompletion;
    }

    public void setRequestCompletion(boolean mRequestCompletion) {
        this.mRequestCompletion = mRequestCompletion;
    }
//
//    public String getName() { return mName; }
//
//    public void setName(String name) { mName = name; }
//
//    public String getMessage() { return mMessage; }
//
//    public void setMessage(String message) { mMessage = message; }
//
//    public String getUid() { return mUid; }
//
//    public void setUid(String uid) { mUid = uid; }
//
//    @ServerTimestamp
//    public Date getTimestamp() { return mTimestamp; }
//
//    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }


}

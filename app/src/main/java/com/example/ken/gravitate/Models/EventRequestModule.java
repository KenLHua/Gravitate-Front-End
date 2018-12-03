package com.example.ken.gravitate.Models;


import com.google.firebase.firestore.DocumentReference;

import java.util.List;


public class EventRequestModule {

    private String mDestName;
    private String mDestTime;
    private String mFlightTime;
    private DocumentReference mLocationRef;
    private List<String> mMemberProfilePhotoUrls;
    private DocumentReference mOrbitRef;
    private boolean mPending;
    private String mPickupAddress;
    private DocumentReference mRideRequestRef;

    public EventRequestModule() {
    } // Needed for Firebase

    public EventRequestModule(String destName,
                              String destTime,
                              String flightTime,
                              DocumentReference locationRef,
                              List<String> memberProfilePhotoUrls,
                              DocumentReference orbitRef,
                              boolean pending,
                              String pickupAddress,
                              DocumentReference rideRequestRef) {
        mDestName = destName;
        mDestTime = destTime;
        mFlightTime = flightTime;
        mLocationRef = locationRef;
        mMemberProfilePhotoUrls = memberProfilePhotoUrls;
        mOrbitRef = orbitRef;
        mPending = pending;
        mPickupAddress = pickupAddress;
        mRideRequestRef = rideRequestRef;

    }

    public DocumentReference getRideRequestRef() {
        return mRideRequestRef;
    }

    public String getPickupAddress() {
        return mPickupAddress;
    }

    public boolean isPending() {
        return mPending;
    }

    public DocumentReference getOrbitRef() {
        return mOrbitRef;
    }

    public List<String> getMemberProfilePhotoUrls() {
        return mMemberProfilePhotoUrls;
    }

    public DocumentReference getLocationRef() {
        return mLocationRef;
    }

    public String getFlightTime() {
        return mFlightTime;
    }

    public String getDestTime() {
        return mDestTime;
    }

    public String getDestName() {
        return mDestName;
    }

    public void setRideRequestRef(DocumentReference mRideRequestRef) {
        this.mRideRequestRef = mRideRequestRef;
    }

    public void setPickupAddress(String mPickupAddress) {
        this.mPickupAddress= mPickupAddress;
    }

    public void setPending(boolean mPending) {
        this.mPending = mPending;
    }

    public void setOrbitRef(DocumentReference mOrbitRef) {
        this.mOrbitRef=  mOrbitRef;
    }

    public void setMemberProfilePhotoUrls(List<String> mMemberProfilePhotoUrls) {
        this.mMemberProfilePhotoUrls= mMemberProfilePhotoUrls;
    }

    public void setLocationRef(DocumentReference mLocationRef) {
        this.mLocationRef = mLocationRef;
    }

    public void setFlightTime(String mFlightTime) {
        this.mFlightTime = mFlightTime;
    }

    public void setDestTime(String mDestTime) {
        this.mDestTime= mDestTime;
    }

    public void setDestName(String mDestName) {
        this.mDestName = mDestName;
    }




}

package com.example.ken.gravitate.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true) // Ignore nested objects such as target
public class AirportRide {

    private boolean driverStatus;
    private String originId;
    private String destinationId;
    private Boolean hasCheckedIn;
    private String orbitId;
    private String userId;
    private Number pricing;
    private boolean requestCompletion;
    private String rideCategory;
    private String flightLocalTime;
    private String flightNumber;
    private String locationId;
    private String pickupAddress;
    private String eventId;

    public boolean isDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(boolean driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public Boolean getHasCheckedIn() {
        return hasCheckedIn;
    }

    public void setHasCheckedIn(Boolean hasCheckedIn) {
        this.hasCheckedIn = hasCheckedIn;
    }

    public String getOrbitId() {
        return orbitId;
    }

    public void setOrbitId(String orbitId) {
        this.orbitId = orbitId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Number getPricing() {
        return pricing;
    }

    public void setPricing(Number pricing) {
        this.pricing = pricing;
    }

    public boolean isRequestCompletion() {
        return requestCompletion;
    }

    public void setRequestCompletion(boolean requestCompletion) {
        this.requestCompletion = requestCompletion;
    }

    public String getRideCategory() {
        return rideCategory;
    }

    public void setRideCategory(String rideCategory) {
        this.rideCategory = rideCategory;
    }

    public String getFlightLocalTime() {
        return flightLocalTime;
    }

    public void setFlightLocalTime(String flightLocalTime) {
        this.flightLocalTime = flightLocalTime;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    // {
    //  "driverStatus": false,
    //  "originId": "ln45oZB9VrNcvQDKAoxQ",
    //  "destinationId": "17s6P9HLonXqrWxVOob7",
    //  "hasCheckedIn": false,
    //  "eventId": "JlQ2Z8zVz3dUIthwGTHM",
    //  "orbitId": null,
    //  "userId": "testuid1",
    //  "target": {
    //    "eventCategory": "airportRide",
    //    "toEvent": true,
    //    "arriveAtEventTime": {
    //      "latest": 1545328800,
    //      "earliest": 1545318000
    //    }
    //  },
    //  "pricing": 987654321,
    //  "requestCompletion": false,
    //  "rideCategory": "airportRide",
    //  "flightLocalTime": "2018-12-20T12:00:00.000",
    //  "flightNumber": "DL89",
    //  "locationId": "17s6P9HLonXqrWxVOob7",
    //  "baggages": {},
    //  "disabilities": {},
    //  "pickupAddress": "Tenaya Hall, San Diego, CA 92161"
    //}

}

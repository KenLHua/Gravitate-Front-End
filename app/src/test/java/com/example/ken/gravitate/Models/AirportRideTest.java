package com.example.ken.gravitate.Models;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class AirportRideTest {

    private InputStream is;
    private String in;

    @Before
    public void setUp() throws Exception {
//        is = Test.class.getResourceAsStream("ride.json");
        in = "{\"driverStatus\":false,\"originId\":\"ln45oZB9VrNcvQDKAoxQ\",\"destinationId\":\"17s6P9HLonXqrWxVOob7\",\"hasCheckedIn\":false,\"eventId\":\"JlQ2Z8zVz3dUIthwGTHM\",\"orbitId\":null,\"userId\":\"testuid1\",\"target\":{\"eventCategory\":\"airportRide\",\"toEvent\":true,\"arriveAtEventTime\":{\"latest\":1545328800,\"earliest\":1545318000}},\"pricing\":987654321,\"requestCompletion\":false,\"rideCategory\":\"airportRide\",\"flightLocalTime\":\"2018-12-20T12:00:00.000\",\"flightNumber\":\"DL89\",\"locationId\":\"17s6P9HLonXqrWxVOob7\",\"baggages\":{},\"disabilities\":{},\"pickupAddress\":\"Tenaya Hall, San Diego, CA 92161\"}";

    }

    @Test
    public void getPickupAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AirportRide testObj = mapper.readValue(in, AirportRide.class);
        assertEquals("pickup address should equal",
                "Tenaya Hall, San Diego, CA 92161",
                testObj.getPickupAddress());
    }

    @Test
    public void getEventAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AirportRide testObj = mapper.readValue(in, AirportRide.class);
        assertEquals("pickup address should equal",
                "Tenaya Hall, San Diego, CA 92161",
                testObj.getPickupAddress());
    }


}
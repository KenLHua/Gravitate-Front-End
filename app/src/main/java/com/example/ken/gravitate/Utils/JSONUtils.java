package com.example.ken.gravitate.Utils;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONUtils {

    /* Parse FlightStats API JSON String */
    public static JSONObject retrieveFSInfo( String JSONString, String pickUpAddress,
                                                    boolean toEvent) {
        JSONObject flightJSON = new JSONObject();

        try {
            final String TAG = "toJSON";
            JSONObject reader = new JSONObject(JSONString);

            // Starting Airport
            JSONArray scheduledFlights = reader.getJSONArray("scheduledFlights");

            JSONObject info = scheduledFlights.getJSONObject(0);
            String flightNumber = info.getString("carrierFsCode")+info.getString("flightNumber");
            String departureAirportFsCode = info.getString("departureAirportFsCode");
            /*
            String departureTerminal = info.getString("departureTerminal");*/
            String departureTime = info.getString("departureTime");
            // Do we need to calculate Earliest and Latest Arrival in Frontend or Backend?

            // Airport Information
            JSONObject appendix = reader.getJSONObject("appendix");
            JSONArray airports = appendix.getJSONArray("airports");
            JSONObject departureAirport = airports.getJSONObject(1);
            String airport_address = departureAirport.getString("street1") + ","
                    + departureAirport.getString("city") + ","
                    + departureAirport.getString("stateCode")+ ","
                    + departureAirport.getString("postalCode");


            flightJSON.put("flightNumber",flightNumber);
            flightJSON.put("flightLocalTime",departureTime);
            flightJSON.put("pickupAddress",pickUpAddress);
            flightJSON.put("toEvent",String.valueOf(toEvent));
            flightJSON.put("airportCode", departureAirportFsCode);


        } catch (JSONException e) {
            final String TAG = "toJSON";
            Log.w(TAG, "failed: Flight JSON");
            e.printStackTrace();
        }

        return flightJSON;
    }

    public static JSONObject retrieveUserInfo (String uid, String display_name, String photo_url,
                                               String pickupAddress, String phone_number) {
        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("uid", uid);
            userJSON.put("display_name", display_name);
            userJSON.put("phone_number", phone_number);
            userJSON.put("photo_url", photo_url);
            userJSON.put("membership", "rider");
            userJSON.put("pickupAddress", pickupAddress);
        } catch (JSONException e) {
            final String TAG = "toJSON";
            Log.w(TAG, "failed: User JSON");
            e.printStackTrace();
        }

        return userJSON;
    }
}

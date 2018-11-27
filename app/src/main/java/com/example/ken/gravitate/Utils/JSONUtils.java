package com.example.ken.gravitate.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONUtils {

    public static String toJSONFlightInfo(Map<String,String> params) {
        String formattedJSON="";
        return formattedJSON;
    }

    /* Parse FlightStats API JSON String */
    public static String retrieveFSInfo( String JSONString, String pickUpAddress,
                                                    boolean toEvent) {
        JSONObject newJSON = new JSONObject();

        try {
            final String TAG = "toJSON";
            JSONObject reader = new JSONObject(JSONString);

            // Starting Airport
            JSONArray scheduledFlights = reader.getJSONArray("scheduledFlights");

            JSONObject info = scheduledFlights.getJSONObject(0);
            String flightNumber = info.getString("carrierFsCode")+info.getString("flightNumber");
            String departureAirportFsCode = info.getString("departureAirportFsCode");
            String departureTerminal = info.getString("departureTerminal");
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

            Log.w(TAG, "JSON_SUCCESS:" + airport_address);

            newJSON.put("flightNumber",flightNumber);
            newJSON.put("flightLocalTime",departureTime);
            newJSON.put("airportLocation",airport_address);
            newJSON.put("pickUpAddress",pickUpAddress);
            newJSON.put("toEvent",toEvent);


        } catch (JSONException e) {
            final String TAG = "toJSON";
            Log.w(TAG, "failed: Did not put into JSON");
            e.printStackTrace();
        }

        return newJSON.toString();
    }
}

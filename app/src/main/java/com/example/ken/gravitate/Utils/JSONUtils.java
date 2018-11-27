package com.example.ken.gravitate.Utils;

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
    private void parseFlightInfo( String JSONString ) {
        Map<String,String> params = new HashMap<>();

        try {
            JSONObject reader = new JSONObject(JSONString);

            // Starting Airport
            JSONObject scheduledFlights = reader.getJSONObject("scheduledFlights");
            String flightNumber = scheduledFlights.getString("carrierFsCode")+scheduledFlights.getString("flightNumber");
            String departureAirportFsCode = scheduledFlights.getString("departureAirportFsCode");
            String departureTerminal = scheduledFlights.getString("departureTerminal");
            String departureTime = scheduledFlights.getString("departureTime");
            // Do we need to calculate Earliest and Latest Arrival in Frontend or Backend?

            // Airport Information
            JSONArray airports = reader.getJSONArray("airports");
            JSONObject departureAirport = airports.getJSONObject(1); // Have to check if always second array
            String airport_address = departureAirport.getString("street1") + ","
                    + departureAirport.getString("city") + ","
                    + departureAirport.getString("state")+ ","
                    + departureAirport.getString("postalCode");

            params.put("flightNumber",flightNumber);
            params.put("flightLocalTime",departureTime);
            params.put("airportLocation",airport_address);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

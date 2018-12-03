package com.example.ken.gravitate.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class APIFlightStatsUtils {
    public static String getFSScheduleURL(String flightCarrier, String flightNumber,
                                          String flightYear, String flightMonth, String flightDay){
        String appId = "501e6179";
        String appKey = "d3832a53deb968c36e7e4da1af4e6ed9";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .path("api.flightstats.com/flex/schedules/rest/v1/json/flight")
                .appendPath(flightCarrier)
                .appendPath(flightNumber)
                .appendPath("departing")
                .appendPath(flightYear)
                .appendPath(flightMonth)
                .appendPath(flightDay)
                .appendQueryParameter("appId",appId)
                .appendQueryParameter("appKey",appKey);

        return builder.toString();
    }

    /* Sends a GET Request to Flightstats API
     *  RETURNS: String in JSON format that contains flight information
     * */
    public static void getFlightStats(final Context inputFlight, String request_url, final String pickupAddress,
                                      final boolean toEvent, final TextView output) {

        final String TAG = "FlightStatsAPI";
        String earliestTime = null;
        String latestTime = null;
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        JSONObject Ride_Request = JSONUtils.retrieveFSInfo(response, pickupAddress, toEvent);
                        APIRideRequestUtils.postRideRequest(inputFlight,Ride_Request);
                        output.setText(Ride_Request.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.w(TAG, "GET_REQUEST: FlightStatsAPI failure");
                    }
                });
        APIRequestSingleton.getInstance(inputFlight).addToRequestQueue(stringRequest,"getRequest");
    }

    static String getAirportCode(JSONObject ride_RequestJSON){
        String abbr = "";
        try{
            abbr = ride_RequestJSON.getString("airportAbbr");
        }
        catch(JSONException e){
            final String TAG = "getAirportCode";
            Log.w(TAG, "failed: Airport Abbreviation - JSON");
            e.printStackTrace();
        }
        return abbr;
    }

    public static String getFlightTime(JSONObject ride_RequestJSON, boolean earlyTime, boolean actualTime){
        String flightTime = null;
        int flightHour = 0;
        String flightMin = null;

        try {
            flightTime = ride_RequestJSON.getString("flightLocalTime");
            for(int i = 0; i < flightTime.length(); i++){
                if(flightTime.charAt(i) == 'T') {
                    flightHour = Integer.parseInt(flightTime.substring(i + 1, i + 3));
                    flightMin = flightTime.substring(i+4,i+6);
                    if(actualTime){
                        return flightHour+":"+flightMin;
                    }
                    if(earlyTime) {
                        flightHour = (flightHour - 5); }
                    else{
                        flightHour = (flightHour - 2); }

                    if( flightHour < 12){
                        flightTime = " AM"; }
                    else{
                        flightTime = " PM"; }
                    // Return HH/mm a
                    flightTime = (flightHour%12)+":"+flightMin+flightTime;
                    return flightTime;
                }
            }
        }
        catch (JSONException e){
            final String TAG = "toJSON";
            Log.w(TAG, "failed: Flight JSON");
            e.printStackTrace();
            return null;
        }
        return flightTime;
    }
}

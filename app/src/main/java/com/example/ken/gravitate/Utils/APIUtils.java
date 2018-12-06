package com.example.ken.gravitate.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ken.gravitate.Event.CreatedRequestDetails;
import com.example.ken.gravitate.Event.InputFlight;
import com.example.ken.gravitate.Event.ScheduledEvents;
import com.example.ken.gravitate.Models.User;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIUtils {

    /* Sends a GET Request to Flightstats API
     *  RETURNS: String in JSON format that contains flight information
     * */
    public static void testAuthEndpoint(final Context inputFlight, final String token) {

        final String url = "https://gravitate-e5d01.appspot.com/endpointTest";
        final String TAG = "Auth Test";
        Log.w(TAG, token);
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        //output.setText(Ride_Request.toString());
                        Log.w(TAG, "GET_REQUEST: Auth success");
                        Toast.makeText(inputFlight,response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.w(TAG, "GET_REQUEST: Auth failure");
                        Toast.makeText(inputFlight, "failure", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        APIRequestSingleton.getInstance(inputFlight).addToRequestQueue(stringRequest,"getRequest");
    }

    /* Gets the correct Endpoint for FlightStats Schedule API
     *  Builds through URI Builder
     *  https://developer.android.com/reference/android/net/Uri.Builder
     *  */
    static boolean occuredError;
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

    public static String getUserURL( String uid ) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .path("gravitate-e5d01.appspot.com/users")
                .appendPath(uid);

        return builder.toString();
    }

    public static String getUserURL( FirebaseUser user ) {
        String uid = user.getUid();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .path("gravitate-e5d01.appspot.com/users")
                .appendPath(uid);

        return builder.toString();
    }

    public static void postUser(final Context confirmProfile, String uid, String display_name, String phone_number, String photo_url, String pickupAddress
    , final String token) {
        final String server_url = getUserURL(uid);
        final JSONObject userJSON = JSONUtils.retrieveUserInfo(uid, display_name, phone_number, photo_url, pickupAddress);
        Log.w("USERJSON", userJSON.toString());
        final String TAG = "User";
        Log.w(TAG, userJSON.toString());
        // Formulate the request and handle the response.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, server_url, userJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with the response
                        Log.w(TAG, "POST_REQUEST: User JSON Sent");
                        Toast.makeText(confirmProfile,"Registration Success", Toast.LENGTH_LONG).show();
                        confirmProfile.startActivity(new Intent(confirmProfile, ScheduledEvents.class));
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(confirmProfile,"Registration Failed", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", token);
                        return params;
                    }
        };
        APIRequestSingleton.getInstance(confirmProfile).addToRequestQueue(jsonObjectRequest, "postRequest");
    }

    public static void getUser(final Context myProfile, FirebaseUser user, final VolleyCallback callback,
                               final String token) {

        String request_url = getUserURL(user);
        final String TAG = "User";
        // Formulate the request and handle the response.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, request_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(myProfile, error + "error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        APIRequestSingleton.getInstance(myProfile).addToRequestQueue(jsonObjectRequest, "getUserRequest");
    }

    public static void getUser(final Context myProfile, String request_url, final VolleyCallback callback,
                               final String token) {

        final String TAG = "User";
        // Formulate the request and handle the response.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, request_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(myProfile, error + "error", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", token);
                        return params;
                    }
        };
        APIRequestSingleton.getInstance(myProfile).addToRequestQueue(jsonObjectRequest, "getUserRequest");
    }



    /* Sends a GET Request to Flightstats API
     *  RETURNS: String in JSON format that contains flight information
     * */
    public static void getFlightStats(final Context inputFlight, String request_url, final String pickupAddress,
                                              final boolean toEvent, final TextView output, final String token) {

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
                        Log.w(TAG, Ride_Request.toString());
                        APIUtils.postRideRequest(inputFlight,Ride_Request, token);
                        //output.setText(Ride_Request.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.w(TAG, "GET_REQUEST: FlightStatsAPI failure");
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", token);
                        return params;
                    }
        };
        APIRequestSingleton.getInstance(inputFlight).addToRequestQueue(stringRequest,"getRequest");
    }

    private static String getAirportAbbr(JSONObject ride_RequestJSON){
        String abbr = "";
        try{
            abbr = ride_RequestJSON.getString("airportCode");
        }
        catch(JSONException e){
            final String TAG = "getAirportAbbr";
            Log.w(TAG, "failed: Airport Abbreviation - JSON");
            e.printStackTrace();
        }
        return abbr;
    }
    public static void postRideRequest(final Context inputFlight, final JSONObject Ride_RequestJSON, final String token) {
        final String server_url = "https://gravitate-e5d01.appspot.com/rideRequests";
        final String TAG = "Ride_Request";

        // Formulate the request and handle the response.
        Log.w(TAG, "REQUEST:Attempt to create jsonObjectRequest");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, server_url, Ride_RequestJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with the response
                        Log.w(TAG, "POST_REQUEST:Create Ride Request success");
                        Toast.makeText(inputFlight,"Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(inputFlight, CreatedRequestDetails.class);
                        intent.putExtra("flightTime", APIUtils.getFlightTime(Ride_RequestJSON, false, true));
                        intent.putExtra("earliestTime", APIUtils.getFlightTime(Ride_RequestJSON, true, false));
                        intent.putExtra("latestTime", APIUtils.getFlightTime(Ride_RequestJSON, false, false));
                        intent.putExtra("airportCode", APIUtils.getAirportAbbr(Ride_RequestJSON));
                        inputFlight.startActivity(intent);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(inputFlight,"Error: Flight Request not made", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", token);
                        return params;
                    }
        };
          APIRequestSingleton.getInstance(inputFlight).addToRequestQueue(jsonObjectRequest, "postRequest");

        Intent intent = new Intent(inputFlight, CreatedRequestDetails.class);
        intent.putExtra("flightTime", APIUtils.getFlightTime(Ride_RequestJSON, false, true));
        intent.putExtra("earliestTime", APIUtils.getFlightTime(Ride_RequestJSON, true, false));
        intent.putExtra("latestTime", APIUtils.getFlightTime(Ride_RequestJSON, false, false));
        // The airport abbreviation should be LAX, since we do not support other airports
        if(getAirportAbbr(Ride_RequestJSON) != "LAX") {
            return;
        }

        intent.putExtra("airportCode", APIUtils.getAirportAbbr(Ride_RequestJSON));
        inputFlight.startActivity(intent);

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

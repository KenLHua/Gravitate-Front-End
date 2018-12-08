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

    public static void postDeleteMatch(final Context mCtx, final String token, final VolleyCallback callback,
                                       final String ride_request_id) {
        final String request_url = "https://gravitate-e5d01.appspot.com/deleteMatchh";
        final String TAG = "Delete Match";
        // Formulate the request and handle the response.
        JSONObject deleteJSON = JSONUtils.deleteRideRequestJSON(ride_request_id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, request_url, deleteJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, error + "error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        APIRequestSingleton.getInstance(mCtx).addToRequestQueue(jsonObjectRequest, "postDeleteMatch");
    }


    /* Gets the correct Endpoint for FlightStats Schedule API
     *  Builds through URI Builder
     *  https://developer.android.com/reference/android/net/Uri.Builder
     *  */
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
                                              final boolean toEvent, final String date, final TextView output, final String token) {

        final String TAG = "FlightStatsAPI";
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        JSONObject Ride_Request = JSONUtils.retrieveFSInfo(response, pickupAddress, toEvent);
                        Log.w(TAG, Ride_Request.toString());
                        APIUtils.postRideRequest(inputFlight, pickupAddress, date, Ride_Request, token);
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
    public static void postRideRequest(final Context inputFlight,final String pickupAddress, final String date, final JSONObject Ride_RequestJSON, final String token) {
        final String server_url = "https://gravitate-e5d01.appspot.com/rideRequests";
        final String TAG = "Ride_Request";
        String airportCode = APIUtils.getAirportAbbr(Ride_RequestJSON);
        if ( !airportCode.equals("LAX")){
            Toast.makeText(inputFlight, "Error: Only LAX flights supported", Toast.LENGTH_LONG).show();
            return;
        }

        // Formulate the request and handle the response.
        Log.w(TAG, "REQUEST:Attempt to create jsonObjectRequest");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, server_url, Ride_RequestJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with the response
                        Log.w(TAG, "POST_REQUEST:Create Ride Request success");
                        Toast.makeText(inputFlight,"Success", Toast.LENGTH_SHORT).show();
                        // Passing information to the next activity
                        Intent intent = new Intent(inputFlight, CreatedRequestDetails.class);
                        String flightTime = APIUtils.getFlightTime(Ride_RequestJSON);
                        intent.putExtra("flightTime", flightTime);
                        intent.putExtra("earliestTime", APIUtils.parsePickupTime(flightTime, true));
                        intent.putExtra("latestTime", APIUtils.parsePickupTime(flightTime, false));
                        intent.putExtra("pickupAddress", pickupAddress);
                        intent.putExtra("airportCode", APIUtils.getAirportAbbr(Ride_RequestJSON));
                        intent.putExtra("date", date);
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

    }
    // time in the form of "##:## AM" or PM
    public static String parsePickupTime(String time, boolean early){
        int hour = Integer.parseInt(time.substring(0,2));
        int minute = Integer.parseInt(time.substring(3,5));
        boolean afternoonEarly = false;
        boolean afternoonLate = false;
        String parsedTime = "";
        if(time.charAt(6) == 'P'){
            afternoonEarly = true;
            afternoonLate = true;
        }

        if (early){
            if( ((hour <= 5) || hour ==12) && minute == 0){
                if(hour <= 5){
                    hour = hour + 12;
                }
                afternoonEarly = !afternoonEarly;
            }
            hour = hour - 5;
            parsedTime = parsedTime + hour;
            if(minute < 9){
                parsedTime = parsedTime + ":0" + minute; }
            else{
                parsedTime = parsedTime + ":" + minute; }
            if(afternoonEarly){
                parsedTime = parsedTime + " PM";
            }
            else{
                parsedTime = parsedTime + " AM";
            }
        }
        else{
            if( ((hour <= 2) || hour ==12) && minute == 0){
                if(hour <= 2){
                    hour = hour + 12;
                }
                afternoonLate = !afternoonLate;
            }
            hour = hour - 2;
            parsedTime = parsedTime + hour;
            if(minute < 9){
                parsedTime = parsedTime + ":0" + minute; }
            else{
                parsedTime = parsedTime + ":" + minute; }
            if(afternoonLate){
                parsedTime = parsedTime + " PM";
            }
            else{
                parsedTime = parsedTime + " AM";
            }
        }
        return parsedTime;

    }
    public static void postForceMatch(final Context forceMatch, JSONObject Ride_RequestJSON) {
        final String server_url = "https://gravitate-e5d01.appspot.com/devForceMatch";
        final String TAG = "ForceMatch";



        // Formulate the request and handle the response.
        Log.w(TAG, "REQUEST:Attempt to create jsonObjectRequest");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, server_url, Ride_RequestJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with the response
                        Log.w(TAG, "POST_REQUEST:Force Match success");
                        Toast.makeText(forceMatch,"Success", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(TAG, "POST_REQUEST:Force Match failed");
                        /*
                        Toast.makeText(inputFlight,"Error: Flight Request not made", Toast.LENGTH_SHORT).show();
                        */

                        error.printStackTrace();

                    }
                });
        APIRequestSingleton.getInstance(forceMatch).addToRequestQueue(jsonObjectRequest, "postRequest");



    }

    public static String getFlightTime(JSONObject ride_RequestJSON){
        String flightTime = null;

        try {
            flightTime = ride_RequestJSON.getString("flightLocalTime");
            StringBuilder parsedFlightTime = new StringBuilder(flightTime.substring(11,16));
            int flightHour = Integer.parseInt(parsedFlightTime.substring(0,2));


            // Making the local time in AM/PM form
            if(flightHour == 0){
                parsedFlightTime.append(" AM");
                parsedFlightTime.setCharAt(0,'1');
                parsedFlightTime.setCharAt(1,'2');
            }
            else if (flightHour == 12){
                parsedFlightTime.append(" PM");
            }
            else{
                // If the hour is 12 to 23, it is PM
                if( flightHour > 11){
                    parsedFlightTime.append(" PM");
                    flightHour = flightHour % 12;

                    // Insert only one hour digit
                    if(flightHour < 10){
                        parsedFlightTime.setCharAt(0, '0');
                        parsedFlightTime.setCharAt(1, Integer.toString(flightHour).charAt(0));
                    }
                    // Insert two hour digits
                    else{
                        parsedFlightTime.setCharAt(0, Integer.toString(flightHour).charAt(0));
                        parsedFlightTime.setCharAt(1, Integer.toString(flightHour).charAt(1));
                    }
                }
                else{
                    parsedFlightTime.append(" AM"); }
            }
            return parsedFlightTime.toString();
        }
        catch (JSONException e){
            final String TAG = "toJSON";
            Log.w(TAG, "failed: Flight JSON");
            e.printStackTrace();
            return null;
        }
    }

}

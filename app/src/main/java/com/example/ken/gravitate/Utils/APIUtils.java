package com.example.ken.gravitate.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.ken.gravitate.Event.RequestCreated;
import com.example.ken.gravitate.Models.Luggage;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIUtils {

    private static String backendUrl = "gravitate-dev.appspot.com";
    public static void luggageAPIUtil(final Context mCtx, final String token, final VolleyCallback callback,
                                      final String ride_request_id, List<Luggage> userLuggage){
        final String TAG = "luggage request";
        final String request_url = "https://" + backendUrl + "";
        JSONObject luggageJSON = JSONUtils.luggageJSON(userLuggage);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, request_url, luggageJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Request Error (App 4-- or Server 5--)
                        Toast.makeText(mCtx, error + "error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Adds Authorization Token to Header
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
    }

    /** postDeleteMatch IMPLEMENTED BUT FOR FUTURE **/
    /* Method Name: postDeleteMatch
       Description: Sends a POST request to unmatch an Orbit and turn into a PENDING Ride Request
       Params: Context mCtx- Context to make Toast in
               String token - Authorization Token
               VolleyCallback callback - Used to retrieve Server Response in another Context
               String ride_request_id - Ride Request ID to turn to PENDING
    */
    public static void postDeleteMatch(final Context mCtx, final String token, final VolleyCallback callback,
                                       final String ride_request_id) {

        /** Gets Information to construct JSON and Endpoint URL**/
        final String TAG = "Delete Match";
        final String request_url = "https://" + backendUrl + "/rideRequests/"+ride_request_id + "/unmatch" ;
        JSONObject deleteJSON = JSONUtils.deleteMatchJSON(ride_request_id);

        // Adds Request to RequestQueue (ASYNC TASK)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, request_url, deleteJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Need to override in Context in which this is called to act on response
                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Request Error (App 4-- or Server 5--)
                        Toast.makeText(mCtx, error + "error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Adds Authorization Token to Header
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        // Processes Request
        APIRequestSingleton.getInstance(mCtx).addToRequestQueue(jsonObjectRequest, "postDeleteMatch");
    }

    /* Method Name: postDeleteRideRequest
       Description: Sends a POST request to delete a PENDING Ride Request
       Params: Context mCtx- Context to make Toast in
               String token - Authorization Token
               VolleyCallback callback - Used to retrieve Server Response in another Context
               String user_id - User ID
               String event_id - Event ID to delete
               String ride_request_id - Ride Request ID to delete
    */
    public static void postDeleteRideRequest(final Context mCtx, final String token, final VolleyCallback callback,
                                       final String user_id, final String event_id, String ride_request_id) {
        /** Gets Information to construct JSON and Endpoint URL**/
        final String request_url = "https://" + backendUrl + "/rideRequests/"+ride_request_id;
        final String TAG = "Delete Ride Request";
//        JSONObject deleteJSON = JSONUtils.deleteRideRequestJSON(user_id, event_id, ride_request_id);

        // Adds Request to RequestQueue (ASYNC TASK)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.DELETE, request_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Need to override in Context in which this is called to act on response
                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Request Error (App 4-- or Server 5--)
                        Toast.makeText(mCtx, error + "error", Toast.LENGTH_LONG).show();
                        Log.w("TAGGIBOI", error);
                        error.printStackTrace();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Adds Authorization Token to Header
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        // Processes Request
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
        Log.w("TAGGIBOI", builder.toString());
        return builder.toString();
    }

    /* Method Name: getUserURL
       Description: Constructs Server URL Endpoint to GET User Information
       Params: String user_id - User ID
    */
    public static String getUserURL( String uid ) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .path(backendUrl + "/users")
                .appendPath(uid);

        return builder.toString();
    }

    /* Method Name: getUserURL
       Description: Constructs Server URL Endpoint to GET User Information
       Params: FirebaseUser user - User to get information from
    */
    public static String getUserURL( FirebaseUser user ) {
        String uid = user.getUid();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .path(backendUrl + "/users")
                .appendPath(uid);

        return builder.toString();
    }

    public static void postUser(final Context confirmProfile, FirebaseUser user, String display_name, String phone_number, String photo_url, String pickupAddress
    , final String token, final Class<?> cls ) {
        final String server_url = getUserURL(user);
        final String userID = user.getUid();
        final JSONObject userJSON = JSONUtils.retrieveUserInfo(userID, display_name, phone_number, photo_url, pickupAddress);
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
                        ((Activity) confirmProfile).finish();
                        Intent intent = new Intent(confirmProfile, cls);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        confirmProfile.startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(confirmProfile,"Failed to save", Toast.LENGTH_SHORT).show();
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

    /* Sends a GET Request to server to retrieve RideRequest Details
     *   RETURNS: JSONObject that contains details
     */
    public static void getRideRequest(final Context ctx, final String rideRequestId, final String token, final VolleyCallback callback){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .path(backendUrl + "/rideRequests")
                .appendPath(rideRequestId);
        final String request_url = builder.toString();
        final String TAG = "RideRequest Details";
        // Formulate the request and handle the response.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, request_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                        Log.w("GETRideRequest", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", token);
                return params;
            }
        };
        APIRequestSingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest, "getRideRequest");
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
                });
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
        final String server_url = "https://" + backendUrl + "/requestRide/" + "airport";
        final String TAG = "Ride_Request";
        String airportCode = APIUtils.getAirportAbbr(Ride_RequestJSON);
        Log.w(TAG, "AirportCode: " + airportCode);
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
                        try {
                            String id = response.getString("id");
                            // Passing information to the next activity
                            showCreated(id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showCreatedInferred();
                        }
                    }

                    private void showCreated(String id) {
                        Intent intent = new Intent(inputFlight, RequestCreated.class);
                        intent.putExtra("id", id);
                        intent.putExtra("airportCode", APIUtils.getAirportAbbr(Ride_RequestJSON));
                        inputFlight.startActivity(intent);
                    }

                    private void showCreatedInferred() {
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
                        Log.w(TAG, "POST_REQUEST:Create Ride Request failed " + new String(error.networkResponse.data) );
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
            if( ((hour <= 5) || hour ==12)){
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
            if( ((hour <= 2) || hour ==12)){
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
        final String server_url = "https://" + backendUrl + "/devForceMatch";
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

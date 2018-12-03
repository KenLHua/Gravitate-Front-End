package com.example.ken.gravitate.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ken.gravitate.Event.CreatedRequestDetails;

import org.json.JSONObject;

public class APIRideRequestUtils {
    /* Gets the correct Endpoint for FlightStats Schedule API
     *  Builds through URI Builder
     *  https://developer.android.com/reference/android/net/Uri.Builder
     *  */
    static boolean occuredError;

    public static void postRideRequest(final Context inputFlight, JSONObject Ride_RequestJSON) {
        final String server_url = "https://gravitate-e5d01.appspot.com/rideRequests";
        final String TAG = "Ride_Request";
        occuredError = true;
        // Formulate the request and handle the response.
        Log.w(TAG, "REQUEST:Attempt to create jsonObjectRequest");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, server_url, Ride_RequestJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with the response
                        Log.w(TAG, "POST_REQUEST:Create Ride Request success");
                        Toast.makeText(inputFlight,"Success", Toast.LENGTH_SHORT).show();
                        occuredError = false;
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(inputFlight,"Error: Flight Request not made", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
          APIRequestSingleton.getInstance(inputFlight).addToRequestQueue(jsonObjectRequest, "postRequest");

          if(occuredError){
              Intent intent = new Intent(inputFlight, CreatedRequestDetails.class);
              intent.putExtra("flightTime", APIFlightStatsUtils.getFlightTime(Ride_RequestJSON, false, true));
              intent.putExtra("earliestTime", APIFlightStatsUtils.getFlightTime(Ride_RequestJSON, true, false));
              intent.putExtra("latestTime", APIFlightStatsUtils.getFlightTime(Ride_RequestJSON, false, false));
              intent.putExtra("airportAbbr", APIFlightStatsUtils.getAirportCode(Ride_RequestJSON));
              inputFlight.startActivity(intent);
          }
    }
}

package com.example.ken.gravitate.Utils;

import android.util.Log;

import com.example.ken.gravitate.Models.Luggage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JSONUtils {


    // Luggages
    // - Luggage_Type param
    // - weight_in_ilbs param
    public static JSONObject luggageJSON(List<Luggage> userLuggage) {
        JSONObject luggageJSON = new JSONObject();

        try {
            for (int index = 0; index < userLuggage.size(); index++) {
                luggageJSON.put("weight" + index, userLuggage.get(index).getWeight());
                luggageJSON.put("size" + index, userLuggage.get(index).getSize());
            }

        }catch(JSONException e){
            final String TAG = "toJSON";
            Log.w(TAG, "failed: Flight JSON");
            e.printStackTrace();
            }

        return luggageJSON;
    }


    /* Method Name: retrieveFSInfo
       Description: ParsesFlightStatsAPI JSON and adds paramters pickUpAddress
                    and toEvent to the JSON
       Params: String JSONString - Complete FlightStats JSON
               String pickUpAddress - User Pickup Address
               boolean toEvent - Flag to determine if heading to Airport or coming from Airport
       Return: JSONObject flightJSON - Parsed FlightStatsJSON
    */
    public static JSONObject retrieveFSInfo( String JSONString, String pickUpAddress,
                                                    boolean toEvent) {
        JSONObject flightJSON = new JSONObject();

        /** Attempt to create flightJSON **/
        try {
            final String TAG = "toJSON";
            JSONObject reader = new JSONObject(JSONString);

            /** Parsing FlightStatsAPI JSON Data **/
            // Starting Airport
            JSONArray scheduledFlights = reader.getJSONArray("scheduledFlights");

            JSONObject info = scheduledFlights.getJSONObject(0);
            String flightNumber = info.getString("carrierFsCode")+info.getString("flightNumber");
            String departureAirportFsCode = info.getString("departureAirportFsCode");
            String departureTime = info.getString("departureTime");
            flightJSON.put("flightNumber",flightNumber);
            flightJSON.put("flightLocalTime",departureTime);
            flightJSON.put("pickupAddress",pickUpAddress);
            flightJSON.put("toEvent",String.valueOf(toEvent));
            flightJSON.put("airportCode", departureAirportFsCode);

        }

        /** JSONObject does not have one of the fields mentioned above **/
        catch (JSONException e) {
            final String TAG = "toJSON";
            Log.w(TAG, "failed: Flight JSON");
            e.printStackTrace();
        }

        return flightJSON;
    }

    /* Method Name: retrieveUserInfo
       Description: Creates JSON that contains User Information
       Params: String uid - User UID
               String display_name - User Display Name
               String photo_url - User Photo URL
               String pickupAddress - User Pick Up Address
               String phone_number - User Phone Number
       Return: JSONObject userJSON - Parsed FlightStatsJSON
    */
    public static JSONObject retrieveUserInfo (String uid, String display_name, String photo_url,
                                               String pickupAddress, String phone_number) {
        JSONObject userJSON = new JSONObject();

        /** Attempts to create userJSON **/
        try {
            userJSON.put("uid", uid);
            userJSON.put("display_name", display_name);
            userJSON.put("phone_number", phone_number);
            userJSON.put("photo_url", photo_url);
            userJSON.put("membership", "rider");
            userJSON.put("pickupAddress", pickupAddress);
        }

        /** JSONObject does not have one of the fields mentioned above **/
        catch (JSONException e) {
            final String TAG = "toJSON";
            Log.w(TAG, "failed: User JSON");
            e.printStackTrace();
        }

        return userJSON;
    }

    /* Method Name: deleteMatchJSON
       Description: Creates JSON that contains Orbit Information to delete
       Params: String rideRequestId - Ride Request ID to be deleted
       Return: JSONObject deleteRRJSON
    */
    public static JSONObject deleteMatchJSON(String rideRequestId) {
        JSONObject deleteRRJSON = new JSONObject();

        /** Attempts to create deleteRRJSON **/
        try {
            deleteRRJSON.put("rideRequestId", rideRequestId);
        }

        /** JSONObject does not have one of the fields mentioned above **/
        catch (JSONException e) {
            final String TAG = "toJSON";
            Log.w(TAG, "failed: deleteRR JSON");
            e.printStackTrace();
        }

        return deleteRRJSON;
    }

    /* Method Name: deleteRideRequestJSON
       Description: Creates JSON that contains Orbit Information to delete
       Params: String userId - User ID
               String eventId - event ID to be deleted
               String rideRequestId - Ride Request ID to be deleted
       Return: JSONObject deleteRRJSON
    */
    public static JSONObject deleteRideRequestJSON(String userId, String eventId, String rideRequestId) {
        JSONObject deleteRRJSON = new JSONObject();

        /** Attempts to create deleteRRJSON **/
        try {
            deleteRRJSON.put("userId", userId);
            deleteRRJSON.put("eventId", eventId);
            deleteRRJSON.put("rideRequestId", rideRequestId);
            Log.w("TAGGIBOI", deleteRRJSON.toString());

        }

        /** JSONObject does not have one of the fields mentioned above **/
        catch (JSONException e) {
            final String TAG = "toJSON";
            Log.w(TAG, "failed: deleteRR JSON");
            e.printStackTrace();
        }

        return deleteRRJSON;
    }
}

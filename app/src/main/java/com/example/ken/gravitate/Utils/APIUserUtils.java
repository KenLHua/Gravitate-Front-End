package com.example.ken.gravitate.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class APIUserUtils {
    public static String getUserURL( String uid ) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .path("https://gravitate-e5d01.appspot.com/users")
                .appendPath(uid);

        return builder.toString();
    }

    public static void postUser(final Context loginScreen, JSONObject userJSON) {
        final String server_url = "https://gravitate-e5d01.appspot.com/user";
        final String TAG = "User";
        // Formulate the request and handle the response.
        Log.w(TAG, "REQUEST:Attempt to create User Object");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, server_url, userJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with the response
                        Log.w(TAG, "POST_REQUEST: User JSON Sent");
                        Toast.makeText(loginScreen,"Registration Success", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(loginScreen,"Registration Failed", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
        APIRequestSingleton.getInstance(loginScreen).addToRequestQueue(jsonObjectRequest, "postRequest");
    }
}

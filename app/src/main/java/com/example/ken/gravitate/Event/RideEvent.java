package com.example.ken.gravitate.Event;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ken.gravitate.Models.AirportRide;
import com.example.ken.gravitate.Models.AirportRideRequest;
import com.example.ken.gravitate.Models.Orbit;
import com.example.ken.gravitate.Models.Rider;
import com.example.ken.gravitate.OrbitMemberAdapter;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.RiderAdapter;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.ResourcePath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

// uber sdk for uber button
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.error.ApiError;

public class RideEvent extends AppCompatActivity {
    private TextView mPartnerName;
    private TextView mPartnerEmail;
    private Context mCtx;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_event);

        mCtx = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ride Details");
        toolbar.setNavigationIcon(R.drawable.system_icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        setSupportActionBar(toolbar);

        // Getting REST access token
        Task<GetTokenResult> tokenTask = FirebaseAuth.getInstance().getAccessToken(false);
        while(!tokenTask.isComplete()){
            Log.d("GettingToken", "async");
            synchronized (this){
                try{
                    wait(500);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        final String token = tokenTask.getResult().getToken();

        db = FirebaseFirestore.getInstance();
        final String rideRequestId = getIntent().getExtras().getString("rideRequestId");
        APIUtils.getRideRequest(mCtx, rideRequestId, token,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        JSONObject response = result;
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            AirportRide rideRequest = mapper.readValue(result.toString(), AirportRide.class);
                            /* Check whether the rideRequest is in an orbit */
                            Boolean isComplete = rideRequest.isRequestCompletion();

                            /* Get variables required to send REST API request for deletion */
                            String eventId = rideRequest.getEventId();
                            String userId = rideRequest.getUserId();

                            /* Hide or display remove button */
                            Boolean hideRemoveButton = isComplete;
                            updateRemoveButton(eventId, rideRequestId, userId, hideRemoveButton);

                            /* Update ride request related information */
                            updateRideRequest(rideRequest);

                            /* Update orbit related information (if applicable) */
                            if (isComplete) {
                                DocumentReference orbitRef = db.collection("orbits").document(rideRequest.getOrbitId());
                                orbitRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Orbit orbit = documentSnapshot.toObject(Orbit.class);
                                        updateOrbitExists(orbit);
                                    }
                                });
                            } else {
                                updateOrbitNone();
                            }
                        } catch (Exception e) {
                            Log.e("json exception", e.toString());
                        }
//
//                        flightLocalTime = response.getString("flightLocalTime");
//                        pickupAddress = response.getString("pickupAddress");
//                        baggages = response.getString("baggages");

                        Log.w("GETRideRequest", "Success");
                    }
                });

        SessionConfiguration config = new SessionConfiguration.Builder()
                // mandatory
                .setClientId("pF6iOhHQDz82jSBQ3q1Am-ZbI_t8IZZR")
                // required for enhanced button features
                .setServerToken("4D4ljF4x6hf97r92tXYXMBw3tih1QljnAB36ruFY")
                // required for implicit grant authentication
//                .setRedirectUri("<REDIRECT_URI>")
                // optional: set sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();

        // initialize uber sdk
        UberSdk.initialize(config);
        RideRequestButton requestButton = (RideRequestButton) findViewById(R.id.uber_bttn);


        RideParameters rideParams = new RideParameters.Builder()
                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
                .setDropoffLocation(
                        37.775304, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
                .setPickupLocation(37.775304, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                .build();


        ServerTokenSession session = new ServerTokenSession(config);

        // callback functions allow us to be notified on successfully loaded estimates,
        // API errors, and unexpected errors (e.g. IOException)
        RideRequestButtonCallback callback = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {
                // react to the displayed estimates
                Log.i("myTag2", "loading info");
            }

            @Override
            public void onError(ApiError apiError) {
                // API error details: /docs/riders/references/api#section-errors
                Log.e("myTag", apiError.toString());
            }



            @Override
            public void onError(Throwable throwable) {
                // Unexpected error, very likely an IOException
                Log.e("myTag", throwable.getMessage());
            }
        };

        // set parameters for the RideRequestButton instance
        requestButton.setRideParameters(rideParams);
        requestButton.setSession(session);
        requestButton.setCallback(callback);
        requestButton.loadRideInformation(); // fetch and display estimates within the button
    }


    private void updateOrbitExists(Orbit orbit) {
        RecyclerView recyclerView = findViewById(R.id.rider_list);

        final List<Rider> rider_list = new ArrayList<Rider>();
        Rider riderCard = new Rider(R.drawable.default_profile, "Name", "Email");
        rider_list.add(riderCard);

        OrbitMemberAdapter adapter = new OrbitMemberAdapter(this, orbit,rider_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void updateOrbitNone() {
        final List<Rider> rider_list = new ArrayList<Rider>();
        Rider riderCard = new Rider(R.drawable.default_profile, "Name", "Email");
        rider_list.add(riderCard);

        RecyclerView recyclerView = findViewById(R.id.rider_list);
        OrbitMemberAdapter adapter = new OrbitMemberAdapter(this,null,rider_list);
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateRideRequest(AirportRide rideRequest) {

        TextView flightTimeDisplay = findViewById(R.id.flightTime);
        TextView pickupAddressDisplay = findViewById(R.id.pickupAddress);

        String pickupAddress = rideRequest.getPickupAddress();
        String flightTime = rideRequest.getFlightLocalTime();

        flightTimeDisplay.setText("Flight Time : " + flightTime);
        pickupAddressDisplay.setText("Pickup Address : " + pickupAddress);

    }

    private void updateRemoveButton(final String eventId, final String rideRequestId, final String userId, Boolean isHidden) {

        // Getting REST access token
        Task<GetTokenResult> tokenTask = FirebaseAuth.getInstance().getAccessToken(false);
        while(!tokenTask.isComplete()){
            Log.d("GettingToken", "async");
            synchronized (this){
                try{
                    wait(500); // TODO: change
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }

        final String token = tokenTask.getResult().getToken();

        Button deleteRideButton = findViewById(R.id.delete_ride_bttn);

        if (isHidden) {
            /* Set button to be hidden and return */
            deleteRideButton.setVisibility(View.GONE);
            return;
        }

        deleteRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                APIUtils.postDeleteRideRequest(mCtx,token,new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        Toast.makeText(mCtx, "Ride Request Deleted", Toast.LENGTH_LONG).show();
                        ((Activity)mCtx).finish();
                        Intent intent = new Intent (mCtx, ScheduledEvents.class);
                        startActivity(intent);
                    }
                },userId,eventId,rideRequestId);
            }
        });

    }
//
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }

    private DocumentReference getDocRef(String pathString) {
        /*
        Helper class for converting from reference string to DocumentReference
        (reference string example: "/rideRequests/testrid1" )
         */
        ResourcePath p = ResourcePath.fromString(pathString);
        return DocumentReference.forPath(p, db);
    }

}


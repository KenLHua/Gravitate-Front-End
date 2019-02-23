package com.example.ken.gravitate.Event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ken.gravitate.Models.AirportRideRequest;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

public class RequestCreated extends AppCompatActivity {

    // UI variables
    Context mContext;
    TextView mEarliestTime;
    TextView mLatestTime;
    TextView mAirport;
    TextView mFlightTime;
    TextView mPickupAddress;
    TextView mDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.created_request_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ride Request Details");
        toolbar.setNavigationIcon(R.drawable.system_icon_back);
        // Setting toolbar back button behavior
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


        // Getting UI elements
        mContext = RequestCreated.this;

        String id = getIntent().getStringExtra("id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("rideRequests").document(id);

        // TODO: Send GET Request to populate request details //
        APIUtils.getRideRequest(mContext, id, token,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        JSONObject response = result;
                        updateUI(response);
                        Log.w("GETRideRequest", "Success");
                    }
                });

//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                AirportRideRequest rideRequest = documentSnapshot.toObject(AirportRideRequest.class);
//
//                final Map<String, Object > target = rideRequest.getTarget();
//                final Map<String, Object> arriveAtEventTime = (Map<String, Object>) target.get("arriveAtEventTime");
//
//                final Long earliestTimestamp = (Long) arriveAtEventTime.get("earliest");
//                String earliestTime = formatTime(earliestTimestamp);
//
//                final Long latestTimestamp = (Long) arriveAtEventTime.get("latest");
//                String latestTime = formatTime(latestTimestamp);
//
//
//                String flightLocalTime = rideRequest.getFlightLocalTime();
//                DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
//                LocalDateTime localDateTime = LocalDateTime.parse(flightLocalTime, timeFormatter);
//                String flightTime = localDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
//                String date = localDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
//                String pickupAddress = rideRequest.getPickupAddress();
//
//                // Infer airportCode from context (TODO: retrieve from rideRequest.eventRef)
//                String airport = getIntent().getStringExtra("airportCode");
//
//                // Setting information for the user to use
//                mEarliestTime.setText(earliestTime);
//                mLatestTime.setText(latestTime);
//                mAirport.setText(airport);
//                mFlightTime.setText(flightTime);
//                mPickupAddress.setText(pickupAddress);
//                mDate.setText(date);
//
//                // Show the toolbar back button
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//            }
//        });

    }

    private void updateUI(JSONObject response) {
        String flightLocalTime;
//        String airport;
        String earliestTime;
        String latestTime;
        String date;
        String pickupAddress;
        String baggages;

        // Infer airportCode from context (TODO: retrieve from rideRequest.eventRef)
        String airport = getIntent().getStringExtra("airportCode");
        try{
            flightLocalTime = response.getString("flightLocalTime");
            pickupAddress = response.getString("pickupAddress");
            baggages = response.getString("baggages");

            JSONObject times = response.getJSONObject("target").getJSONObject("arriveAtEventTime");
            earliestTime = times.getString("latest");
            latestTime = times.getString("earliest");
            setUIElements(earliestTime, latestTime, airport, flightLocalTime, pickupAddress, baggages);

        }catch(JSONException e){
            Log.w("RideRequest GET: JSONObject read incorrectly", e.toString());
        }
    }

    private void setUIElements( final String earliestTime, final String latestTime, final String airport,
                                final String flightLocalTime, final String pickupAddress, final String baggages) {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(flightLocalTime, timeFormatter);
        String flightTime = localDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        final String date = localDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));

        mEarliestTime = findViewById(R.id.earlyArrivalTime);
        mLatestTime = findViewById(R.id.latestArrivalTime);
        mAirport = findViewById(R.id.airportName);
        mFlightTime = findViewById(R.id.flightTime);
        mPickupAddress = findViewById(R.id.pickupAddress);
        mDate = findViewById(R.id.Date);
        mEarliestTime.setText(earliestTime);
        mLatestTime.setText(latestTime);
        mAirport.setText(airport);
        mFlightTime.setText(flightTime);
        mPickupAddress.setText(pickupAddress);
        mDate.setText(date);
        Log.w("GETRideRequest", "Set UI Success");
    }

    private String formatTime(Long startTimestamp) {
        final ZoneId zoneId = ZoneId.of("America/Los_Angeles");
        final ZonedDateTime eventDate = ZonedDateTime.ofInstant(Instant.ofEpochSecond(startTimestamp), zoneId);
        // TODO: test all operations involved in Datetime conversion
        final String eventTime = eventDate.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        return eventTime;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ScheduledEvents.class);
        startActivity(intent);
        super.onBackPressed();
    }
}

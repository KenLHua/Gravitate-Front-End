package com.example.ken.gravitate.Event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ken.gravitate.Models.AirportRideRequest;
import com.example.ken.gravitate.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

public class RequestCreated extends AppCompatActivity {

    // UI variables
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

        String id = getIntent().getStringExtra("id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("rideRequests").document(id);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AirportRideRequest rideRequest = documentSnapshot.toObject(AirportRideRequest.class);

                final Map<String, Object > target = rideRequest.getTarget();
                final Map<String, Object> arriveAtEventTime = (Map<String, Object>) target.get("arriveAtEventTime");

                final Long earliestTimestamp = (Long) arriveAtEventTime.get("earliest");
                String earliestTime = formatTime(earliestTimestamp);

                final Long latestTimestamp = (Long) arriveAtEventTime.get("latest");
                String latestTime = formatTime(latestTimestamp);


                String flightLocalTime = rideRequest.getFlightLocalTime();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime localDateTime = LocalDateTime.parse(flightLocalTime, timeFormatter);
                String flightTime = localDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
                String date = localDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
                String pickupAddress = rideRequest.getPickupAddress();

                // Infer airportCode from context (TODO: retrieve from rideRequest.eventRef)
                String airport = getIntent().getStringExtra("airportCode");

                // Getting UI elements
                mEarliestTime = findViewById(R.id.earlyArrivalTime);
                mLatestTime = findViewById(R.id.latestArrivalTime);
                mAirport = findViewById(R.id.airportName);
                mFlightTime = findViewById(R.id.flightTime);
                mPickupAddress = findViewById(R.id.pickupAddress);
                mDate = findViewById(R.id.Date);

                // Setting information for the user to use
                mEarliestTime.setText(earliestTime);
                mLatestTime.setText(latestTime);
                mAirport.setText(airport);
                mFlightTime.setText(flightTime);
                mPickupAddress.setText(pickupAddress);
                mDate.setText(date);

                // Show the toolbar back button
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            }
        });

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

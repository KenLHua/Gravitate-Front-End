package com.example.ken.gravitate.Event;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ken.gravitate.R;

public class CreatedRequestDetails extends AppCompatActivity {

    TextView mEarliestTime;
    TextView mLatestTime;
    TextView mAirport;
    TextView mFlightTime;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.created_request_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ride Request Details");
        toolbar.setNavigationIcon(R.drawable.system_icon_back);
        setSupportActionBar(toolbar);
        String earliestTime = getIntent().getStringExtra("earliestTime");
        String latestTime = getIntent().getStringExtra("latestTime");
        String airport = getIntent().getStringExtra("airportCode");
        String flightTime = getIntent().getStringExtra("flightTime");


        mEarliestTime = findViewById(R.id.earlyArrivalTime);
        mLatestTime = findViewById(R.id.latestArrivalTime);
        mAirport = findViewById(R.id.airportName);
        mFlightTime = findViewById(R.id.flightTime);

        mEarliestTime.setText(earliestTime);
        mLatestTime.setText(latestTime);
        mAirport.setText(airport);
        mFlightTime.setText(flightTime);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

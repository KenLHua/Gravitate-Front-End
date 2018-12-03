package com.example.ken.gravitate.Event;

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
import android.widget.TextView;

import com.example.ken.gravitate.Models.Rider;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.RiderAdapter;

import java.util.ArrayList;
import java.util.List;

public class RideEvent extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_event);

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
        RecyclerView recyclerView = findViewById(R.id.rider_list);
        final List<Rider> rider_list = new ArrayList<Rider>();
        rider_list.add(new Rider(R.drawable.default_profile, "Tyler", "test.ucsd.edu"));
        RiderAdapter adapter = new RiderAdapter(this,rider_list);

        TextView flightTimeDisplay = findViewById(R.id.flightTime);
        String flightTime = getIntent().getStringExtra("flightTime");
        flightTimeDisplay.setText(flightTime);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
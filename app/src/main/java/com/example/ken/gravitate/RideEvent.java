package com.example.ken.gravitate;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RideEvent extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_event);
        RecyclerView recyclerView = findViewById(R.id.rider_list);
        final List<Rider> rider_list = new ArrayList<Rider>();
        rider_list.add(new Rider(R.drawable.default_profile, "Tyler", "test.ucsd.edu"));

        rider_list.add(new Rider(R.drawable.default_profile, "Tyler1", "test.ucsd.edu"));

        RiderAdapter adapter = new RiderAdapter(this,rider_list);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

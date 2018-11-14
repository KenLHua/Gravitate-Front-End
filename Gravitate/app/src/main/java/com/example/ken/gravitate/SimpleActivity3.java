package com.example.ken.gravitate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.module.AirportRideRequest;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.ButterKnife;

public class SimpleActivity3 extends AppCompatActivity {

    //    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //    @BindView(R.id.airport_ride_request_list)
    RecyclerView airportRideRequestList;


    private FirebaseFirestore db;

    private static final String TAG = "SimpleActivity3";

    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_activity_3);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SimpleActivity3");
        setSupportActionBar(toolbar);

        init();
        getAirportRideRequestList();
    }

    private void init() {
        airportRideRequestList = findViewById(R.id.airport_ride_request_list_k);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        airportRideRequestList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getAirportRideRequestList() {
        Query query = FirebaseFirestore.getInstance()
                .collection("rideRequests")
                .orderBy("hasCheckedIn")
                .limit(50);


        FirestoreRecyclerOptions<AirportRideRequest> options =
                new FirestoreRecyclerOptions
                        .Builder<AirportRideRequest>()
                        .setQuery(query, AirportRideRequest.class)
                        .build();


        adapter = new FirestoreRecyclerAdapter<AirportRideRequest, AirportRideRequestHolder>(options) {
            @Override
            public void onBindViewHolder(AirportRideRequestHolder holder, int position, AirportRideRequest model) {
                // TODO Bind the AirportRideRequest object to the AirportRideRequestHolder

                holder.textName.setText(model.getFlightLocalTime());

                Log.d(TAG, model.getFlightNumber());

            }

            @Override
            public AirportRideRequestHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.ride_request_text, group, false);

                return new AirportRideRequestHolder(view);
            }

        };

        adapter.notifyDataSetChanged();

        airportRideRequestList.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class AirportRideRequestHolder extends RecyclerView.ViewHolder {

        TextView textName;


        public AirportRideRequestHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.ride_request_name);
        }

    }


}



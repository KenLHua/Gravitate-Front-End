package com.example.ken.gravitate.Event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

//Necessary libraries for Address Autocomplete functionality
import com.example.ken.gravitate.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class InputPickupAddress extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_pickup_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("InputPickupAddress");
        setSupportActionBar(toolbar);

        final TextView inputPickup = findViewById(R.id.inputPickup);
        final ImageButton pickupClear = findViewById(R.id.clear_pickup_button);


        //Note: Address Autocomplete sorts address priority by user location/proximity.

        //Initialize Address Autocomplete Fragment
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        //Limit search to addresses in United States only, without the filter the autocomplete will
        //display results from different countries
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("us")
                .build();
        autocompleteFragment.setFilter(filter);
        autocompleteFragment.setHint("Pickup Address");
        pickupClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPickup.setText("");
            }
        });

        //Listener to store selected address and display on screen
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                inputPickup.setText(place.getAddress());
            }
            @Override
            public void onError(Status status) {
                inputPickup.setText(status.toString());
            }
        });

    }




}


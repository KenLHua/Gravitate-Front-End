package com.example.ken.gravitate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

//Necessary libraries for Address Autocomplete functionality
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
        final TextView txtVw = findViewById(R.id.placeName);

        //Note: Address Autocomplete sorts address priority by user location/proximity.

        //Initialize Address Autocomplete Fragment
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        //Limit search to addresses in United States only, without the filter the autocomplete will
        //display results from different countries
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("us")
                .build();
        autocompleteFragment.setFilter(filter);

        //Listener to store selected address and display on screen
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                txtVw.setText("Pick-Up Address: \n"+place.getName());
            }
            @Override
            public void onError(Status status) {
                txtVw.setText(status.toString());
            }
        });
    }




}


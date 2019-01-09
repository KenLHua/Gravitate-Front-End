package com.example.ken.gravitate.Event;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIRequestSingleton;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.DateAndTimePickerAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import java.util.Calendar;

//Necessary libraries for Address Autocomplete functionality

public class CreateEventRide extends AppCompatActivity {
    // Autrocomplete Request Code
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    private Button mFlightStats_Bttn;

    private TextInputEditText mPickUpAddress;
    private boolean toEvent = true;
    private RequestQueue mRequestQueue;
    private TextView mflightCarrier;
    private TextView mflightNum;
    private TextView inputDepartureDate;
    private TextView mEventId;

    //The pickupAddress text
    private TextView inputPickup;
    private Context mContext;

    private Calendar cal;


    /**** TESTING ****/
    private TextView mOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setting activity UI elements
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_ride);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.input_flight_toolbar);
        toolbar.setNavigationIcon(R.drawable.system_icon_back);



        setSupportActionBar(toolbar);

        //Limit search to addresses in United States only, without the filter the autocomplete will
        //display results from different countries
        final AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("us")
                .build();

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


        mContext = this;
        mOutput = findViewById(R.id.outputText);

        // For displaying eventId for debugging purposes
        mEventId = findViewById(R.id.eventId);

        // Creating input TextFields
        mflightCarrier = findViewById(R.id.inputFlightCarrier);
        mflightNum = findViewById(R.id.inputFlightNumber);

        // Creating input TextFields
        inputPickup = findViewById(R.id.inputPickup);
        mflightCarrier = findViewById(R.id.inputFlightCarrier);
        mflightNum = findViewById(R.id.inputFlightNumber);

        // Clears the pickup Text Box using the X
        final ImageButton pickupClear = findViewById(R.id.clear_pickup_button);
        pickupClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPickup.setText("");
            }
        });
        // Clicking on the Text Box lets Google's autocomplete do the work
        inputPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPlaceAutocompleteActivityIntent(filter); }});

        // Setting timepickers
        int year, month, day, hour, min;
        cal = Calendar.getInstance();

        inputDepartureDate = findViewById(R.id.inputDepartureTime);
        // Set a date and time picker to update inputDepartureDate
        // Once inputDepartureDate is updated, also update earlyArrivalTime with a time 4 hours before
        final DateAndTimePickerAdapter datePicker = new DateAndTimePickerAdapter(cal,inputDepartureDate,
                CreateEventRide.this, false);


        final String eventId = getIntent().getExtras().getString("eventId");
        mEventId.setText(eventId);

    }


    // Calling the PlaceAutoComplete activity
    private void callPlaceAutocompleteActivityIntent( AutocompleteFilter filter){
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(filter)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }
    }
    // The deal with the actions done at the autocomplete activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                inputPickup.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                inputPickup.setText("");
                Log.i("Autocomplete Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


}
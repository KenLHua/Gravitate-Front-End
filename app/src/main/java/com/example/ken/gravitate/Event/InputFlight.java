package com.example.ken.gravitate.Event;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.DateAndTimePickerAdapter;

import android.content.Context;
import android.app.DatePickerDialog;
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
import com.example.ken.gravitate.Utils.APIRequestSingleton;
import com.example.ken.gravitate.R;

//Necessary libraries for Address Autocomplete functionality
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

public class InputFlight extends AppCompatActivity {
    // Autrocomplete Request Code
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    private Button mFlightStats_Bttn;

    private TextInputEditText mPickUpAddress;
    private boolean toEvent = true;
    private RequestQueue mRequestQueue;
    private TextView mflightCarrier;
    private TextView mflightNum;
    private TextView inputDepartureDate;

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
        setContentView(R.layout.input_flight_information);
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
<<<<<<< HEAD
            try{
                wait(500);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        final String token = tokenTask.getResult().getToken();
//        APIUtils.testAuthEndpoint(this,token);
=======
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

>>>>>>> fb9e46d5bbee9e97dad7ea3a041f719930ffb614

        mContext = this;
        mOutput = findViewById(R.id.outputText);

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
                InputFlight.this, false);

        // Popup date picker
        inputDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the datePicker window
                cal.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
                DatePickerDialog dateDialog = new DatePickerDialog(InputFlight.this, datePicker.getDateListener(),
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                // Set the minimum date to be today
                dateDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                // Show the window
                dateDialog.show();
            }
        });



        // Setting Flightstats Bttn
        mFlightStats_Bttn = findViewById(R.id.flightStats_bttn);

        // Lookup flight information using flight stats api
        mFlightStats_Bttn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.flightStats_bttn:

                        String flightDate = inputDepartureDate.getText().toString();
                        // Checking if all necessary inputs are given, if not return and give a error
                        if (invalidFlightFields(mflightCarrier.getText().toString(),
                                mflightNum.getText().toString(),
                                flightDate,
                                inputPickup.getText().toString())) return;

                        // Request_URL takes in ("Carrier", "Flight Number", "YEAR", "MONTH", "DATE")
                        String request_url = APIUtils.getFSScheduleURL(
                                mflightCarrier.getText().toString(),mflightNum.getText().toString(),
                                flightDate.substring(6, flightDate.length())
                                ,flightDate.substring(0,2)
                                ,flightDate.substring(3,5));

                        APIUtils.getFlightStats(mContext, request_url,inputPickup.getText().toString() , toEvent, flightDate, mOutput, token);
                        break;
                }
            }
        });

        // Initializing Request Components
        mRequestQueue = APIRequestSingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
    }



    // Checks if all flight input fields are filled
    private boolean invalidFlightFields(String flightCarrier, String flightNum, String flightDate, String pickupAddress) {
        if(flightCarrier.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input the flight carrier", Toast.LENGTH_LONG).show();
            return true;
        }
        if(flightNum.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input the flight number", Toast.LENGTH_LONG).show();
            return true;
        }
        if(flightDate.length() == 0 ){
            Toast.makeText(mContext, "Error: Please choose a date", Toast.LENGTH_LONG).show();
            return true;
        }
        if(pickupAddress.length() == 0 ){
            Toast.makeText(mContext, "Error: Please choose a pickup address", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
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
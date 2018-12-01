package com.example.ken.gravitate.Event;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.DateAndTimePickerAdapter;

import android.content.Context;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.ken.gravitate.Utils.APIRequestSingleton;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.JSONUtils;

//Necessary libraries for Address Autocomplete functionality
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.Calendar;

public class InputFlight extends AppCompatActivity {
    // Autrocomplete Request Code
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // Radio Group Filters
    private RadioGroup inputGroup;
    private RadioButton flightRadio;
    private RadioButton manualRadio;

    private Button mFlightStats_Bttn;

    private TextInputLayout flightNumberTextDisplay;
    private TextInputLayout manualTimeDisplay;
    private TextInputLayout manualFlightAddress;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_flight_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.input_flight_toolbar);
        setSupportActionBar(toolbar);



        mContext = this;
        mOutput = findViewById(R.id.outputText);

        // Creating input TextFields
        mflightCarrier = findViewById(R.id.inputFlightCarrier);
        mflightNum = findViewById(R.id.inputFlightNumber);

        // Initializing place autocompletion
        final ImageButton pickupClear = findViewById(R.id.clear_pickup_button);
        //Limit search to addresses in United States only, without the filter the autocomplete will
        //display results from different countries
        final AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("us")
                .build();

        // Setting Radio Buttons
        inputGroup = (RadioGroup) findViewById(R.id.flightRadioGroup);
        flightRadio = (RadioButton) findViewById(R.id.flightRadio);
        manualRadio = (RadioButton) findViewById(R.id.manualRadio);

        // Creating input TextFields
        inputPickup = findViewById(R.id.inputPickup);
        mflightCarrier = findViewById(R.id.inputFlightCarrier);
        mflightNum = findViewById(R.id.inputFlightNumber);
        mPickUpAddress = findViewById(R.id.inputFlightAddress);

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
        TextView inputArrivalTime = findViewById(R.id.inputArrivalTime);
        // Set a date and time picker to update inputDepartureDate
        // Once inputDepartureDate is updated, also update inputArrivalTime with a time 4 hours before
        final DateAndTimePickerAdapter datePicker = new DateAndTimePickerAdapter(cal,inputDepartureDate,
                InputFlight.this, false);

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

        mFlightStats_Bttn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.flightStats_bttn:
                        /*** TEST CODE ****/
/*                        String request_url = APIUtils.getFSScheduleURL("DL", "89",
                                "2019", "5", "2");*/

                        /****** ACTUAL CODE ****/
                        String flightDate = inputDepartureDate.getText().toString();
                        // Request_URL = ("Carrier", "Flight Number", "YEAR", "MONTH", "DATE")
                        String request_url = APIUtils.getFSScheduleURL(
                                mflightCarrier.getText().toString(),mflightNum.getText().toString(),
                                flightDate.substring(6, flightDate.length())
                                ,flightDate.substring(0,2)
                                ,flightDate.substring(3,5));

                        APIUtils.getFlightStats(mContext,request_url, inputPickup.getText().toString(),toEvent,mOutput);
                        break;
                }
            }
        });




        // Setting Text Fields
        flightNumberTextDisplay = (TextInputLayout) findViewById(R.id.flightNumber);
        manualTimeDisplay = (TextInputLayout) findViewById(R.id.manualTime);
        manualFlightAddress = (TextInputLayout) findViewById(R.id.manualFlightAddress);

/*        // Initializing Request Components
        mRequestQueue = getRequestQueue();*/

        mRequestQueue = APIRequestSingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
    }
    // Calling the PlaceAutoComplete activity
    private void callPlaceAutocompleteActivityIntent( AutocompleteFilter filter){
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(filter)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
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
                // TODO: Handle the error.
                inputPickup.setText("");
                Log.i("Autocomplete Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    // Add Checkmark to the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inputflight_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // The CheckMark in this case
            case R.id.send_request_checkmark:
                //  TODO : Double check that all text boxes are filled before sending the request
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
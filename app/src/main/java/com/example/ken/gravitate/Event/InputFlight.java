package com.example.ken.gravitate.Event;

<<<<<<< HEAD
import android.content.Intent;
=======
import android.content.DialogInterface;
>>>>>>> MaterialSettings
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ken.gravitate.Utils.APIRequestSingleton;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.JSONUtils;

<<<<<<< HEAD
//Necessary libraries for Address Autocomplete functionality
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
=======
import org.json.JSONObject;
>>>>>>> MaterialSettings

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
    private TextInputEditText mflightNum;
    private TextInputEditText mflightCarrier;
    private TextInputEditText mflightYear;
    private TextInputEditText mflightMonth;
    private TextInputEditText mflightDay;
    private TextInputEditText mPickUpAddress;
    private boolean toEvent = true;
    private RequestQueue mRequestQueue;

    //
    private TextView inputPickup;



    /**** TESTING ****/
    private TextView mOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_flight_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.input_flight_toolbar);
        setSupportActionBar(toolbar);

        // Initializing place autocompletion
        inputPickup = findViewById(R.id.inputPickup);
        final ImageButton pickupClear = findViewById(R.id.clear_pickup_button);
        //Limit search to addresses in United States only, without the filter the autocomplete will
        //display results from different countries
        final AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("us")
                .build();



        // Creating input TextFields
        mflightCarrier = findViewById(R.id.inputFlightCarrier);
        mflightNum = findViewById(R.id.inputFlightNumber);
        mflightYear = findViewById(R.id.inputFlightYear);
        mflightMonth = findViewById(R.id.inputFlightMonth);
        mflightDay = findViewById(R.id.inputFlightDay);
        mPickUpAddress = findViewById(R.id.inputFlightAddress);

        // Clears the pickup Text Box using the X
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
                callPlaceAutocompleteActivityIntent(filter);
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
                        String request_url = APIUtils.getFSScheduleURL(
                                mflightCarrier.getText().toString(),mflightNum.getText().toString(),
                                mflightYear.getText().toString(),mflightMonth.getText().toString(),
                                mflightDay.getText().toString());


                        getFlightStats(request_url);
                        break;
                }
            }
        });

        // Setting Radio Buttons
        inputGroup = (RadioGroup) findViewById(R.id.flightRadioGroup);
        flightRadio = (RadioButton) findViewById(R.id.flightRadio);
        manualRadio = (RadioButton) findViewById(R.id.manualRadio);

        // Setting Text Fields
        flightNumberTextDisplay = (TextInputLayout) findViewById(R.id.flightNumber);
        manualTimeDisplay = (TextInputLayout) findViewById(R.id.manualTime);
        manualFlightAddress = (TextInputLayout) findViewById(R.id.manualFlightAddress);

/*        // Initializing Request Components
        mRequestQueue = getRequestQueue();*/

        mRequestQueue = APIRequestSingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        // Setting Radio hide/show behavior
        flightRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (flightRadio.isChecked()) {
                    hideManualInput();
                } else {
                    showManualInput();
                }
            }
        });
        inputGroup.check(R.id.flightRadio);
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

    // **Incomplete** Add Checkmark to the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    // Helper method to make hide more readable
    public void hideManualInput() {
        flightNumberTextDisplay.setVisibility(View.VISIBLE);
        manualTimeDisplay.setVisibility(View.GONE);
        manualFlightAddress.setVisibility(View.GONE);

    }

    // Helper method to make show more readable
    public void showManualInput() {
        flightNumberTextDisplay.setVisibility(View.GONE);
        manualTimeDisplay.setVisibility(View.VISIBLE);
        manualFlightAddress.setVisibility(View.VISIBLE);
    }

    /* Sends a GET Request to Flightstats API
     *  RETURNS: String in JSON format that contains flight information
     * */
    private void getFlightStats(String request_url) {

        final String TAG = "FlightStatsAPI";
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        JSONObject Ride_Request = JSONUtils.retrieveFSInfo(response, mPickUpAddress.getText().toString(),toEvent);
                        postRideRequest(Ride_Request);
                        mOutput.setText(Ride_Request.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.w(TAG, "GET_REQUEST: FlightStatsAPI failure");
                    }
                });
        APIRequestSingleton.getInstance(this).addToRequestQueue(stringRequest,"getRequest");
    }

    private void postRideRequest(JSONObject Ride_RequestJSON) {
        final String server_url = "https://gravitate-e5d01.appspot.com/rideRequests";
        final String TAG = "Ride_Request";
        // Formulate the request and handle the response.
        Log.w(TAG, "REQUEST:Attempt to create jsonObjectRequest");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, server_url, Ride_RequestJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with the response
                        Log.w(TAG, "POST_REQUEST:Create Ride Request success");
                        Toast.makeText(InputFlight.this,"Success", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(InputFlight.this,"Error...", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
                APIRequestSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest, "postRequest");


    }

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
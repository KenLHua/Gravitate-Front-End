package com.example.ken.gravitate.Event;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ken.gravitate.Utils.APIRequestSingleton;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIUtils;

public class InputFlight extends AppCompatActivity {
    private RadioGroup inputGroup;
    private RadioButton flightRadio;
    private RadioButton manualRadio;
    private TextInputLayout flightNumberTextDisplay;
    private TextInputLayout manualTimeDisplay;
    private TextInputLayout manualFlightAddress;
    private TextInputEditText mflightNum;
    private TextInputEditText mflightCarrier;
    private TextInputEditText mflightYear;
    private TextInputEditText mflightMonth;
    private TextInputEditText mflightDay;
    private RequestQueue mRequestQueue;

    /**** TESTING ****/
    private TextView mOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_flight_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.input_flight_toolbar);
        setSupportActionBar(toolbar);

        // Creating input TextFields

        /**************************************************************/

        /**** Testing ****/
        mOutput = findViewById(R.id.output);


        /**** Actual Code ****/
/*      mflightCarrier = findViewById(R.id.inputFlightCarrier);
        mflightNum = findViewById(R.id.inputFlightNumber);
        mflightYear = findViewById(R.id.inputFlightYear);
        mflightMonth = findViewById(R.id.inputFlightMonth);
        mflightDay = findViewById(R.id.inputFlightDay);*/

        /**************************************************************/


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

    ;

    // Helper method to make show more readable
    public void showManualInput() {
        flightNumberTextDisplay.setVisibility(View.GONE);
        manualTimeDisplay.setVisibility(View.VISIBLE);
        manualFlightAddress.setVisibility(View.VISIBLE);
    }

    ;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:

                /*** TEST CODE ****/
/*                String request_url = getFSScheduleURL("DL","89",
                        "2019","5","2");*/
                String request_url = APIUtils.getFSScheduleURL("DL", "89",
                        "2019", "5", "2");

                /****** ACTUAL CODE ****/
/*                String request_url = getFSScheduleURL(mflightCarrier.toString(),mflightNum.toString(),
                        mflightYear.toString(),mflightMonth.toString(),mflightDay.toString());*/
                getFlightStats(request_url);


            default:
                return super.onOptionsItemSelected(item);
        }
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
                        Log.w(TAG, "GET_REQUEST:success");
                        mOutput.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.w(TAG, "GET_REQUEST:failure");
                    }
                });
        APIRequestSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
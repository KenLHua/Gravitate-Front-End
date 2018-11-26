package com.example.ken.gravitate.Event;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ken.gravitate.R;

import org.w3c.dom.Text;

public class InputFlight extends AppCompatActivity {
    private RadioGroup inputGroup;
    private RadioButton flightRadio;
    private RadioButton manualRadio;
    private TextInputLayout flightNumberTextDisplay;
    private TextInputLayout manualTimeDisplay;
    private TextInputLayout manualFlightAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_flight_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.input_flight_toolbar);
        setSupportActionBar(toolbar);

        // Creating input TextFields


        // Setting Radio Buttons
        inputGroup = (RadioGroup) findViewById(R.id.flightRadioGroup);
        flightRadio = (RadioButton) findViewById(R.id.flightRadio);
        manualRadio = (RadioButton) findViewById(R.id.manualRadio);
        // Setting Text Fields
        flightNumberTextDisplay = (TextInputLayout) findViewById(R.id.flightNumber);
        manualTimeDisplay = (TextInputLayout) findViewById(R.id.manualTime);
        manualFlightAddress = (TextInputLayout) findViewById(R.id.manualFlightAddress);


        // Setting Radio hide/show behavior
        flightRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flightRadio.isChecked()){
                    hideManualInput();
                }
                else {
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
    public void hideManualInput(){
        flightNumberTextDisplay.setVisibility(View.VISIBLE);
        manualTimeDisplay.setVisibility(View.GONE);
        manualFlightAddress.setVisibility(View.GONE);

    };
    // Helper method to make show more readable
    public void showManualInput(){
        flightNumberTextDisplay.setVisibility(View.GONE);
        manualTimeDisplay.setVisibility(View.VISIBLE);
        manualFlightAddress.setVisibility(View.VISIBLE);
    };
}
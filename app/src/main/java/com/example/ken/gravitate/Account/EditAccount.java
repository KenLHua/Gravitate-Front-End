package com.example.ken.gravitate.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIRequestSingleton;
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseUser;

public class EditAccount extends AppCompatActivity {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private Button mSaveEditAccountBtn;

    private TextView mFullName;
    private TextView mPhoneNumber;
    private TextView mEmailAddress;
    private TextView mPostalAddress;
    private Context mContext;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Account");
        toolbar.setNavigationIcon(R.drawable.system_icon_back);

        // Back button will go to previous page when clicked on
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });

        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.edit_account_fragment, new EditAccountFragment())
                .commit();
        FirebaseUser user = AuthSingleton.getInstance().getCurrentUser();

        String userEmail = user.getEmail();
        String userFullName = user.getDisplayName();
        String userPhoneNumber = user.getPhoneNumber();
        String userProfilePic  = user.getPhotoUrl().toString();

        mContext = EditAccount.this;
        mFullName = findViewById(R.id.inputFullName);
        mPhoneNumber = findViewById(R.id.inputPhoneNumber);
        mPostalAddress = findViewById(R.id.inputPostalAddress);

        // Display profile pic and autofill user info
        mFullName.setText(userFullName);
        mEmailAddress.setText(userEmail);
        mPhoneNumber.setText(userPhoneNumber);
        new DownloadImageTask((ImageView)findViewById(R.id.c_profile_pic)).execute(userProfilePic);

        //Limit search to addresses in United States only, without the filter the autocomplete will
        //display results from different countries
        final AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("us")
                .build();


        mPostalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPlaceAutocompleteActivityIntent(filter); }});

        
        /*mSaveEditAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.send_request_save:
                        String fullName = mFullName.getText().toString();
                        String phoneNumber = mPhoneNumber.getText().toString();
                        String emailAddress = mEmailAddress.getText().toString();
                        String postalAddress = mPostalAddress.getText().toString();
                    
                        // Checking if all necessary inputs are given, if not return and give a error
                        if (invalidAccountFields(fullName,
                                phoneNumber,
                                emailAddress,
                                postalAddress)) return;

                        // Request_URL = ("Full Name", "Phone Number", "Email Address", "Address")
                        *//*String request_url = APIUtils.getFSScheduleURL(
                                mflightCarrier.getText().toString(),mflightNum.getText().toString(),
                                flightDate.substring(6, flightDate.length())
                                ,flightDate.substring(0,2)
                                ,flightDate.substring(3,5));

                        APIUtils.getFlightStats(mContext,request_url, inputPickup.getText().toString(),toEvent,mOutput);
                        break;*//*
                        break;
                }
            }
        });*/

        /*// Initializing Request Components
        mRequestQueue = APIRequestSingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
*/

    }

    //  Add Save button to the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_actionbar_menu, menu);
        return true;
    }

    // Helper method for back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                mPostalAddress.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                mPostalAddress.setText("");
                Log.i("Autocomplete Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    // Checks if all flight input fields are filled
    private boolean invalidAccountFields(String checkFullName, String checkPhoneNumber, String checkEmailAddress, String checkPostalAddress) {
        if(checkFullName.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your full name", Toast.LENGTH_LONG).show();
            return true;
        }
        if(checkPhoneNumber.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your phone number", Toast.LENGTH_LONG).show();
            return true;
        }
        if(checkEmailAddress.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your email address", Toast.LENGTH_LONG).show();
            return true;
        }
        if(checkPostalAddress.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your address", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem button){
        switch(button.getItemId()){
            case R.id.send_request_save:

                String fullName = mFullName.getText().toString();
                String phoneNumber = mPhoneNumber.getText().toString();
                String emailAddress = mEmailAddress.getText().toString();
                String postalAddress = mPostalAddress.getText().toString();

                // Checking if all necessary inputs are given, if not return and give a error
                if (invalidAccountFields(fullName,
                        phoneNumber,
                        emailAddress,
                        postalAddress)) break;


                Toast.makeText(mContext, "Account Information Saved!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(button);
    }
}

package com.example.ken.gravitate.Account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.ken.gravitate.Event.ScheduledEvents;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIRequestSingleton;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmProfile extends AppCompatActivity {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private Button mSaveEditAccountBtn;
    private FirebaseAuth mAuth;
    private TextView mFullName;
    private TextView mPhoneNumber;
    private ImageView mPicture;
    private TextView mPostalAddress;
    private Context mContext;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_account_layout);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        checkUserExists(user);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Confirm Account");
//        toolbar.setNavigationIcon(R.drawable.system_icon_back);
//        // Back button will go to previous page when clicked on
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSupportNavigateUp();
//            }
//        });
        setSupportActionBar(toolbar);
        mContext = this;
        mFullName = findViewById(R.id.inputFullName);
        mPicture = findViewById(R.id.c_profile_pic);
        mPhoneNumber = findViewById(R.id.inputPhoneNumber);
        mPhoneNumber.setText("+1");
        mPostalAddress = findViewById(R.id.inputPostalAddress);
        mRequestQueue = APIRequestSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        // Display profile pic and autofill user info
        String display_name = getIntent().getStringExtra("display_name");
        String photo_url = getIntent().getStringExtra("photo_url");
        mFullName.setText(display_name);
        new DownloadImageTask((ImageView)findViewById(R.id.c_profile_pic)).execute(photo_url);

        //Limit search to addresses in United States only, without the filter the autocomplete will
        //display results from different countries
        final AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("us")
                .build();


        mPostalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPlaceAutocompleteActivityIntent(filter); }});


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
    private boolean invalidAccountFields(String checkFullName, String checkPhoneNumber, String checkPostalAddress) {
        if(checkFullName.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your full name", Toast.LENGTH_LONG).show();
            return true;
        }
        if(checkPhoneNumber.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your phone number", Toast.LENGTH_LONG).show();
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

                String display_name = mFullName.getText().toString();
                String phone_number = mPhoneNumber.getText().toString();
                String pickupAddress = mPostalAddress.getText().toString();

                // Checking if all necessary inputs are given, if not return and give a error
                if (invalidAccountFields(display_name,
                        phone_number,
                        pickupAddress
                        )) break;

                FirebaseUser user = mAuth.getCurrentUser();
                APIUtils.postUser(this,user, pickupAddress);
                return true;
        }
        return super.onOptionsItemSelected(button);
    }

    public void checkUserExists( FirebaseUser user ) {
        APIUtils.getUser(this, user,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        startActivity(new Intent (ConfirmProfile.this, ScheduledEvents.class));
                    }
                });
    }
}


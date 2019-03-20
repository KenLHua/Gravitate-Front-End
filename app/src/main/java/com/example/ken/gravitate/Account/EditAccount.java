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
import android.view.textservice.SpellCheckerInfo;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

public class EditAccount extends AppCompatActivity {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // Setting all activity variables
    private TextView mFullName;
    private TextView mPhoneNumber;
    private TextView mPostalAddress;
    private Context mContext;
    private String token;
    private FirebaseUser user;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initializing layout UI elements
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
        // Set the action bar to use the toolbar
        setSupportActionBar(toolbar);


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

        user = AuthSingleton.getInstance().getCurrentUser();

        // Set these variables to be used to input data
        mContext = EditAccount.this;
        mFullName = findViewById(R.id.inputFullName);
        mPhoneNumber = findViewById(R.id.inputPhoneNumber);
        mPostalAddress = findViewById(R.id.inputPostalAddress);


        // Set the information to match the user's information in the database
        APIUtils.getUser(mContext, user,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            JSONObject response = result;
                            mFullName.setText(response.getString("display_name"));
                            mPostalAddress.setText(response.getString("pickupAddress"));
                            new DownloadImageTask((ImageView)findViewById(R.id.c_profile_pic)).execute(response.getString("photo_url"));
                            mPhoneNumber.setText(response.getString("phone_number"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, token);




        //Limit search to addresses in United States only, without the filter the autocomplete will
        //display results from different countries
        final AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("us")
                .build();


        mPostalAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPlaceAutocompleteActivityIntent(filter); }});

        // Facebook log-in integration
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(mContext, "Successful fb login. ", Toast.LENGTH_LONG).show();
                        Log.i("fb login onSuccess", loginResult.toString());
                        Log.i("fb login onSuccess Token", loginResult.getAccessToken().getToken());

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(mContext, "Cancelled fb login. ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(mContext, "Error fb login. ", Toast.LENGTH_LONG).show();
                        Log.i("fb login onError", exception.getMessage());
                    }
                });

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
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
//            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    // Checks if all profile fields are correct
    private boolean invalidAccountFields(String checkFullName, String checkPhoneNumber, String checkPostalAddress) {
        if(checkFullName.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your full name", Toast.LENGTH_LONG).show();
            return true;
        }
        if(checkPhoneNumber.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your phone number", Toast.LENGTH_LONG).show();
            return true;
        }
        if(checkPhoneNumber.length() != 12){
            Toast.makeText(mContext, "Error: Please input a valid 10 length phone number", Toast.LENGTH_LONG).show();
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
                String postalAddress = mPostalAddress.getText().toString();

                // Checking if all necessary inputs are given, if not return and give a error
                if (invalidAccountFields(fullName,
                        phoneNumber,
                        postalAddress)) break;

                //Do something with the information

                APIUtils.postUser(this, user, fullName, user.getPhotoUrl().toString(),postalAddress, phoneNumber, token, MyProfile.class);
                return true;
        }
        return super.onOptionsItemSelected(button);
    }
}

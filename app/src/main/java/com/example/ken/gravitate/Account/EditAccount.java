package com.example.ken.gravitate.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.JSONUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EditAccount extends AppCompatActivity {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private TextView mFullName;
    private TextView mPhoneNumber;
    private TextView mEmailAddress;
    private TextView mPostalAddress;
    private Context mContext;

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

        String userFullName = user.getDisplayName();
        String userPhoneNumber = user.getPhoneNumber();
        String userProfilePic  = user.getPhotoUrl().toString();

        mContext = EditAccount.this;
        mFullName = findViewById(R.id.inputFullName);
        mPhoneNumber = findViewById(R.id.inputPhoneNumber);

        // Display profile pic and autofill user info
        mFullName.setText(userFullName);
        mPhoneNumber.setText(userPhoneNumber);
        new DownloadImageTask((ImageView)findViewById(R.id.profile_pic)).execute(userProfilePic);

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

    // Checks if all flight input fields are filled
    private boolean invalidAccountFields(String checkFullName, String checkPhoneNumber) {
        if(checkFullName.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your full name", Toast.LENGTH_LONG).show();
            return true;
        }
        if(checkPhoneNumber.length() == 0 ){
            Toast.makeText(mContext, "Error: Please input your phone number", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    // Will not update if information is the same
    private boolean sameAccountFields(String checkFullName, String checkPhoneNumber) {
        if (checkFullName == mFullName.getText().toString()) {
            return true;
        }
        if (checkPhoneNumber == mPhoneNumber.getText().toString()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem saveButton){
        switch(saveButton.getItemId()){
            case R.id.send_request_save:

                String fullName = mFullName.getText().toString();
                String phoneNumber = mPhoneNumber.getText().toString();

                // Checking if all necessary inputs are given, if not return and give a error
                if (invalidAccountFields(fullName, phoneNumber)) break;

                // Check if user saves already existing information
                if (sameAccountFields(fullName, phoneNumber)) break;

                // Request_URL = ("Full Name", "Phone Number", "Email Address", "Address")
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //JSONUtils.getEditUserInfo(user.getUid(),fullName, phoneNumber,user.getPhotoUrl(), postalAddress);

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("updateUserSuccessful", "User profile updated.");
                                }
                            }
                        });

                Toast.makeText(mContext, "Account Information Saved!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(saveButton);
    }
}

package com.example.ken.gravitate.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ken.gravitate.Models.User;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class MyProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView mNameDisplay;
    private TextView mEmailDisplay;
    private TextView mPhoneDisplay;
    private ImageView mProfileImageDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Getting current user's information
        FirebaseUser user = AuthSingleton.getInstance().getCurrentUser();
        populateUserInfo(user);

        String userEmail = user.getEmail();
        String userFullName = user.getDisplayName();
//        String userPhoneNumber = user.getPhoneNumber();
        String userProfilePic  = user.getPhotoUrl().toString();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_layout);

        // Getting UI fields to input user information
        mNameDisplay = (TextView) findViewById(R.id.username);
        mEmailDisplay = (TextView) findViewById(R.id.email);
        mPhoneDisplay = (TextView) findViewById(R.id.phone_number);
        mProfileImageDisplay = (ImageView) findViewById(R.id.profile_pic);

        // Setting UI fields to represent the current user's information
        mNameDisplay.setText(userFullName);
        mEmailDisplay.setText(userEmail);
//        mPhoneDisplay.setText(userPhoneNumber);
        new DownloadImageTask(mProfileImageDisplay).execute(userProfilePic);



        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        //Set up back button
        toolbar.setNavigationIcon(R.drawable.system_icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });

        //Set up edit account button
        TextView textview = findViewById(R.id.edit_account_btn);
        textview.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, EditAccount.class));
            }
        });

    }

//    public void populateUserInfo( FirebaseUser user ) {
//
//        String request_url = APIUtils.getUserURL(user.getUid());
//        APIUtils.getUser(this, request_url,
//                new VolleyCallback() {
//                    @Override
//                    public void onSuccessResponse(String result) {
//                        try {
//                            JSONObject response = new JSONObject(result);
//                            mNameDisplay.setText(response.getString("display_name"));
//                            mEmailDisplay.setText(response.getString("email"));
//                            mPhoneDisplay.setText(response.getString("phone_number"));
//                            new DownloadImageTask(mProfileImageDisplay).execute(response.getString("photo_url"));
//
//                        } catch (JSONException e ) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }

    public void populateUserInfo( FirebaseUser user ) {

        String request_url = APIUtils.getUserURL(user.getUid());
        APIUtils.getUser(this, request_url,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            mNameDisplay.setText(result.getString("display_name"));
//                            mEmailDisplay.setText(result.getString("email"));
                            mPhoneDisplay.setText(result.getString("phone_number"));
                            new DownloadImageTask(mProfileImageDisplay).execute(result.getString("photo_url"));

                        } catch (JSONException e ) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    // Helper method for back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

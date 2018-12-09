package com.example.ken.gravitate.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MyProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView mNameDisplay;
    private TextView mEmailDisplay;
    private TextView mPhoneDisplay;
    private TextView mPickupDisplay;
    private ImageView mProfileImageDisplay;
    private Context mContext;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = MyProfile.this;


        // Getting current user's information
        FirebaseUser user = AuthSingleton.getInstance().getCurrentUser();
        populateUserInfo(user);

        String userEmail = user.getEmail();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_layout);

        // Getting UI fields to input user information
        mNameDisplay = (TextView) findViewById(R.id.username);
        mPhoneDisplay = (TextView) findViewById(R.id.phone_number);
        mEmailDisplay = (TextView) findViewById(R.id.email);
        mPickupDisplay= (TextView) findViewById(R.id.address);
        mProfileImageDisplay = (ImageView) findViewById(R.id.c_profile_pic);
        mEmailDisplay.setText(userEmail);

        APIUtils.getUser(mContext, user,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            JSONObject response = result;
                            mNameDisplay.setText(response.getString("display_name"));
                            mPickupDisplay.setText(response.getString("pickupAddress"));
                            new DownloadImageTask(mProfileImageDisplay).execute(response.getString("photo_url"));
                            mPhoneDisplay.setText(response.getString("phone_number"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, token);




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

    // Populate the user's profile page with their database information
    public void populateUserInfo( FirebaseUser user ) {
        APIUtils.getUser(this, user,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            mNameDisplay.setText(result.getString("display_name"));
                            mEmailDisplay.setText(result.getString("email"));
                            mPhoneDisplay.setText(result.getString("phone_number"));
                            new DownloadImageTask(mProfileImageDisplay).execute(result.getString("photo_url"));

                        } catch (JSONException e ) {
                            e.printStackTrace();
                        }
                    }
                }, token );
    }


    // Helper method for back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

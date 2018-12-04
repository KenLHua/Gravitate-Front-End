package com.example.ken.gravitate.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ken.gravitate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView mNameDisplay;
    private TextView mEmailDisplay;
    private TextView mPhoneDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Getting current user's information
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        String userFullName = user.getDisplayName();
        String userPhoneNumber = user.getPhoneNumber();
        String userProfilePic  = user.getPhotoUrl().toString();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_layout);

        // Getting UI fields to input user information
        mNameDisplay = (TextView) findViewById(R.id.username);
        mEmailDisplay = (TextView) findViewById(R.id.email);
        mPhoneDisplay = (TextView) findViewById(R.id.phone_number);
        // Setting UI fields to represent the current user's information
        mNameDisplay.setText(userFullName);
        mEmailDisplay.setText(userEmail);
        mPhoneDisplay.setText(userPhoneNumber);




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



    // Helper method for back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

package com.example.ken.gravitate;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ken.gravitate.Account.LoginActivity;
import com.example.ken.gravitate.Event.ScheduledEvents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeScreen extends AppCompatActivity {
    private Button button;
    private Button skip;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    LinearLayoutCompat topL, botL;
    Animation upDown, downUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        // if a user is already logged in, skip them to scheduled events
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if ( user != null ) {
                    mAuth.removeAuthStateListener(mAuthListener);
                    startActivity(new Intent(WelcomeScreen.this, ScheduledEvents.class));
                    finish();
                }

            }
        };

        setContentView(R.layout.activity_welcome_screen);

        // Setting the gradient
        RelativeLayout welcomeScreen = findViewById(R.id.welcomeScreen);
        AnimationDrawable colorgradient = (AnimationDrawable) welcomeScreen.getBackground();
        colorgradient.setEnterFadeDuration(2000);
        colorgradient.setExitFadeDuration(4000);
        colorgradient.start();

        // Grab the UI elements
        button = (Button) findViewById(R.id.explore);
        skip = (Button) findViewById(R.id.welcomeSkip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openReactActivity();
                openOnBoard();
            }
        });

        // Skip to Scheduled Events
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.welcomeSkip:
                        startActivity(new Intent(WelcomeScreen.this, LoginActivity.class));
                        finish();
                        break;

                }
            }
        });

        //Setup gradient animation
        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.gravitate_logo);


    }
    public void openOnBoard(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
//
//    public void openReactActivity() {
//        Intent intent = new Intent(this, MyReactActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
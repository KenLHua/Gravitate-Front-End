package com.example.ken.gravitate;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ken.gravitate.Event.ScheduledEvents;

public class WelcomeScreen extends AppCompatActivity {
    private Button button;
    private Button skip;
    LinearLayoutCompat topL, botL;
    Animation upDown, downUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        ConstraintLayout welcomeScreen = findViewById(R.id.welcomeScreen);

        button = (Button) findViewById(R.id.explore);
        skip = (Button) findViewById(R.id.welcomeSkip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOnBoard();
            }
        });

        // Skip to Scheduled Events
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.welcomeSkip:
                        startActivity(new Intent(WelcomeScreen.this, ScheduledEvents.class));
                        break;

                }
            }
        });

    }
    public void openOnBoard(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
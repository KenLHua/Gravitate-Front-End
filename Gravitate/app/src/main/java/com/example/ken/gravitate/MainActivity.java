package com.example.ken.gravitate;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import  android.support.v4.widget.NestedScrollView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private NestedScrollView nestedScrollView;
    private DrawerLayout drawerLayout;
    private ImageView profile;
    private View header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
            // Getting references to navDrawer items
        header = navigationView.getHeaderView(0);
        profile = header.findViewById(R.id.nav_profile);
            //
        if(profile == null){
            startActivity(new Intent(MainActivity.this, NewEvent.class));
        }

            // Setting up the navigation drawer icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            //
        //

        fab = findViewById(R.id.fab);



        // Pressing on FAB, open new Activity -> New Event
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewEvent.class));
            }
        });

        // Pressing on Profile Pic, open new Activity -> My Profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyProfile.class));
            }
        });

    }
    // Open NavDrawer when MenuButton is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Not Finished
    private CardView createNewCard(String eventName){
        //CardView newCard = getApplicationContext().obtainStyledAttributes(attrs)
        return null;
    }
}


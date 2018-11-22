package com.example.ken.gravitate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fragmentManager;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private ImageView profile;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();


        setContentView(R.layout.activity_main);

        // Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Side-Navigation Setup
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Side-Navigation Menu Setup
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Floating Action Button Setup
        fab = findViewById(R.id.fab);

        // Pressing on FAB, open new Activity -> New Event
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewEvent.class));
            }
        });

    }

    // Creates a Fragment Instance of respective menu item
    // TODO: Determine whether to use Fragments or Activites
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, MyProfile.class));
                break;
        }
        switch(menuItem.getItemId()){
            case R.id.nav_messages:
                replaceFragment(new MessageFragment());
                break;
        }
        switch(menuItem.getItemId()){
            case R.id.nav_simple_activity_1:
                startActivity(new Intent(MainActivity.this, SimpleActivity1.class));
                break;
        }
        switch(menuItem.getItemId()){
            case R.id.nav_simple_activity_2:
                startActivity(new Intent(MainActivity.this, InputPickupAddress.class));
                break;
        }
        switch(menuItem.getItemId()){
            case R.id.nav_simple_activity_3:
                startActivity(new Intent(MainActivity.this, SimpleActivity3.class));
                break;
        }

        switch(menuItem.getItemId()){
            case R.id.nav_settings:
                replaceFragment(new SettingFragment());
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // When pressing back button, closes menu instead of exiting program
    @Override
    public void onBackPressed() {
        if( drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    // Open NavDrawer when MenuButton is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Display a fragment and handle backstack behavior
    private void replaceFragment (Fragment fragment){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}


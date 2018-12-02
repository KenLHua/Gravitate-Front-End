package com.example.ken.gravitate.Event;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.ken.gravitate.Account.LoginActivity;
import com.example.ken.gravitate.Utils.Card;
import com.example.ken.gravitate.Utils.CardAdapter;
import com.example.ken.gravitate.Messaging.MessageFragment;
import com.example.ken.gravitate.Account.MyProfile;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Settings.SettingsActivity;
import com.example.ken.gravitate.SimpleActivity1;
import com.example.ken.gravitate.SimpleActivity3;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.ArrayList;
import java.util.List;

public class ScheduledEvents extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fragmentManager;
    private DrawerLayout drawer;
    private ImageView profile;
    private View header;
    private SpeedDialView fab;
    GoogleSignInClient mGoogleSignInClient;
    private static final String web_client_id = "1070051773756-o6l5r1l6v7m079r1oua2lo0rsfeu8m9i.apps.googleusercontent.com";
    private static final String DOMAIN = "ucsd.edu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.scheduled_events);

        // Configure sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(web_client_id)
                .requestEmail()
                .setHostedDomain(DOMAIN)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
        final SpeedDialView speedDialView = findViewById(R.id.speedDial);

        // Populate the FAB secondary menu
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_input_flight_number, R.drawable.system_icon_plane_ticket)
                        .setLabel(getString(R.string.fab_flight))
                        .setLabelClickable(false)
                        .create());
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_new_event, R.drawable.system_icon_event)
                        .setLabel(getString(R.string.fab_event))
                        .setLabelClickable(false)
                        .create());


        // Provide behavior to the secondary FAB buttons
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.fab_input_flight_number:
                        startActivity(new Intent(ScheduledEvents.this, InputFlight.class));
                        return false; // true to keep the Speed Dial open
                    default:
                        return false;
                }
            }
        });
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.fab_input_flight_number:
                        startActivity(new Intent(ScheduledEvents.this, InputFlight.class));
                        return false; // true to keep the Speed Dial open
                    default:
                        return false;
                }
            }
        });

        //Recycler view with adapter to display cards
        // TODO:: do this programatically with the server data
        RecyclerView recyclerView = findViewById(R.id.recycler_list);
        /*
        Parse the JSON File and add all the events and times into the cardList
        TODO: Fill in the process
         */

        /*
         Create the cards and dispaly then
         */
        final List<Card> card_list = new ArrayList<>();

        card_list.add(new Card(R.drawable.lax, "LAX", R.drawable.default_profile, "Mon 8pm"));
        card_list.add(new Card(R.drawable.lax, "LAX", R.drawable.default_profile, "Tue 8pm"));
        card_list.add(new Card(R.drawable.lax, "LAX", R.drawable.default_profile, "Mon 8pm"));
        card_list.add(new Card(R.drawable.lax, "LAX", R.drawable.default_profile, "Tue 8pm"));
        card_list.add(new Card(R.drawable.lax, "LAX", R.drawable.default_profile, "Mon 8pm"));
        card_list.add(new Card(R.drawable.lax, "LAX", R.drawable.default_profile, "Tue 8pm"));

        /*
        Parse the JSON File and add all the events and times into the cardList
        TODO: Fill in the process
         */

        if(card_list.size() == 0){
            // TODO: Display "No Ride requests pending"
        }

        final CardAdapter adapter = new CardAdapter(this,card_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnCardClickListener(new CardAdapter.OnCardClickListener() {
            @Override
            public void onCardClick(int position) {
                card_list.get(position).setDestName("Clicked");
                adapter.notifyItemChanged(position);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy >0){
                    speedDialView.hide();
                }
                else{
                    speedDialView.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    // Creates a Fragment Instance of respective menu item
    // TODO: Determine whether to use Fragments or Activites
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.nav_profile:
                startActivity(new Intent(ScheduledEvents.this, MyProfile.class));
                break;
        }
        switch(menuItem.getItemId()){
            case R.id.nav_messages:
                replaceFragment(new MessageFragment());
                break;
        }
        switch(menuItem.getItemId()){
            case R.id.nav_simple_activity_1:
                startActivity(new Intent(ScheduledEvents.this, SimpleActivity1.class));
                break;
        }
        switch(menuItem.getItemId()){
            case R.id.nav_simple_activity_2:
                break;
        }
        switch(menuItem.getItemId()){
            case R.id.nav_simple_activity_3:
                startActivity(new Intent(ScheduledEvents.this, SimpleActivity3.class));
                break;
        }

        switch(menuItem.getItemId()){
            case R.id.nav_settings:
                startActivity(new Intent(ScheduledEvents.this, SettingsActivity.class));
                break;
        }
        switch(menuItem.getItemId()) {
            case R.id.nav_logout:
                signOut();
                startActivity(new Intent(ScheduledEvents.this, LoginActivity.class));
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

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private List<Card> parseJSONEvents (List<Card> cardList){
        // TODO: parse them and add them to the cardList
        return null;
    }
}


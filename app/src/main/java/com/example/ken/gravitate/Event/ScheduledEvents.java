package com.example.ken.gravitate.Event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ken.gravitate.Account.LoginActivity;
import com.example.ken.gravitate.Account.MyProfile;
import com.example.ken.gravitate.Settings.SettingsActivity;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.MyViewHolder;
import com.example.ken.gravitate.Models.EventRequestModule;
import com.example.ken.gravitate.forcematch2;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScheduledEvents extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String TAG = "documentLOOKUP";

    public static FragmentManager fragmentManager;
    private DrawerLayout drawer;
    private ImageView profile;
    private View header;
    private SpeedDialView fab;
    private TextView emptyRequests;
    private ImageView navProfilePic;

    DocumentReference userDocRef;
    FirebaseFirestore db;

    private RecyclerView orbitView;
    private SwipeRefreshLayout swipeContainer;
    private Context mContext;
    private boolean hasRide;
    private FirestoreRecyclerAdapter adapter;


    GoogleSignInClient mGoogleSignInClient;
    private static final String web_client_id = "1070051773756-o6l5r1l6v7m079r1oua2lo0rsfeu8m9i.apps.googleusercontent.com";
    private static final String DOMAIN = "ucsd.edu";
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasRide = false;

        // Setting UI layout
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.scheduled_events);
        //Recycler view with adapter to display cards
        orbitView = findViewById(R.id.orbit_list);
        mContext = ScheduledEvents.this;

        // Side-Navigation Setup
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Have shadow appear above everything
        View toolbarShadow = findViewById(R.id.toolbar_shadow);
        toolbarShadow.bringToFront();


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

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Getting ride requests from the user's collection
        userID = user.getUid();
        userDocRef = db.collection("users").document(userID);
        getUserRideRequestList(userDocRef, orbitView);
        orbitView.setLayoutManager(new LinearLayoutManager(ScheduledEvents.this));




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


        // Get the only header and set the profile's picture
        View navHeader = navigationView.getHeaderView(0);
        navProfilePic = navHeader.findViewById(R.id.nav_profile);
        APIUtils.getUser(mContext, user, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    new DownloadImageTask(navProfilePic).execute(result.getString("photo_url"));

                } catch (JSONException e ) {
                    e.printStackTrace();
                }
            }
        }, token);

        // Displaying that the user has no ride requests
        emptyRequests = findViewById(R.id.no_rides);




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

        // Scrolling the cards will hide the FAB button
        orbitView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView orbitView, int dx, int dy) {
                if (dy >0){
                    speedDialView.hide();
                }
                else{
                    speedDialView.show();
                }
                super.onScrolled(orbitView, dx, dy);
            }
        });

        // Swipe to refresh
        swipeContainer = findViewById(R.id.swipeContainer);

        // Swipe to refresh behavior
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 1500); // Delay in millis

            }
        });
        // Adding colors for refresh
        swipeContainer.setColorSchemeResources(
                R.color.colorDark,
                R.color.colorAccent);
    }

    // Creates a Fragment Instance of respective menu item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.nav_profile:
                startActivity(new Intent(ScheduledEvents.this, MyProfile.class));
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

    // Sign out of google account
    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        ScheduledEvents.this.finishAndRemoveTask();
                    }
                });
    }


    // Populate ride request information
    private void getUserRideRequestList(DocumentReference userRef, RecyclerView display) {
        // Get a limit of 10 ride requests from the user
        Query rideRequestQuery = userRef.collection("eventSchedules").orderBy("destTime").limit(10);

        // Set the model for firebase to populate the adapter
        FirestoreRecyclerOptions<EventRequestModule> options =
                new FirestoreRecyclerOptions
                        .Builder<EventRequestModule>()
                        .setQuery(rideRequestQuery,EventRequestModule.class)
                        .build();

        


        // Create the adapter
        adapter = new FirestoreRecyclerAdapter<EventRequestModule, MyViewHolder>(options) {



            @NonNull
            @Override
            // Set the holder for the view that will have all the information for each view
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                // Create the card
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View v = inflater.inflate(R.layout.card, viewGroup, false);
                registerForContextMenu(v);
                MyViewHolder myViewHolder = new MyViewHolder(v);


                return myViewHolder;
            }

            @Override
            // When firestore gives us information, populate the information
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull EventRequestModule model) {
                // Set background and destination UI elements
                int cardBackground = R.drawable.lax;
                final String destName = "LAX";
                // Format the flight local time to be readable
                final String parsedFlightDate = model.getFlightTime().substring(0,10) + " | ";

                // Parse the hour and minutes
                final StringBuilder parsedFlightTime = new StringBuilder(model.getFlightTime().substring(11,16));
                int flightHour = Integer.parseInt(parsedFlightTime.substring(0,2));

                // Making the local time in AM/PM form
                if(flightHour == 0){
                    parsedFlightTime.append(" AM");
                    parsedFlightTime.setCharAt(0,'1');
                    parsedFlightTime.setCharAt(1,'2');
                }
                else if (flightHour == 12){
                    parsedFlightTime.append(" PM");
                }
                else{
                    // If the hour is 12 to 23, it is PM
                    if( flightHour > 11){
                        parsedFlightTime.append(" PM");
                        flightHour = flightHour % 12;

                        // Insert only one hour digit
                        if(flightHour < 10){
                            parsedFlightTime.setCharAt(0, '0');
                            parsedFlightTime.setCharAt(1, Integer.toString(flightHour).charAt(0));
                        }
                        // Insert two hour digits
                        else{
                            parsedFlightTime.setCharAt(0, Integer.toString(flightHour).charAt(0));
                            parsedFlightTime.setCharAt(1, Integer.toString(flightHour).charAt(1));
                        }
                    }
                    else{
                        parsedFlightTime.append(" AM"); }
                }

                // Grab all the necessary information and pass it onto the next activity once a card is pressed
                final String eventId = getSnapshots().getSnapshot(i).getId();

                final boolean stillPending = model.isPending();
                final DocumentReference orbitRef = model.getOrbitRef();
                final String pickupAddress = model.getPickupAddress();
                List<String> temp = model.getMemberProfilePhotoUrls();
                final ArrayList<String> profileImages = new ArrayList<String>();
                final Long destTime = model.getDestTime();
                final DocumentReference rideRef = model.getRideRequestRef();

                // Get all profile URLs and add it to an array list that can be passed
                for (String eachURL : temp){
                    profileImages.add(eachURL);
                }

                // Set the context and fields
                holder.context = mContext;
                holder.stillPending = stillPending;
                holder.background_img.setImageResource(cardBackground);
                holder.card_dest.setText(destName);
                // Update whether the "no requests found" is displayed
                hasRide = true;

                // If no orbit is found
                if(stillPending) {
                    holder.card_pending.setText("Pending Ride Request");
                    holder.card_time.setText("Flight Time : " + parsedFlightDate + parsedFlightTime);
                }
                // If an orbit is found
                else {
                    new DownloadImageTask(holder.profile_photo1).execute(profileImages.get(0));
                    if(profileImages.size() == 2){
                        new DownloadImageTask(holder.profile_photo2).execute(profileImages.get(1));
                    }
                    holder.card_time.setText("Flight Time : " + parsedFlightDate + parsedFlightTime);
                    holder.card_pending.setText("Orbiting");
                    holder.orbitRef = model.getOrbitRef();
                    holder.profileImages = profileImages;
                }

                // Once a card is clicked
                holder.setOnClickListener(new MyViewHolder.ClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        //Start new activity to show event details
                        Intent intent = new Intent(mContext, RideEvent.class);
                        // Pass all the information into the next actiity
                        intent.putExtra("destName", destName);
                        intent.putExtra("flightTime", parsedFlightDate + parsedFlightTime);
                        intent.putExtra("stillPending", stillPending);
                        intent.putExtra("pickupAddress", pickupAddress);

                        // If it is an orbit, pass a few more fields
                        intent.putExtra("rideRef", rideRef.getId());
                        intent.putExtra("eventRef", eventId);
                        intent.putExtra("userRef", userID);

                        if(!stillPending) {
                            intent.putStringArrayListExtra("profileImages", profileImages);
                            intent.putExtra("orbitRef",  orbitRef.getPath());
                            intent.putExtra("destTime", destTime );
                        }
                        // Go into the view more details of a card
                        mContext.startActivity(intent);
                    }

                    // Behavior the user holds down on a card
                    @Override
                    public void onItemLongClick(View view, int position) {
                        // Show options menu when long pressing a card
                        PopupMenu popup = new PopupMenu(mContext, view);
                        popup.inflate(R.menu.request_menu);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch(menuItem.getItemId()){
                                    case R.id.cancel_request:
                                        // Handle cancel button
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        popup.show();
                    }
                });
            }

            // If the data changes, do these actions
            @Override
            public void onDataChanged() {
                if (adapter.getItemCount() == 0){
                    hasRide = false;
                    emptyRequests.setText("You have no requests!");
                }
                else{
                    hasRide = true;
                }
            }

        };
        adapter.notifyDataSetChanged();

        display.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }




}


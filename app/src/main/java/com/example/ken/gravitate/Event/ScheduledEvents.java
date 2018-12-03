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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ken.gravitate.Account.LoginActivity;
import com.example.ken.gravitate.Account.MyProfile;
import com.example.ken.gravitate.Messaging.MessageFragment;
import com.example.ken.gravitate.Settings.SettingsActivity;
import com.example.ken.gravitate.Utils.Card;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.MyViewHolder;
import com.example.ken.gravitate.Models.EventRequestModule;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

    private List<DocumentSnapshot> allDocs;
    CollectionReference eventsSchedules;
    DocumentReference userDocRef;
    private List<Card> mPendingCards;
    private List<Card> mOrbitCards;
    private String mDestTime;
    private DocumentSnapshot locationSnap;
    FirebaseFirestore db;

    // Kenneth's spagetti code
    RecyclerView orbitView;
    RecyclerView requestView;
    RecyclerView emptyView;

    // Swipe to refresh
    SwipeRefreshLayout swipeLayout;
    private SwipeRefreshLayout swipeContainer;

    private Context mContext;

    private FirestoreRecyclerAdapter adapter;
    //

    GoogleSignInClient mGoogleSignInClient;
    private static final String web_client_id = "1070051773756-o6l5r1l6v7m079r1oua2lo0rsfeu8m9i.apps.googleusercontent.com";
    private static final String DOMAIN = "ucsd.edu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.scheduled_events);
        final String TAG = "documentLOOKUP";
        //Recycler view with adapter to display cards
        orbitView = findViewById(R.id.orbit_list);
        requestView = findViewById(R.id.pending_list);
        //emptyView = findViewById(R.id.empty_list);
        //

        mContext = ScheduledEvents.this;


        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // ACTUAL CODE userDocRef = db.document(user.getUid());
        // String userID = user.getUid();
        String userID = "zkenneth_test";
                userDocRef = db.collection("users").document(userID);

        getUserRideRequestList(userDocRef, orbitView);

        requestView.setLayoutManager(new LinearLayoutManager(ScheduledEvents.this));
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

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 3000); // Delay in millis

            }
        });
        // Adding colors for refresh
        swipeContainer.setColorSchemeResources(
                R.color.colorDark,
                R.color.colorAccent);
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


    private void getUserRideRequestList(DocumentReference userRef, RecyclerView display) {
        Query rideRequestQuery = userRef.collection("eventSchedules").limit(10);

        FirestoreRecyclerOptions<EventRequestModule> options =
                new FirestoreRecyclerOptions
                        .Builder<EventRequestModule>()
                        .setQuery(rideRequestQuery,EventRequestModule.class)
                        .build();

        

        adapter = new FirestoreRecyclerAdapter<EventRequestModule, MyViewHolder>(options) {



            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                // Create the card
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View v = inflater.inflate(R.layout.card, viewGroup, false);
                MyViewHolder myViewHolder = new MyViewHolder(v);


                return myViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull EventRequestModule model) {
                int cardBackground = R.drawable.lax;
                final String destName = "LAX";
                int cardProfilePhoto = R.drawable.default_profile;
                final String flightTime = model.getFlightTime();
                final boolean stillPending = model.isPending();
                final DocumentReference orbitRef = model.getOrbitRef();
                List<String> temp = model.getMemberProfilePhotoUrls();
                final ArrayList<String> profileImages = new ArrayList<String>();
                for (String eachURL : temp){
                    profileImages.add(eachURL);
                }
                holder.context = mContext;
                holder.stillPending = stillPending;
                holder.background_img.setImageResource(cardBackground);
                holder.profile_photo.setImageResource(cardProfilePhoto);
                holder.card_dest.setText(destName);

                if(stillPending) {
                    holder.card_time.setText("Desired Flight Time : " + flightTime);
                    holder.card_pending.setText("Pending Ride Request");
                }
                else {

                    holder.card_time.setText("Projected Arrival Time : " + model.getDestTime());
                    holder.card_pending.setText("Orbiting");
                    holder.orbitRef = model.getOrbitRef();
                    holder.profileImages = profileImages;
                }

                holder.setOnClickListener(new MyViewHolder.ClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        //Start new activity to show event details
                        Intent intent = new Intent(mContext, RideEvent.class);
                        intent.putExtra("destName", destName);
                        intent.putExtra("flightTime", flightTime);
                        intent.putExtra("stillPending", stillPending);
                        if(!stillPending) {
                            intent.putStringArrayListExtra("profileImages", profileImages);
                            intent.putExtra("orbitRef",  orbitRef.getPath());

                        }
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                });
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


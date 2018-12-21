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
import com.example.ken.gravitate.Models.Event;
import com.example.ken.gravitate.Models.EventRequestModule;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Settings.SettingsActivity;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.EventViewHolder;
import com.example.ken.gravitate.Utils.MyViewHolder;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import java.time.ZonedDateTime;
import java.time.ZoneId;

public class DiscoverEvents extends AppCompatActivity {

    final String TAG = "DiscoverEvents";

    public static FragmentManager fragmentManager;
    private TextView emptyRequests;

//    DocumentReference userDocRef;
    CollectionReference eventsRef;
    FirebaseFirestore db;

//    private RecyclerView orbitView;
    private RecyclerView eventsView;
    private SwipeRefreshLayout swipeContainer;
    private Context mContext;
    private boolean hasRide;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasRide = false;

        // Setting UI layout
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.discover_events);
        //Recycler view with adapter to display cards
        eventsView = findViewById(R.id.event_list);
        mContext = DiscoverEvents.this;

        // Have shadow appear above everything
        View toolbarShadow = findViewById(R.id.toolbar_shadow);
        toolbarShadow.bringToFront();

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Getting ride requests from the user's collection
        eventsRef = db.collection("events");
        getEventList(eventsRef, eventsView);
        eventsView.setLayoutManager(new LinearLayoutManager(DiscoverEvents.this));

        // Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Displaying that the user has no ride requests
        emptyRequests = findViewById(R.id.no_rides);

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

    // Populate ride request information
    private void getEventList(CollectionReference eventCollectionRef, RecyclerView display) {
        // Get a limit of 10 ride requests from the user
        Query eventsQuery = eventCollectionRef.orderBy("startTimestamp").limit(10);

        // Set the model for firebase to populate the adapter
        FirestoreRecyclerOptions<Event> options =
                new FirestoreRecyclerOptions
                        .Builder<Event>()
                        .setQuery(eventsQuery,Event.class)
                        .build();

        // Create the adapter
        adapter = new FirestoreRecyclerAdapter<Event, EventViewHolder>(options) {
            @NonNull
            @Override
            // Set the holder for the view that will have all the information for each view
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                // Create the card
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View v = inflater.inflate(R.layout.event_card, viewGroup, false);
                registerForContextMenu(v);
                EventViewHolder eventViewHolder = new EventViewHolder(v);
                return eventViewHolder;
            }

            @Override
            // When firestore gives us information, populate the information
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int i, @NonNull Event model) {
                // Set background and destination UI elements
                int cardBackground = R.drawable.lax;
                final String destName = "LAX";
                // Format the flight local time to be readable
                final String parsedFlightDate = model.getStartTimestamp().toString();

                final Long startTimestamp = model.getStartTimestamp();
                final ZoneId zoneId = ZoneId.of("America/Los_Angeles");
                final ZonedDateTime eventDate = ZonedDateTime.ofInstant(Instant.ofEpochSecond(startTimestamp), zoneId);
                // TODO: refactor and test all operations involved in Datetime conversion
                final String eventDay = eventDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));

                final boolean eventStatus = model.getIsClosed();
                final ArrayList<String> profileImages = new ArrayList<String>();

                // Set the context and fields
                holder.context = mContext;
                holder.background_img.setImageResource(cardBackground);
                holder.card_dest.setText(destName);
                // Update whether the "no requests found" is displayed
                hasRide = true;

                // If events are active (= not isClosed)
                if(true) { // Currently
                    holder.card_status.setText("Active");
                    holder.card_time.setText("Event Date: " + eventDay);
                }

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


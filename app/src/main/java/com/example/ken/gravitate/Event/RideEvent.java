package com.example.ken.gravitate.Event;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ken.gravitate.Models.Rider;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.RiderAdapter;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RideEvent extends AppCompatActivity {
    private TextView mPartnerName;
    private TextView mPartnerEmail;
    private Context mCtx;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_event);

        mCtx = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ride Details");
        toolbar.setNavigationIcon(R.drawable.system_icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        setSupportActionBar(toolbar);

        Boolean stillPending = getIntent().getExtras().getBoolean("stillPending");
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        RecyclerView recyclerView = findViewById(R.id.rider_list);

        final List<Rider> rider_list = new ArrayList<Rider>();
        Rider riderCard = new Rider(R.drawable.default_profile, "Name", "Email");
        rider_list.add(riderCard);

        TextView flightTimeDisplay = findViewById(R.id.flightTime);
        TextView pickupAddressDisplay = findViewById(R.id.pickupAddress);

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


        Button deleteRideButton = findViewById(R.id.delete_ride_bttn);
        deleteRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = getIntent().getExtras().getString("eventRef");
                String rideRequestId = getIntent().getExtras().getString("rideRef");
                String userId = getIntent().getExtras().getString("userRef");
                APIUtils.postDeleteRideRequest(mCtx,token,new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        Toast.makeText(mCtx, "Ride Request Deleted", Toast.LENGTH_LONG).show();
                        ((Activity)mCtx).finish();
                        Intent intent = new Intent (mCtx, ScheduledEvents.class);
                        startActivity(intent);
                    }
                },userId,eventId,rideRequestId);
            }
        });

        String pickupAddress = getIntent().getStringExtra("pickupAddress");

        String flightTime = getIntent().getStringExtra("flightTime");
        flightTimeDisplay.setText("Flight Time : " + flightTime);
        pickupAddressDisplay.setText("Pickup Address : " + pickupAddress);


        if(!stillPending.booleanValue()){
            deleteRideButton.setVisibility(View.GONE);
            String orbitPath = getIntent().getStringExtra("orbitRef");
            DocumentReference orbitRef = db.document(orbitPath);
            RiderAdapter adapter = new RiderAdapter(this,orbitRef,rider_list);
            ArrayList<String> profileImages = getIntent().getExtras().getStringArrayList("profileImages");
            adapter.setProfileImages(profileImages);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        }
        else{
            RiderAdapter adapter = new RiderAdapter(this,null,rider_list);
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
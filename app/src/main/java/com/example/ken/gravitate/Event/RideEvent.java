package com.example.ken.gravitate.Event;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ken.gravitate.Models.Rider;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.RiderAdapter;
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RideEvent extends AppCompatActivity {
    private TextView mPartnerName;
    private TextView mPartnerEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_event);

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
        String orbitPath = getIntent().getStringExtra("orbitRef");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference orbitRef = db.document(orbitPath);
        FirebaseUser user = AuthSingleton.getInstance().getCurrentUser();
        String currUserID = user.getUid();


        RecyclerView recyclerView = findViewById(R.id.rider_list);

        final List<Rider> rider_list = new ArrayList<Rider>();
        Rider riderCard = new Rider(R.drawable.default_profile, "Name", "Email");
        rider_list.add(riderCard);

        RiderAdapter adapter = new RiderAdapter(this,orbitRef,rider_list);
        // Tester code
        /*
        adapter.stillPending = stillPending.booleanValue();
        JSONObject orbitSnapshot = new JSONObject();
        HashMap<String, HashMap<String, Object>> userTicketPairs = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> userID = new HashMap<String,Object>();
        userID.put("hasCheckedIn", new Boolean(false));
        userID.put("inChat", new Boolean(false));
        userID.put("pickupAddress", "Pickup Address Hi!");
        userID.put("rideRequestRef", null);
        userID.put("userWillDrive", false);
        try{
            orbitSnapshot.put("ASLDKASLDKASDLK!L:#@K!#K!L:KA:LKDAL:SDKSA", userID);
            orbitSnapshot.put(currUserID, null);
        }
        catch(JSONException e){
            final String TAG = "RideEventJSON";
            Log.w(TAG, " Failed reading JSON object");
            e.printStackTrace();
        }
        */

        // End of tester Code

        if(!stillPending.booleanValue()){
            ArrayList<String> profileImages = getIntent().getExtras().getStringArrayList("profileImages");
            adapter.setProfileImages(profileImages);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        }
        else{
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }




        TextView flightTimeDisplay = findViewById(R.id.flightTime);
        String flightTime = getIntent().getStringExtra("flightTime");
        flightTimeDisplay.setText(flightTime);


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
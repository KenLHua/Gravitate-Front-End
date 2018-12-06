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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ken.gravitate.Models.Rider;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.RiderAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RideEvent extends AppCompatActivity {
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


        RecyclerView recyclerView = findViewById(R.id.rider_list);

        final List<Rider> rider_list = new ArrayList<Rider>();
        Rider riderCard = new Rider(R.drawable.default_profile, "Bobby Hill", "bobbyHill@ucsd.edu");
        rider_list.add(riderCard);

        RiderAdapter adapter = new RiderAdapter(this,rider_list);
        adapter.stillPending = stillPending.booleanValue();

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

    //  Add check button to the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inputflight_menu, menu);
        return true;
    }


}
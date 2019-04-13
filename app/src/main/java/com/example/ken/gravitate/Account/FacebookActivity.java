package com.example.ken.gravitate.Account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.textservice.SpellCheckerInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.ken.gravitate.Event.ScheduledEvents;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIRequestSingleton;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookActivity extends AppCompatActivity {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // Setting all activity variables
    private TextView mFullName;
    private TextView mPhoneNumber;
    private TextView mPostalAddress;
    private Context mContext;
    private String token;
    private FirebaseUser user;
    private CallbackManager callbackManager;
    private LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initializing layout UI elements
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Account");
        toolbar.setNavigationIcon(R.drawable.system_icon_back);

        // Back button will go to previous page when clicked on
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        // Set the action bar to use the toolbar
        setSupportActionBar(toolbar);


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

        user = AuthSingleton.getInstance().getCurrentUser();

        // Set these variables to be used to input data
        mContext = FacebookActivity.this;
        mFullName = findViewById(R.id.inputFullName);
        mPhoneNumber = findViewById(R.id.inputPhoneNumber);
        mPostalAddress = findViewById(R.id.inputPostalAddress);


        // Set the information to match the user's information in the database
        APIUtils.getUser(mContext, user,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            JSONObject response = result;
                            mFullName.setText(response.getString("display_name"));
                            mPostalAddress.setText(response.getString("pickupAddress"));
                            new DownloadImageTask((ImageView)findViewById(R.id.c_profile_pic)).execute(response.getString("photo_url"));
                            mPhoneNumber.setText(response.getString("phone_number"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, token);

        // Test link to someone else's facebook messenger profile
        findViewById(R.id.fb_mme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://m.me/billyraozx";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


//        findViewById(R.id.fb_event_button).setOnClickListener( new View.OnClickListener()  {
//            @Override
//            public void onClick(View v) {
//
//            }
//        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}

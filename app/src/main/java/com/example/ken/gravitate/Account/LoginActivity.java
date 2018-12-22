package com.example.ken.gravitate.Account;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ken.gravitate.Event.ScheduledEvents;
import com.example.ken.gravitate.R;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    SignInButton sign_in_bttn;
    FirebaseAuth mAuth;
    Context mCtx;
    private final static int RC_SIGN_IN = 2; // Request Code for starting new activity
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "GoogleActivity";
//    private static final String web_client_id = "1070051773756-o6l5r1l6v7m079r1oua2lo0rsfeu8m9i.apps.googleusercontent.com";
    private static final String DOMAIN = "ucsd.edu";
    private RelativeLayout layout;
    private ProgressBar progressBar;
    private TextView progressText;
    private boolean skippedProfile = false;
    private String token;

    @Override
    // Set the authentication listener on start of the activity
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Setting content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout = findViewById(R.id.login_layout);
        Log.d("Startingactivity", "hello");

        mCtx = this;

        // Setting up sign in progress
        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);


        // Configure sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .setHostedDomain(DOMAIN)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();


        // Create an auth listener for the auth instance
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };


        // Floating Action Button Setup
        sign_in_bttn = findViewById(R.id.googleBtn);


        // Gets Google Button Reference in Login_Activity Layout
        sign_in_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.googleBtn:
                        signIn();
                        break;
                }
            }
        });





    }

    @Override
    // When returning from google autocomplete, do something with the information
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ConnectivityManager cm =
                (ConnectivityManager)mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            Toast.makeText(mCtx, "Error: Make sure a stable internet connection is available"
                    , Toast.LENGTH_LONG).show();
            try{
                signOut();
            }catch (Error e){
                e.printStackTrace();
            }
            return;
        }


        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // When sign in is valid, authenticate with our firebase
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Signed in successfully
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            progressBar.setVisibility(View.INVISIBLE);
            progressText.setVisibility(View.INVISIBLE);
        }
    }

    // Authenticating with firebase
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        String email = acct.getEmail();
        email = email.substring(email.length()-9, email.length());
        // If email does not end with @ucsd.edu, then do not let them sign up
        if(!email.equals("@"+DOMAIN)){
            signOut();
            Toast.makeText(LoginActivity.this
                    , "Error: Registration only open to " + DOMAIN + " emails.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
            progressText.setVisibility(View.INVISIBLE);
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If we authenticated successfully
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Get REST access token
                            Task<GetTokenResult> tokenTask = FirebaseAuth.getInstance().getAccessToken(false);

                            while(!tokenTask.isComplete()){
                                Log.d("GettingToken", "async");
                                try{
                                    wait(500);
                                }
                                catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                            }
                            token = tokenTask.getResult().getToken();

                            // Check if user already set up profie
                            checkUserExists(user, token);
                            // Delay action by 3000 miliseconds
                            Handler handler = new Handler();
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {

                                    // If user does not exist, then trigger profile setup
                                    if(!skippedProfile) {
                                        String display_name = acct.getDisplayName();
                                        String photo_url = acct.getPhotoUrl().toString();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        progressText.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(LoginActivity.this, ConfirmProfile.class);
                                        intent.putExtra("display_name", display_name);
                                        intent.putExtra("photo_url", photo_url);
                                        startActivity(intent);

                                    }
                                    // Else, let them into the app
                                    else{

                                        startActivity(new Intent(LoginActivity.this, ScheduledEvents.class));


                                    }

                                }
                            };
                            handler.postDelayed(r,3000);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.login_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            progressText.setVisibility(View.INVISIBLE);
                            try{
                                // If it fails, sign them out of the google authentication
                                signOut();
                            }catch (Error e){
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    // sign in behavior
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    // Sign out behavior
    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    // Helper method to check if user set up profile
    public void checkUserExists( FirebaseUser user, String token ) {

        APIUtils.getUser(this, user,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        skippedProfile = true;
                    }
                },token);
    }


}

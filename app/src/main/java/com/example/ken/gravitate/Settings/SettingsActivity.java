package com.example.ken.gravitate.Settings;

<<<<<<< HEAD
import android.content.Context;
=======
import android.content.Intent;
>>>>>>> MaterialSettings
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;
<<<<<<< HEAD
=======
import com.example.ken.gravitate.Account.LoginActivity;
import com.example.ken.gravitate.Event.ScheduledEvents;
import com.example.ken.gravitate.R;
>>>>>>> MaterialSettings

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}

package com.example.ken.gravitate.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import com.example.ken.gravitate.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD

        setContentView(R.layout.settings);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        toolbar.setNavigationIcon(R.drawable.system_icon_back);

        // Back button will go to previous page when clicked on
=======
        setContentView(R.layout.settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        toolbar.setNavigationIcon(R.drawable.system_icon_back);
>>>>>>> f766ca5d0cc50586c4038b5d9a87ad294ecae8ca
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        setSupportActionBar(toolbar);

<<<<<<< HEAD

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment, new SettingsFragment())
=======
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.setting_fragment, new SettingsFragment())
>>>>>>> f766ca5d0cc50586c4038b5d9a87ad294ecae8ca
                .commit();
    }

    // Helper method for back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

package com.example.ken.gravitate.Account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.ken.gravitate.R;

public class ChangePassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Change Password");
        setSupportActionBar(toolbar);
    }

    // **Incomplete** Add Checkmark to the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_actionbar_menu, menu);
        return true;
    }
}

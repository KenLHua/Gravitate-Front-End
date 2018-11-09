package com.example.ken.gravitate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SimpleActivity1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_activity_1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SimpleActivity1");
        setSupportActionBar(toolbar);
    }
}

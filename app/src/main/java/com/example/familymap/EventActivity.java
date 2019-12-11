package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class EventActivity extends AppCompatActivity {
    private MapFragment mapFragment;
    // TODO: FIX BACK BUTTON

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = new MapFragment();
        fm.beginTransaction().replace(R.id.map_fragment, mapFragment).commit();
    }
}

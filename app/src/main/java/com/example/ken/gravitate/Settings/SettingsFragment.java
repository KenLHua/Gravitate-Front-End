package com.example.ken.gravitate.Settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.example.ken.gravitate.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Nullable
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}

package com.example.ken.gravitate.Account;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.example.ken.gravitate.R;
import javax.annotation.Nullable;

public class EditAccountFragment extends PreferenceFragmentCompat {

    @Nullable
    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.edit_account_preferences, rootKey);
    }
}

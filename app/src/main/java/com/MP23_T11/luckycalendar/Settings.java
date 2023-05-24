package com.MP23_T11.luckycalendar;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class Settings extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
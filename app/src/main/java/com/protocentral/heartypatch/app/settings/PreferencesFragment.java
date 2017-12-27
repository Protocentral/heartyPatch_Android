package com.protocentral.heartypatch.app.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.util.Log;

import com.protocentral.heartypatch.BuildConfig;
import com.protocentral.heartypatch.R;

public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    // Log
    private final static String TAG = PreferenceFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        initSummary(getPreferenceScreen());

        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
        setupSpecialPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "Pref changed: " + key);
        updatePrefSummary(findPreference(key));
    }

    private void setupSpecialPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Set reset button
        Preference button = findPreference("pref_reset");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                Log.d(TAG, "Reset preferences");

                // Reset prefs
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, true);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                // Refresh view
                setPreferenceScreen(null);
                addPreferencesFromResource(R.xml.preferences);

                // Setup prefs
                setupSpecialPreferences();

                return true;
            }
        });


    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        } else if (p instanceof EditTextPreference) {
            //updateEditTextPreferenceSummary(p.getKey());
        } else if (p instanceof MultiSelectListPreference) {
            //EditTextPreference editTextPref = (EditTextPreference) p;
            //p.setSummary(editTextPref.getText());
        }
    }



    public static int getUartTextMaxPackets(Context context) {

        return 0;
    }
}

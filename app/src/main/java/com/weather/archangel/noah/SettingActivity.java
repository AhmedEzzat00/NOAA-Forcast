package com.weather.archangel.noah;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        //set the listener to watch for value change
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        //Cast the object to String Value
        String stringValue = o.toString();

        //Check the type of preference
        //List of Preferences
        if (preference instanceof ListPreference) {
            //Cast the preference to the ListPreference
            ListPreference listPreference = (ListPreference) preference;

            //Get the index of the string value associated with the preference
            int index = listPreference.findIndexOfValue(stringValue);
            //if the preference within the scope
            if (index >= 0) {
                //Set the summery to the character sequences starting from index
                preference.setSummary(listPreference.getEntries()[index]);
            }
        } else {
            preference.setSummary(stringValue);
        }
        return true;
    }
}
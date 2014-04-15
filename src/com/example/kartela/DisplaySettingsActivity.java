package com.example.kartela;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

public class DisplaySettingsActivity extends PreferenceFragment{
	
	public void onCreate(Bundle savedInstanceState) {
		Log.d("test","hej");
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
	}
}



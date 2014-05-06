/*
Copyright (c) 2014, Student group C in course TNM082 at Linköpings University
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the {organization} nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.example.kartela;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.app.DialogFragment;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingsActivity extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	public static final String KEY_PREF_MAIL = "pref_mail";
	public static final String KEY_PREF_REMINDER = "pref_sync";
	 
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
 
		 // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);        
	 }
	
	
	@Override
	public void onResume(){
	        super.onResume();
	        // Set up a listener whenever a key changes
	        getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	        
	        if(getPreferenceScreen().getSharedPreferences().contains("pref_mail")){
	        	updatePreference("pref_mail");
	        }
	        
	        if(getPreferenceScreen().getSharedPreferences().contains("pref_sync")){
	        	updatePreference("pref_sync");
	        }
	    }
	 
	    @Override
		public void onPause() {
	        super.onPause();
	        // Unregister the listener whenever a key changes
	        getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	    }
	 
	    @Override
	    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	            String key) {
	        updatePreference(key);
	    }
	    
	    private void updatePreference(String key){
	    		    	
	        if (key.equals(KEY_PREF_MAIL)){
	            Preference preference = findPreference(key);
	            if (preference instanceof EditTextPreference){
	                EditTextPreference editTextPreference =  (EditTextPreference)preference;
	                if (editTextPreference.getText().trim().length() > 0){
	                    editTextPreference.setSummary(editTextPreference.getText());
	                }else{
	                    editTextPreference.setSummary("Enter mejladdress");
	                }
	            }
	        }
	        
	        if (key.equals(KEY_PREF_REMINDER)){
	        	Preference preference = findPreference(key);
	        	
	        	if (preference instanceof MultiSelectListPreference) {
	        		MultiSelectListPreference multiselectlistpreference = (MultiSelectListPreference)preference;
	        		Set<String> selections = multiselectlistpreference.getValues();
	        		List<String> list = new ArrayList<String>(selections);
	        		Collections.sort(list);
	        		String[] days = {"Måndag","Tisdag","Onsdag","Torsdag","Fredag"};	        		
	        		String summary_string = "";
	        		
	        		for (String s: list) {
	        			summary_string = summary_string + " " + days[Integer.parseInt(s)];
	        		}
	        		
	        		// user has chosen day(s) for reminder
	        		if(summary_string != "") {
	        			multiselectlistpreference.setSummary(summary_string);
	        		}
	        		else {
	        			multiselectlistpreference.setSummary("Du har inte valt någon dag för påminnelse.");
	        		}
	        		
	        	}
	        }
	    }	
}
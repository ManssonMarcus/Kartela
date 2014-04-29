package com.example.kartela;

import android.app.DialogFragment;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingsActivity extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	
	 
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 // TODO Auto-generated method stub
		 super.onCreate(savedInstanceState);
		 //setContentView(R.xml.preferences);
 
		 // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
   /*     Preference p = (Preference) findPreference("pref_reminderTime");
        p.setOnPreferenceClickListener(new OnPreferenceClickListener() {
    		public boolean onPreferenceClick(Preference preference) {
    			// TODO Auto-generated method stub
    			DialogFragment dialogFragment = new TimePickerFragment();
    			dialogFragment.show(getFragmentManager(), "timePicker");
    			return false;
    		}
        });*/
        
        
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
	        if (key.equals("pref_mail")){
	        	Log.d("test filter", "nr 1");
	            Preference preference = findPreference(key);
	            if (preference instanceof EditTextPreference){
	            	Log.d("test filter", "nr 2");
	                EditTextPreference editTextPreference =  (EditTextPreference)preference;
	                Log.d("test filter", Integer.toString(editTextPreference.getText().trim().length()));
	                if (editTextPreference.getText().trim().length() > 0){
	                    editTextPreference.setSummary(editTextPreference.getText());
	                }else{
	                	Log.d("test filter", "nr 3");
	                    editTextPreference.setSummary("Enter mejladdress");
	                }
	            }
	        }
	        
	        
	    }



	
	
}
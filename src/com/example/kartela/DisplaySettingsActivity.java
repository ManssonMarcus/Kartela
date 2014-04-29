package com.example.kartela;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

public class DisplaySettingsActivity extends FragmentActivity {	 
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 // TODO Auto-generated method stub
		 super.onCreate(savedInstanceState);
		 //setContentView(R.xml.preferences);
		 
		 getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsActivity()).commit();
	 }
 

}
/*
Copyright (c) 2014, Student group C in course TNM082 at Link�pings University
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class TimeReportActivity extends FragmentActivity implements OnDateSetListener, OnTimeSetListener{
	
	public final static String EXTRA_MESSAGE = "com.example.kartela.MESSAGE";

	private TimelogDataSource datasource;
	private EditText activeTimeID;
	private boolean dateVerified = false;
	private boolean timeVerified = false;
	private Bundle extras; 
	private EditText dateEditText, startEditText, endEditText, breakEditText, commentEditText;
	private TextView changeLabel;
        private Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_report);
		addItemsOnProjectSpinner();
    	   	
    	//initialize database as datasource		
        datasource = new TimelogDataSource(this);
        datasource.open();
        
        //if an existing entry should be updated, Intent contains the current values
        extras = getIntent().getExtras();
        if (extras != null)
        {
        	
        	spinner = (Spinner) findViewById(R.id.projects_spinner);
        	
        	int project_name_index = 0;
        	
        	for (int i = 0; i < spinner.getCount(); i++) {
        		if (spinner.getItemAtPosition(i).equals(extras.get("name"))) {
        			project_name_index = i;
        		}
        	}        	
        	
        	// update selected item on spinner to project for the timelog being edited
        	spinner.setSelection(project_name_index);
        	
        	// date and time has already been verified for the timelog being edited
        	dateVerified = true;
        	timeVerified = true;
        	
        	dateEditText = (EditText) findViewById(R.id.date);
        	dateEditText.setText(extras.getString("date"));
        	
            startEditText = (EditText) findViewById(R.id.startTime);
            startEditText.setText(extras.getString("start"));
            
            endEditText = (EditText) findViewById(R.id.endTime);
            endEditText.setText(extras.getString("end"));
            
            breakEditText = (EditText) findViewById(R.id.breakTime);
            breakEditText.setText(String.valueOf((extras.getInt("bt"))));
            
        	commentEditText = (EditText) findViewById(R.id.comment);
        	commentEditText.setText(extras.getString("comment"));
                changeLabel = (TextView)findViewById(R.id.header_label);
                changeLabel.setText(extras.getString("label"));
        }
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_report, menu);
		return true;
	}

	public void showDatePickerDialog(View view) {

		DialogFragment dialogFragment = new DatePickerFragment();
		dialogFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void showStartTimePickerDialog(View view) {
		activeTimeID = (EditText) findViewById(R.id.startTime);
		Log.d("kartela", "starttid klickat");
		DialogFragment dialogFragment = new TimePickerFragment();
		dialogFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void showEndTimePickerDialog(View view) {
		activeTimeID = (EditText) findViewById(R.id.endTime);
		DialogFragment dialogFragment = new TimePickerFragment();
		dialogFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void addItemsOnProjectSpinner() {
    	spinner = (Spinner) findViewById(R.id.projects_spinner);
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
    	        R.array.projects_array, android.R.layout.simple_spinner_item);
    	// Specify the layout to use when the list of choices appears
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	spinner.setAdapter(adapter);
    }
	
	@SuppressLint("DefaultLocale")
	public void onDateSet(DatePicker view, int year, int month, int day) {
		String monthString, dayString;

		monthString = String.format("%02d", month + 1);
		//Log.d("kartela", "fel" + monthString + " " + month);
		dayString = String.format("%02d", day);
		
		String fullDate = year+"-"+monthString+"-"+dayString;
		
		if(isValidDate(fullDate)) {
			((EditText) findViewById(R.id.date)).setTextColor(getResources().getColor(R.color.button_green));
			((EditText) findViewById(R.id.date)).setText(fullDate);
			
			dateVerified = true;
		}
		else {
			Log.d("kartela", "fel format p� datum: " + fullDate);		
			dateVerified = false;
		}
		
		
        
    }
		
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String timeString = String.format("%02d:%02d", hourOfDay, minute);
		
		EditText startTime = (EditText) findViewById(R.id.startTime);
		EditText endTime = (EditText) findViewById(R.id.endTime);
	
		//l�gg tiden i f�ltet
		activeTimeID.setText(timeString);
		
		if(startTime.getText().toString().length() > 0 && endTime.getText().toString().length() >0 ){
			//Compare the times so that endTime comes after startTime
			int c = compareStringTime(startTime.getText().toString(), endTime.getText().toString());
			
			if(c <= 0)
			{
				((EditText) findViewById(R.id.startTime)).setTextColor(getResources().getColor(R.color.button_green));
				((EditText) findViewById(R.id.endTime)).setTextColor(getResources().getColor(R.color.button_green));
				timeVerified = true;
			}
			else{
				Log.d("kartela", c + "");
				
	    		Context context = getApplicationContext();
	    		CharSequence text = "starttiden m�ste ligga innan sluttiden";
	    		int duration = Toast.LENGTH_SHORT;			
	    		Toast.makeText(context, text, duration).show();
				
				((EditText) findViewById(R.id.startTime)).setTextColor(getResources().getColor(R.color.button_red));
				((EditText) findViewById(R.id.endTime)).setTextColor(getResources().getColor(R.color.button_red));
				
				timeVerified = false;
			}
		}
		
		
	}
	
	public void updateTimeReport(View view) {
		
		long timelogId = extras.getLong("timelogId");
    	Spinner project = (Spinner) findViewById(R.id.projects_spinner);
    	String projectMessage = project.getSelectedItem().toString();
				
		int b = datasource.updateTimelog(datasource.getSpecificTimelog(timelogId), projectMessage, commentEditText.getText().toString(), startEditText.getText().toString(), endEditText.getText().toString(), Integer.parseInt(breakEditText.getText().toString()), true, dateEditText.getText().toString());
		changeLabel.getText().toString();
		Intent intent = new Intent(this, TabLayoutActivity.class);
		intent.putExtra("message","Activity started from updateTimeReport");
		startActivity(intent);
		
	}
	
	
	public void saveTimeReport(View view) {
		
		if (extras != null) {
			updateTimeReport(view);
        }
		else {
			EditText date = (EditText) findViewById(R.id.date);
	    	String dateMessage = date.getText().toString();
	    	
	    	EditText startTime = (EditText) findViewById(R.id.startTime);
	    	String startTimeMessage = startTime.getText().toString();

			ArrayList<String> timeReportItems = new ArrayList<String>();
	    	
			Intent intent = new Intent(this, TabLayoutActivity.class);

	    	EditText endTime = (EditText) findViewById(R.id.endTime);
	    	String endTimeMessage = endTime.getText().toString();

	    	EditText breakTime = (EditText) findViewById(R.id.breakTime);
	    	String breakTimeMessage = breakTime.getText().toString();
	    	if(breakTimeMessage.length()<=0) breakTimeMessage = "0";
	    	
	    	Spinner project = (Spinner) findViewById(R.id.projects_spinner);
	    	String projectMessage = project.getSelectedItem().toString();
	    	
	    	EditText comment =  (EditText) findViewById(R.id.comment);
	    	String commentMessage = comment.getText().toString();
	    	
	    	//L�gg till i databasen om allt �r ok annars skriv ut felmeddelande
	    	if(timeVerified && dateVerified){
	        	Timelog timelog = datasource.createTimelog(projectMessage, commentMessage, startTimeMessage, endTimeMessage, Integer.parseInt(breakTimeMessage), dateMessage);   	
	        	startActivity(intent);	
	    	}
	    	else{
	    		CharSequence text = "";
	    		if(!timeVerified && !dateVerified) {
	        		text = "Du m�ste ange datum, starttid och sluttid";
	    		}
	    		else if(!timeVerified){
	    			text = "Du m�ste ange korrekt starttid och sluttid";
	    		}
	    		else{
	    			text = "Du m�ste ange datum";
	    		}
	    		
	    		Context context = getApplicationContext();

	    		int duration = Toast.LENGTH_SHORT;
			
	    		Toast.makeText(context, text, duration).show();
	    	}	    	

	    	//need to reload the tabview after adding information to the database.
	    	intent.putExtra("message","Activity started from saveTimeReport");
	    	startActivity(intent);
	    	//TabLayoutActivity.tabHost.setCurrentTab(0);
		}		
    }
	
	public void showTimeReports(View view){
		//Intent intent = new Intent(this, DisplayTimeReportActivity.class);
		//startActivity(intent);
		
		//Intent i = new Intent(this, TabLayoutActivity.class);
		//startActivity(i);
		TabLayoutActivity.tabHost.setCurrentTab(0);
		
		
	}
	
	//Hj�lpfunktioner
	private boolean isValidDate(String date)
	{   
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date testDate = null;
	    String errorMessage;
	    try
	    {
	      testDate = sdf.parse(date);
	    } 
	    catch (ParseException e)
	    {
	      errorMessage = "the date you provided is in an invalid date" + " format.";
	      return false;
	    }
	 
	    if (!sdf.format(testDate).equals(date))
	    {
	      errorMessage = "The date that you provided is invalid.";
	      return false;
	    }
	     
	    return true;
	 
	} 
	
	@SuppressLint("SimpleDateFormat")
	private int compareStringTime(String d1, String d2){
        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        int result = 100;
        try {
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            result = date1.compareTo(date2);

        } catch (ParseException e){
            // Exception handling goes here
        }
		return result;
	}

	//Dubbelt bak�tklick f�r att avsluta appen.
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tryck p� tillbaka igen f�r att avsluta", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;                       
            }
        }, 2000);
    }
}


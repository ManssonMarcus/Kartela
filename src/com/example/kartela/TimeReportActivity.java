package com.example.kartela;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
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
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class TimeReportActivity extends FragmentActivity implements OnDateSetListener, OnTimeSetListener{
	
	public final static String EXTRA_MESSAGE = "com.example.kartela.MESSAGE";

	private TimelogDataSource datasource;
	private EditText activeTimeID;
	private boolean dateVerified = false;
	private boolean timeVerified = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_report);
		addItemsOnProjectSpinner();
			
		//Set text on time/datepickers
//		Calendar calendar = Calendar.getInstance();
//		String year = calendar.get(Calendar.YEAR) + "";
//		String month = calendar.get(Calendar.MONTH) + 1 + "";
//		if(month.length() < 2 ) month = "0"+month;
//		String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
//		String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
//		if(hour.length() < 2 ) hour = "0" + hour;
//		String minute = calendar.get(Calendar.MINUTE) + "";
//		if(minute.length() < 2 ) minute = "0" + minute;
//			
//		String dateString = year + "-" + month + '-' + day;
//		EditText dateText = (EditText) findViewById(R.id.date);
//		dateText.setText(dateString);
    	   	
    	//initialize database as datasource		
        datasource = new TimelogDataSource(this);
        datasource.open();
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
    	Spinner spinner = (Spinner) findViewById(R.id.projects_spinner);
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
			Log.d("kartela", "fel format på datum: " + fullDate);		
			dateVerified = false;
		}
		
		
        
    }
		
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String timeString = String.format("%02d:%02d", hourOfDay, minute);
		
		EditText startTime = (EditText) findViewById(R.id.startTime);
		EditText endTime = (EditText) findViewById(R.id.endTime);
	
		//lägg tiden i fältet
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
	    		CharSequence text = "starttiden måste ligga innan sluttiden";
	    		int duration = Toast.LENGTH_SHORT;			
	    		Toast.makeText(context, text, duration).show();
				
				((EditText) findViewById(R.id.startTime)).setTextColor(getResources().getColor(R.color.button_red));
				((EditText) findViewById(R.id.endTime)).setTextColor(getResources().getColor(R.color.button_red));
				
				timeVerified = false;
			}
		}
		
		
	}
	
	
	public void saveTimeReport(View view) {
//		Intent intent = new Intent(this, DisplayTimeReportActivity.class);
    	
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
    	
    	//Lägg till i databasen om allt är ok annars skriv ut felmeddelande
    	if(timeVerified && dateVerified){
        	Timelog timelog = datasource.createTimelog(projectMessage, commentMessage, startTimeMessage, endTimeMessage, Integer.parseInt(breakTimeMessage), dateMessage);   	
        	startActivity(intent);	
    	}
    	else{
    		CharSequence text = "";
    		if(!timeVerified && !dateVerified) {
        		text = "Du måste ange datum, starttid och sluttid";
    		}
    		else if(!timeVerified){
    			text = "Du måste ange korrekt starttid och sluttid";
    		}
    		else{
    			text = "Du måste ange datum";
    		}
    		
    		Context context = getApplicationContext();

    		int duration = Toast.LENGTH_SHORT;
		
    		Toast.makeText(context, text, duration).show();
    	}
    	

    	//need to reload the tabview after adding information to the database.
    	startActivity(intent);
    	//TabLayoutActivity.tabHost.setCurrentTab(0);
    }
	
	public void showTimeReports(View view){
		//Intent intent = new Intent(this, DisplayTimeReportActivity.class);
		//startActivity(intent);
		
		//Intent i = new Intent(this, TabLayoutActivity.class);
		//startActivity(i);
		TabLayoutActivity.tabHost.setCurrentTab(0);
		
		
	}
	
	//Hjälpfunktioner
	private boolean isValidDate(String date)
	{   
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d");
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
	
	public void showSettings(View view){
		Intent intent = new Intent(this, DisplaySettingsActivity.class);
		
		startActivity(intent);
	}
	
	public void showProjectList(View view){
		Intent intent = new Intent(this, ProjectListActivity.class);
		startActivity(intent);
	}

}

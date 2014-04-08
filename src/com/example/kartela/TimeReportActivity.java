package com.example.kartela;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class TimeReportActivity extends FragmentActivity implements OnDateSetListener, OnTimeSetListener{
	
	public final static String EXTRA_MESSAGE = "com.example.kartela.MESSAGE";

	private TimelogDataSource datasource;
	private EditText activeTimeID;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_report);
		addItemsOnProjectSpinner();
		
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
	
	public void onDateSet(DatePicker view, int year, int month, int day) {
		
		String monthString, dayString;

		monthString = String.format("%02d", month);
		dayString = String.format("%02d", day);
		
		((EditText) findViewById(R.id.date)).setText(year+"-"+monthString+"-"+dayString);
        
    }
	
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		String timeString = String.format("%02d:%02d", hourOfDay, minute);
		activeTimeID.setText(timeString);
	}
	
	public void saveTimeReport(View view) {
		ArrayList<String> timeReportItems = new ArrayList<String>();
    	Intent intent = new Intent(this, DisplayTimeReportActivity.class);
    	EditText editText = (EditText) findViewById(R.id.date);
    	String message = editText.getText().toString();

    	EditText editText2 = (EditText) findViewById(R.id.startTime);
    	String message2 = editText2.getText().toString();

    	EditText editText3 = (EditText) findViewById(R.id.endTime);
    	String message3 = editText3.getText().toString();

    	EditText editText4 = (EditText) findViewById(R.id.breakTime);
    	String message4 = editText4.getText().toString();

    	Spinner editText5 = (Spinner) findViewById(R.id.projects_spinner);
    	String message5 = editText5.getSelectedItem().toString();
    	
    	Timelog timelog = datasource.createTimelog(message5, "kommentar", message2,message3,Integer.parseInt(message4),message); 
    	
//    	timeReportItems.add(timelog.getDate());
//    	timeReportItems.add(timelog.getStartTime());
//    	timeReportItems.add(timelog.getEndTime());
//    	timeReportItems.add("" + timelog.getBreakTime());
//    	timeReportItems.add(timelog.getName());
    	
    	intent.putStringArrayListExtra(EXTRA_MESSAGE, timeReportItems);
    	startActivity(intent);
    }

}

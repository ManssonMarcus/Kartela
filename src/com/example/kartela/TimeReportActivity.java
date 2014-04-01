package com.example.kartela;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
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

public class TimeReportActivity extends FragmentActivity implements OnDateSetListener{
	
	public final static String EXTRA_MESSAGE = "com.example.kartela.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_report);
		addItemsOnProjectSpinner();
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
	
	public void showTimePickerDialog(View view) {
		System.out.println("Šr i showTimePickerDialog");
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
    	
    	timeReportItems.add(message);
    	timeReportItems.add(message2);
    	timeReportItems.add(message3);
    	timeReportItems.add(message4);
    	timeReportItems.add(message5);
    	
    	intent.putStringArrayListExtra(EXTRA_MESSAGE, timeReportItems);
    	startActivity(intent);
    }
	


}

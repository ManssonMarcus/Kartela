package com.example.kartela;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import au.com.bytecode.opencsv.CSVWriter;

import com.example.kartela.TimeReportActivity;

public class DisplayTimeReportActivity extends ListActivity {
	private TimelogDataSource datasource;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_time_report);
		
        datasource = new TimelogDataSource(this);
        datasource.open();
        
        List<Timelog> values = datasource.getAllTimelogs();
		
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Timelog> adapter = new ArrayAdapter<Timelog>(this,
            android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
    @Override
    protected void onResume() {
      datasource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      datasource.close();
      super.onPause();
    }
    
    public void sendTimeReportMail(View view) {
    	//List<Timelog> values = datasource.getAllTimelogs();
    	
    	String csvLocation = android.os.Environment.getExternalStorageDirectory().
    	
    	//CSVWriter csvWriter = new CSVWriter(null);
    	
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"kartela.agilen@gmail.com"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Message from Kartela");
		startActivity(Intent.createChooser(emailIntent, "Send email..."));		
	}
}

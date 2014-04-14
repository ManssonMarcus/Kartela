package com.example.kartela;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import au.com.bytecode.opencsv.CSVWriter;


public class DisplayTimeReportActivity extends ListActivity {
	private TimelogDataSource datasource;
	private List<Timelog> values, combitech_values, saab_values, volvo_values, helikopter_values;
	
	@SuppressLint("NewApi")	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_time_report);
		
        datasource = new TimelogDataSource(this);
        datasource.open();
        
        values = datasource.getAllTimelogs();
        combitech_values = datasource.getTimelogsByName("Combitech");
        saab_values = datasource.getTimelogsByName("Saab");
        volvo_values = datasource.getTimelogsByName("Volvo");
        helikopter_values = datasource.getTimelogsByName("Helikopter");
        
        updateProgressBar();
        
        ListAdapter adapter = new ListAdapter(this, values);
        setListAdapter(adapter);
	}
	
	public void onClick(View view) {
	      @SuppressWarnings("unchecked")
	      ArrayAdapter<Timelog> adapter = (ArrayAdapter<Timelog>) getListAdapter();
	      Timelog timelog = null;
		  Log.d("klickade pa ", view.getId() + "");
	      
	      
	      switch (view.getId()) {

			case R.id.deleteall:   	  
			    if (getListAdapter().getCount() > 0) {
			      timelog = (Timelog) getListAdapter().getItem(0);
			      datasource.deleteTimelog(timelog);
			      adapter.remove(timelog);
			    }
			break;

	      }
	      adapter.notifyDataSetChanged();
	    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Log.d("kartela", "" + values.get(position).getName());
		
		String name = values.get(position).getName();
		String start = values.get(position).getStartTime();
		String end = values.get(position).getEndTime();
		int bt = values.get(position).getBreakTime();
		String comment = values.get(position).getComment();
		
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.popupview);//popup view is the layout you created
		
		dialog.setTitle(name);
		
		TextView txt1 = (TextView)dialog.findViewById(R.id.popup_start);
		txt1.setText("Starttid: " + start);
		
		TextView txt2 = (TextView)dialog.findViewById(R.id.popup_end);
		txt2.setText("Sluttid: " + end);
		
		TextView txt3 = (TextView)dialog.findViewById(R.id.popup_break);
		txt3.setText("Rast: " + bt);
		
		TextView txt4 = (TextView)dialog.findViewById(R.id.popup_comment);
		txt4.setText("Kommentar: " + comment);
		
		dialog.show();
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
    	
    	List<Timelog> values = datasource.getAllTimelogs();

    	String csvLocation = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Test.csv";
    	
    	try {
			CSVWriter csvWriter = new CSVWriter(new FileWriter(csvLocation));
			
			List<String[]> data = new ArrayList<String[]>();
			
			// first row: attributes
			data.add(new String[] {"id","name","comment","start time","end time","break","date"});
	    	
			// iterate through all timelog items
	    	for(int i = 0; i < values.size(); i++) {
	    		
	    		if(values.get(i).getEditable()){
	    			//lock item
	    			datasource.lockSpecificTimelog(values.get(i));
	    			
	    			//add item to csv
	    			String id = String.valueOf(values.get(i).getId());
	    			String name = values.get(i).getName();
	    			String comment = values.get(i).getComment();
	    			String startTime = values.get(i).getStartTime();
	    			String endTime = values.get(i).getEndTime();
	    			String breakTime = String.valueOf(values.get(i).getBreakTime());
	    			String date = values.get(i).getDate();
	    			data.add(new String[] {id, name, comment, startTime, endTime, breakTime, date});
	    		}
	    	}
	    	
	    	csvWriter.writeAll(data);
	    	csvWriter.close();
	    	
	    	// Send email with csv file as attachment
	    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + csvLocation));
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"kartela.agilen@gmail.com"});
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Message from Kartela");
			startActivity(Intent.createChooser(emailIntent, "Send email..."));
		} catch (IOException e) {
			e.printStackTrace();
		}
 		
	}
    
    public void updateProgressBar() {
    	
    	int multiple = 100, height = 50;
    	
    	double combitech_sum = 0, saab_sum = 0, volvo_sum = 0, helikopter_sum = 0;
    	double total_sum = 0;
    	
    	for(int i = 0; i < combitech_values.size(); i++) {
    		Log.d("test", "inside for loop");
    		combitech_sum = combitech_sum + combitech_values.get(i).getWorkedTimeInNumbers();
    	}
    	
    	for(int i = 0; i < saab_values.size(); i++) {
    		Log.d("test", "inside for loop");
    		saab_sum = saab_sum + saab_values.get(i).getWorkedTimeInNumbers();
    	}
    	
    	for(int i = 0; i < volvo_values.size(); i++) {
    		Log.d("test", "inside for loop");
    		volvo_sum = volvo_sum + volvo_values.get(i).getWorkedTimeInNumbers();
    	}
    	
    	for(int i = 0; i < helikopter_values.size(); i++) {
    		Log.d("test", "inside for loop");
    		helikopter_sum = helikopter_sum + helikopter_values.get(i).getWorkedTimeInNumbers();
    	}
    	
    	for(int i = 0; i < values.size(); i++) {
    		
    		total_sum = total_sum + values.get(i).getWorkedTimeInNumbers();
    	}
    	
    	double combitech_ratio = combitech_sum/total_sum;
    	double saab_ratio = saab_sum/total_sum;
    	double volvo_ratio = volvo_sum/total_sum;
    	double helikopter_ratio = helikopter_sum/total_sum;
    	
    	TextView combitech = (TextView) findViewById(R.id.progress_combitech);
    	combitech.getLayoutParams().height = height;
    	combitech.getLayoutParams().width = (int)(combitech_ratio*multiple);
    	combitech.requestLayout();
    	
    	TextView saab = (TextView) findViewById(R.id.progress_saab);
    	saab.getLayoutParams().height = height;
    	saab.getLayoutParams().width = (int)(saab_ratio*multiple);
    	saab.requestLayout();
    	
    	TextView volvo = (TextView) findViewById(R.id.progress_volvo);
    	volvo.getLayoutParams().height = height;
    	volvo.getLayoutParams().width = (int)(volvo_ratio*multiple);
    	volvo.requestLayout();    	
    	
    	TextView helikopter = (TextView) findViewById(R.id.progress_helikopter);
    	helikopter.getLayoutParams().height = height;
    	helikopter.getLayoutParams().width = (int)(helikopter_ratio*multiple);
    	helikopter.requestLayout();
    	
    }
}

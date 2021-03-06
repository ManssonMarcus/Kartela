package com.example.kartela;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import au.com.bytecode.opencsv.CSVWriter;


public class DisplayTimeReportActivity extends ListActivity {
	private TimelogDataSource datasource;
	private List<Timelog> values;
	
	@SuppressLint("NewApi")	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_time_report);
		
        datasource = new TimelogDataSource(this);
        datasource.open();
        
        values = datasource.getAllTimelogs();
        
        ListAdapter adapter = new ListAdapter(this, values);
        setListAdapter(adapter);
        
        Button btn = (Button) findViewById(R.id.send_report);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openAlert(v);
			}
		});
	}
	
	public void openAlert(final View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayTimeReportActivity.this);
		
		alertDialogBuilder.setTitle("Vill du skicka in?");
		alertDialogBuilder.setMessage("Alla tider kommer l�sas och det finns ingen m�jlighet att �ndra tider i efterhand.");
		
		alertDialogBuilder.setPositiveButton("Skicka", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sendTimeReportMail(view);
			}
		});
		
		alertDialogBuilder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	
	public void openEditAlert(final ListView l, final View view, final int position, long id) {
		
		String name = values.get(position).getName();
		String start = values.get(position).getStartTime();
		String end = values.get(position).getEndTime();
		int bt = values.get(position).getBreakTime();
		String comment = values.get(position).getComment();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayTimeReportActivity.this);
		
		alertDialogBuilder.setTitle(name);
		alertDialogBuilder.setMessage("Start tid: " + start + "\n" + "Slut tid: " + end + "\n" + "Rast: "  + bt + "\n" + "Kommentar: " + comment);
		
		alertDialogBuilder.setPositiveButton("�ndra", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Context c = getApplicationContext();
				Intent intent = new Intent(c, TimeReportActivity.class);
				intent.putExtra("hej", values.get(position).getId());
				startActivity(intent);
				
			}
		});
		
		alertDialogBuilder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
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
	
	/*
	 * 
	 * 
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		openEditAlert(l,v,position,id);
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

}

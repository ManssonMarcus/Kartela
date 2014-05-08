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
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;


public class DisplayTimeReportActivity extends ListActivity {
	private TimelogDataSource datasource;
	private List<Timelog> values;
	private TextView sum_text_view;
	
	@SuppressLint("NewApi")	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_time_report);
		
        datasource = new TimelogDataSource(this);
        datasource.open();

        //values = datasource.getAllTimelogsByDate();
        values = datasource.getAllEditableTimelogs();
        
     	// get total worked time
        double total_sum = 0;
        
    	for (int i = 0; i < values.size(); i++) {
    		total_sum = total_sum + values.get(i).getWorkedTimeInNumbers();
    	}
    	
  	    // update string for "sum of hours" text view
    	sum_text_view = (TextView) findViewById(R.id.sum_text_view);
    	sum_text_view.setText(datasource.getTimeStringFromMilliSeconds(total_sum));

        ListAdapter adapter = new ListAdapter(this, values);

        setListAdapter(adapter);
        
        Button btn = (Button) findViewById(R.id.send_report);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(haveNetworkConnection()){
					openAlert(v);
				} else {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayTimeReportActivity.this);
					
					alertDialogBuilder.setTitle("OBS!");
					alertDialogBuilder.setMessage("Du kan inte skicka in din rapport utan internet. Var vänligen försök igen när du är uppkopplat till internet.");
					
					alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				
				}
				
			}
		});
	}
	
	public void openAlert(final View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayTimeReportActivity.this);
		
		alertDialogBuilder.setTitle("Vill du skicka in?");
		alertDialogBuilder.setMessage("Alla tider kommer låsas och det finns ingen möjlighet att ändra tider i efterhand.");
		
		alertDialogBuilder.setPositiveButton("Skicka", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
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
		
		final String name = values.get(position).getName();
		final String date = values.get(position).getDate();
		final String start = values.get(position).getStartTime();
		final String end = values.get(position).getEndTime();
		final int bt = values.get(position).getBreakTime();
		final String comment = values.get(position).getComment();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayTimeReportActivity.this);
		
		alertDialogBuilder.setTitle(name);
		alertDialogBuilder.setMessage("Datum: " + date + "\n" + "Start-tid: " + start + "\n" + "Slut-tid: " + end + "\n" + "Rast: "  + bt + "\n" + "Kommentar: " + comment);
		
		// user should only be able to edit timelogs that haven't been locked yet
		if(values.get(position).getEditable()) {
			alertDialogBuilder.setPositiveButton("Ändra", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					Context c = getApplicationContext();
					Intent intent = new Intent(c, TimeReportActivity.class);
					intent.putExtra("name", name);
					intent.putExtra("date", date);
					intent.putExtra("start", start);
					intent.putExtra("end", end);
					intent.putExtra("bt", bt);
					intent.putExtra("comment", comment);
					intent.putExtra("timelogId", values.get(position).getId());
					
					startActivity(intent);
					
				}
			});
		}
		
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
		
		Log.d("test filter","inside onclick");
		
		@SuppressWarnings("unchecked")
		ArrayAdapter<Timelog> adapter = (ArrayAdapter<Timelog>) getListAdapter();
		Timelog timelog = null;	      
		
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
	    values = datasource.getAllEditableTimelogs();
	    
	    // disable 'Skicka rapport' button if there are no editable timelogs, i.e. 'values' is empty
	    if (values.size() == 0) {
	    	Button send_btn = (Button) findViewById(R.id.send_report);
	    	send_btn.setBackgroundColor(getResources().getColor(R.color.medium_grey));
	    	send_btn.setEnabled(false);
	    }
      
	    // get total worked time
	    double total_sum = 0;
      
	  	for (int i = 0; i < values.size(); i++) {
	  		total_sum = total_sum + values.get(i).getWorkedTimeInNumbers();
	  	}
	  	
	    // update string for "sum of hours" text view
	  	sum_text_view = (TextView) findViewById(R.id.sum_text_view);
	  	sum_text_view.setText(datasource.getTimeStringFromMilliSeconds(total_sum));
	
	  	ListAdapter adapter = new ListAdapter(this, values);
	
	  	setListAdapter(adapter);
	  	super.onResume();
    }

    @Override
    protected void onPause() {
      datasource.close();
      super.onPause();
    }
    
    public void sendTimeReportMail(View view) {
    	
    	// Check if there are editable timelogs that should be sent by mail, i.e. 'values' contains something
    	if (values.size() > 0) {
    		
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
    	    	
    	    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	    	String mail = sharedPreferences.getString(SettingsActivity.KEY_PREF_MAIL,"");
    	    	
    	    	// Send email with csv file as attachment
    	    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    			emailIntent.setType("plain/text");
    			emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + csvLocation));
    			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mail});
    			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Message from Kartela");
    			startActivity(Intent.createChooser(emailIntent, "Send email..."));
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    	}
    	else {
    		Toast.makeText(this, "Alla tidsrapporteringar har skickats iväg!", Toast.LENGTH_SHORT).show();
    	}

	}
    
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
  //Dubbelt bakåtklick för att avsluta appen.
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tryck på tillbaka igen för att avsluta", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;                       
            }
        }, 2000);
    }
    

}

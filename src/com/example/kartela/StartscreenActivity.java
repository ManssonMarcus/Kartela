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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class StartscreenActivity extends Activity implements OnClickListener{
	private TimelogDataSource datasource;
	private List<Timelog> values;
	private List<Timelog> allTimelogs;
	private Time time = new Time(); 

	private TextView tvCurrentWeek, tvTimespan;
	private Button btnDecreaseWeek, btnIncreaseWeek;
	private int currentWeeknumber, currentYear;
	private double total_sum = 0;

	private NumberPicker weekPicker;
	private List<String> projects;
	private ArrayList<String> weekdaysArray;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startscreen);
		
		final Context context = this;
	
		//add listeners to buttons
        btnDecreaseWeek = (Button) findViewById(R.id.minus_button);   
        btnDecreaseWeek.setOnClickListener(this);
        
        btnIncreaseWeek = (Button) findViewById(R.id.plus_button);   
        btnIncreaseWeek.setOnClickListener(this);
        
		datasource = new TimelogDataSource(this);
        datasource.open();
		
        time.setToNow();
        currentYear = time.year;
        currentWeeknumber = time.getWeekNumber();
        

        Resources res = getResources();
        projects = datasource.getAllProjects(res);
        
        String timeSpan = updateTimeSpan(currentWeeknumber, currentYear);
        tvTimespan = (TextView)findViewById(R.id.textViewTimespan);
        tvTimespan.setText(timeSpan);
        
        final ListView weekdayList = (ListView)findViewById(R.id.listViewWeekdays);
        weekdaysArray = new ArrayList<String>();
        getCurrentWeekDays(currentWeeknumber, currentYear);
             
        values = datasource.getTimeInterval(currentWeeknumber);
        allTimelogs = datasource.getAllTimelogs();
        
    	// get total worked time this week
    	total_sum = datasource.getWorkTimeByWeek(currentWeeknumber);
    	
    	tvCurrentWeek = (TextView) findViewById(R.id.textViewCurrentWeek);  
    	tvCurrentWeek.setText(datasource.getTimeStringFromMilliSeconds(total_sum));
        
    	StartscreenListAdapter adapter = new StartscreenListAdapter(this, weekdaysArray, currentWeeknumber, currentYear, projects, datasource);
        weekdayList.setAdapter(adapter);
        
    	weekdayList.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
    			String s = (String) weekdayList.getItemAtPosition(myItemInt); 		
    			String date = s.split(" ")[1];
    			//Hämta timeligsen för ett visst datum det datumet finns i Stringen "s";
    			allTimelogs = datasource.getTimelogsByDate(date);
    			
    			if(allTimelogs.size() > 0){
	    			StartscreenPopupAdapter adapter = new StartscreenPopupAdapter(context, allTimelogs);
	    			
	    			AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    			builder.setTitle(s);
	    			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
	    			    public void onClick(DialogInterface dialog, int item) {
	    			    	Timelog t = allTimelogs.get(item);
    			    	
    						Context c = getApplicationContext();
    						Intent intent = new Intent(c, TimeReportActivity.class);
    						intent.putExtra("name", t.getName());
    						intent.putExtra("date", t.getDate());
    						intent.putExtra("start", t.getStartTime());
    						intent.putExtra("end", t.getEndTime());
    						intent.putExtra("bt", t.getBreakTime());
    						intent.putExtra("comment", t.getComment());
    						intent.putExtra("timelogId", t.getId());
    						
    						startActivity(intent); 	
	    			    }
	    			});
	    			
	    			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                           dialog.dismiss();
	                       }
	                   });
	    			
	    			builder.show();
    			}

			}                 
    	});        
	 }
	
	void getWeekDays() {
		
		Calendar c = Calendar.getInstance();

		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);	
		weekdaysArray.add("måndag" + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DAY_OF_MONTH) + "tjena" + c.get(Calendar.DAY_OF_WEEK));
		c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);	
		weekdaysArray.add("tisdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);	
		weekdaysArray.add("onsdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);	
		weekdaysArray.add("torsdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);	
		weekdaysArray.add("fredag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DAY_OF_MONTH));
	}
	
	@SuppressLint("SimpleDateFormat") void getCurrentWeekDays(int v, int y) {
		
		weekdaysArray.clear();
		
		Calendar c = Calendar.getInstance();
		
		String Days[] = {"Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag", "Söndag"};
		
		c.clear();
		c.set(Calendar.WEEK_OF_YEAR, v);
		c.set(Calendar.YEAR, y);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		
		SimpleDateFormat format1 = new SimpleDateFormat("MM-dd");
		Date monday = c.getTime();
        String mondayInString = format1.format(monday);
		
		weekdaysArray.add("Måndag " + c.get(Calendar.YEAR)  + "-" + mondayInString);
		
		c.add(Calendar.DATE, 1);
		Date tuesday = c.getTime();
        String tuesdayInString = format1.format(tuesday);
		weekdaysArray.add("Tisdag " + c.get(Calendar.YEAR)  + "-" + tuesdayInString);
		
		c.add(Calendar.DATE, 1);
		Date wednesday = c.getTime();
        String wednesdayInString = format1.format(wednesday);
		weekdaysArray.add("Onsdag " + c.get(Calendar.YEAR)  + "-" + wednesdayInString);
		
		c.add(Calendar.DATE, 1);
		Date thursday = c.getTime();
        String thursdayInString = format1.format(thursday);
		weekdaysArray.add("Torsdag " + c.get(Calendar.YEAR)  + "-" + thursdayInString);
		
		c.add(Calendar.DATE, 1);
		Date friday = c.getTime();
        String fridayInString = format1.format(friday);
		weekdaysArray.add("Fredag " + c.get(Calendar.YEAR)  + "-" + fridayInString);
		
		ListView weekdayList = (ListView)findViewById(R.id.listViewWeekdays);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, weekdaysArray);
        weekdayList.setAdapter(arrayAdapter);
	}
	
	@Override
	public void onClick(View v) {    	
        switch(v.getId()) {
	        case R.id.minus_button:
	        	if(currentWeeknumber <= 1){
	        		currentWeeknumber = 52;
	        		currentYear --;
	        	}
	        	else{
	        		currentWeeknumber--;
	        	}
	        	tvTimespan.setText(updateTimeSpan(currentWeeknumber, currentYear));
	        	values = datasource.getTimeInterval(currentWeeknumber);
	        	total_sum = datasource.getWorkTimeByWeek(currentWeeknumber);
	        	tvCurrentWeek.setText(datasource.getTimeStringFromMilliSeconds(total_sum));
	        	getCurrentWeekDays(currentWeeknumber, currentYear);
	
	          break;
	        case R.id.plus_button:
	        	if(currentWeeknumber >= 52){
	        		currentWeeknumber = 1;
	        		currentYear++;
	        	}
	        	else{
	        		currentWeeknumber++;        		
	        	}
	        	tvTimespan.setText(updateTimeSpan(currentWeeknumber, currentYear));
	        	values = datasource.getTimeInterval(currentWeeknumber);
	        	total_sum = datasource.getWorkTimeByWeek(currentWeeknumber);
	        	tvCurrentWeek.setText(datasource.getTimeStringFromMilliSeconds(total_sum));
	        	getCurrentWeekDays(currentWeeknumber, currentYear);
	          break;
	      	}
			ListView weekdayList = (ListView)findViewById(R.id.listViewWeekdays);
			StartscreenListAdapter adapter = new StartscreenListAdapter(this, weekdaysArray, currentWeeknumber, currentYear, projects, datasource);
			weekdayList.setAdapter(adapter);
			

			
	}	
	
	private String updateTimeSpan(int v, int y){

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.WEEK_OF_YEAR, v);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);
		
		return "Vecka " + v + " (" + startDateInStr + " - " + endDaString + ")";
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
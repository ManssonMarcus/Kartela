/**
 * Copyright (c) 2014, Kartela
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.example.kartela;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class StartscreenActivity extends Activity implements OnClickListener{
	private TimelogDataSource datasource;
	private List<Timelog> values;
	private Time time = new Time(); 

	private TextView tvCurrentWeek, tvTimespan;
	private Button btnDecreaseWeek, btnIncreaseWeek;
	private int currentWeeknumber;

	private NumberPicker weekPicker;
	private List<String> projects;
	private ArrayList<String> weekdaysArray;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startscreen);
	
		//add lsiteners to buttons
        btnDecreaseWeek = (Button) findViewById(R.id.minus_button);   
        btnDecreaseWeek.setOnClickListener(this);
        
        btnIncreaseWeek = (Button) findViewById(R.id.plus_button);   
        btnIncreaseWeek.setOnClickListener(this);
        
		datasource = new TimelogDataSource(this);
        datasource.open();
		
        time.setToNow();
        
        currentWeeknumber = time.getWeekNumber();

        Resources res = getResources();
        projects = datasource.getAllProjects(res);
        
        tvCurrentWeek = (TextView) findViewById(R.id.textViewCurrentWeek);  
        tvCurrentWeek.setText(Integer.toString(currentWeeknumber));
        
        String timeSpan = updateTimeSpan(currentWeeknumber);
        tvTimespan = (TextView)findViewById(R.id.textViewTimespan);
        tvTimespan.setText(timeSpan);
        
        ListView weekdayList = (ListView)findViewById(R.id.listViewWeekdays);
        weekdaysArray = new ArrayList<String>();
        getWeekDays();
        
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, weekdaysArray);
//        weekdayList.setAdapter(arrayAdapter);
        
        values = datasource.getTimeInterval(currentWeeknumber);
        
        StartscreenListAdapter adapter = new StartscreenListAdapter(this, values, weekdaysArray);
        weekdayList.setAdapter(adapter);

        Log.d("grejs", "before upd. progressbar");
		updateProgressBar();
        Log.d("grejs", "Update progressbar");
	 }
	
	void getWeekDays() {
		
		Calendar c = Calendar.getInstance();

		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);	
		weekdaysArray.add("måndag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE) );
		c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);	
		weekdaysArray.add("tisdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);	
		weekdaysArray.add("onsdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);	
		weekdaysArray.add("torsdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);	
		weekdaysArray.add("fredag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
	}
	
	@Override
	public void onClick(View v) {
        switch(v.getId()) {
        case R.id.minus_button:
        	if(currentWeeknumber <= 1){
        		currentWeeknumber = 52;
        	}
        	else{
        		currentWeeknumber--;
        	}
        	tvTimespan.setText(updateTimeSpan(currentWeeknumber));
        	values = datasource.getTimeInterval(currentWeeknumber);
        	tvCurrentWeek.setText(Integer.toString(currentWeeknumber));

          break;
        case R.id.plus_button:
        	if(currentWeeknumber >= 52){
        		currentWeeknumber = 1;
        	}
        	else{
        		currentWeeknumber++;
        	}
        	tvTimespan.setText(updateTimeSpan(currentWeeknumber));
        	values = datasource.getTimeInterval(currentWeeknumber);
        	tvCurrentWeek.setText(Integer.toString(currentWeeknumber));
          break;
      }
		
	}	
	
	private String updateTimeSpan(int v){
		
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, v);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
        Log.d("kartela", "start " + startDateInStr);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);
        Log.d("kartela", "end " + endDaString);
		
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
    
    public void updateProgressBar() {
    	// set "100 % progress" to screen width
    	Display display = getWindowManager().getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);

    	int paddings = 40;
    	int multiple = size.x - paddings;
    	
    	int height = 100;
    	
    	// temp variables
    	double temp_sum = 0, temp_ratio = 0, total_sum = 0;
    	TextView temp_view;
    	String temp_name;
    	int temp_id;

    	// get total worked time this week
    	for (int i = 0; i < values.size(); i++) {
    		total_sum = total_sum + values.get(i).getWorkedTimeInNumbers();
    	}
    	Log.d("test", Double.toString(total_sum));
    	
    	// update progress bar for each project
    	for(int i = 0; i < projects.size(); i++) {
//        	Log.d("test","projects loop");
    		temp_sum = datasource.getWorkTimeByName(projects.get(i), currentWeeknumber);
    		temp_ratio = (temp_sum/total_sum)*100;
    		
    		// check ratio to round up or down
    		if(temp_ratio - Math.floor(temp_ratio) < 0.5) {
    			temp_ratio = Math.floor(temp_ratio);
    		}
    		else {
    			temp_ratio = Math.ceil(temp_ratio);
    		}
    		
    		temp_name = "progress_" + projects.get(i);
    		temp_id = getResources().getIdentifier(temp_name, "id", getApplicationContext().getPackageName());    		
    		temp_view = (TextView) findViewById(temp_id);
    		
    		// update project textview
    		temp_view.setText(Integer.toString((int)temp_ratio) + " %");
    		temp_view.getLayoutParams().height = height;
    		temp_view.getLayoutParams().width = ((int)(temp_ratio*multiple/100));
    		temp_view.requestLayout();
    		
    		// reset variables
    		temp_sum = 0;
    		temp_ratio = 0;
    	}
    	
    }


}
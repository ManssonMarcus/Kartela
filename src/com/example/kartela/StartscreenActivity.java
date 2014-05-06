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
	private double total_sum = 0;

	private NumberPicker weekPicker;
	private List<String> projects;
	private ArrayList<String> weekdaysArray;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startscreen);
	
		//add listeners to buttons
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
        getCurrentWeekDays(currentWeeknumber);
        
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, weekdaysArray);
//        weekdayList.setAdapter(arrayAdapter);
        
        values = datasource.getTimeInterval(currentWeeknumber);
        
    	// get total worked time this week
    	for (int i = 0; i < values.size(); i++) {
    		total_sum = total_sum + values.get(i).getWorkedTimeInNumbers();
    	}
    	
        
        StartscreenListAdapter adapter = new StartscreenListAdapter(this, values, weekdaysArray, total_sum, projects, datasource);
        weekdayList.setAdapter(adapter);
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
	
	void getCurrentWeekDays(int v) {
		
		weekdaysArray.clear();
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.WEEK_OF_YEAR, v);
		c.add(Calendar.MONTH,1);
		c.add(Calendar.DATE,-1);
					
		weekdaysArray.add("måndag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE) );
		c.add(Calendar.DATE, 1);
		weekdaysArray.add("tisdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		c.add(Calendar.DATE, 1);				
		weekdaysArray.add("onsdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		c.add(Calendar.DATE, 1);
		weekdaysArray.add("torsdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		c.add(Calendar.DATE, 1);	
		weekdaysArray.add("fredag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		
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
	        	}
	        	else{
	        		currentWeeknumber--;
	        	}
	        	tvTimespan.setText(updateTimeSpan(currentWeeknumber));
	        	values = datasource.getTimeInterval(currentWeeknumber);
	        	tvCurrentWeek.setText(Integer.toString(currentWeeknumber));
	        	getCurrentWeekDays(currentWeeknumber);
	
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
	        	getCurrentWeekDays(currentWeeknumber);
	        	
	          break;
        }

        ListView weekdayList = (ListView)findViewById(R.id.listViewWeekdays);
        StartscreenListAdapter adapter = new StartscreenListAdapter(this, values, weekdaysArray, total_sum, projects, datasource);
        weekdayList.setAdapter(adapter);
	}	
	
	private String updateTimeSpan(int v){
		
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, v);
        calendar.add(Calendar.DATE,-1);

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
}
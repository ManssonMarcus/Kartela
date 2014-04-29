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

import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.NumberPicker;
import android.widget.TextView;

public class StartscreenActivity extends Activity implements OnClickListener{
	private TimelogDataSource datasource;
	private List<Timelog> values;
	private Time time = new Time(); 
	private NumberPicker weekPicker;
	private List<String> projects;
	private int currentWeeknumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startscreen);
		
		datasource = new TimelogDataSource(this);
        datasource.open();
		
        time.setToNow(); 
        currentWeeknumber = time.getWeekNumber();

        Resources res = getResources();
        projects = datasource.getAllProjects(res);
        
        Log.d("kartela", "veckonummer " + currentWeeknumber + "");
        
        values = datasource.getTimeInterval(currentWeeknumber);
        
		updateProgressBar();
	 }
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Log.d("kartela", "klickat");
		
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
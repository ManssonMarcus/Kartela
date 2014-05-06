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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera.Size;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StartscreenListAdapter extends ArrayAdapter<String> {
	
	private static final int HOURS_IN_MS = 3600000;
	private final Context context;
  	private final List<Timelog> values;
  	private final ArrayList<String> weekdaysArray;
  	private final List<String> projects;
  	private double total;
  	private final TimelogDataSource datasource;
  	private Time time = new Time();
  	private int currentWeeknumber;

	public StartscreenListAdapter(Context context, List<Timelog> values, ArrayList<String> weekdaysArray, double total_sum, List<String> projects, TimelogDataSource datasource) {
	    super(context, R.layout.day_row_layout, weekdaysArray);
	    this.context = context;
	    this.values = values;
	    this.weekdaysArray = weekdaysArray;
	    this.projects = projects;
	    this.total = total_sum;
	    this.datasource = datasource;
	    time.setToNow();
        this.currentWeeknumber = time.getWeekNumber();
	}
	
	@Override
  	public View getView(int position, View convertView, ViewGroup parent) {
//		Log.d("updprb", Integer.toString(position));
    	
	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.day_row_layout, parent, false);
	    TextView dayTitle = (TextView) convertView.findViewById(R.id.day_title);
	    dayTitle.setText(weekdaysArray.get(position));

	    updateProgressBar(convertView);

//		temp_sum = datasource.getWorkTimeByName(projects.get(i), currentWeeknumber);
//		temp_view.setText(Integer.toString((int)50) + " %");
//		temp_view.getLayoutParams().height = height;
//		temp_view.getLayoutParams().width = ((int)(temp_ratio*multiple/100));

	    return convertView;
	}
	
	public void updateProgressBar(View convertView) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

    	Point size = new Point();
    	display.getSize(size);

    	int paddings = 40;
    	int multiple = size.x - paddings;
    	
    	// temp variables
    	double temp_sum = 0, temp_ratio = 0;
    	TextView temp_view;
    	String temp_name;
    	int temp_id;
    	
    	this.total = HOURS_IN_MS*40;
	    for (int i=0; i<projects.size(); i++) {
    		temp_sum = datasource.getWorkTimeByName(projects.get(i), currentWeeknumber);
    		Log.d("logTime", Double.toString(temp_sum));
    		temp_ratio = (temp_sum/this.total)*100;
    		
    		// check ratio to round up or down
    		if(temp_ratio - Math.floor(temp_ratio) < 0.5) {
    			temp_ratio = Math.floor(temp_ratio);
    		}
    		else {
    			temp_ratio = Math.ceil(temp_ratio);
    		}
    		
			temp_name = "progress_" + projects.get(i);
		    temp_id = context.getResources().getIdentifier(temp_name, "id", context.getPackageName());
	    	temp_view = (TextView) convertView.findViewById(temp_id);

    		// update project textview
	    	Double hours = (double)temp_sum/HOURS_IN_MS;
	    	String str = String.format("%1.2f", hours);
    		temp_view.setText(str + "h");
    		temp_view.getLayoutParams().height = 50;
    		temp_view.getLayoutParams().width = ((int)(temp_ratio*multiple/100));
	    }

	}

}

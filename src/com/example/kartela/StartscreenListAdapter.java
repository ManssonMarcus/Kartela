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
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.text.format.Time;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StartscreenListAdapter extends ArrayAdapter<String> {
	
	private static final int HOURS_IN_MS = 3600000;
	private static final int WORK_TIME_PER_DAY = 8;
	private static final int PADDING = 40;
	private final Context context;
  	private final ArrayList<String> weekdaysArray;
  	private final List<String> projects;
  	private double total;
  	private final TimelogDataSource datasource;
  	private Time time = new Time();

	private WindowManager wm;
	private Display display;
	private Point size;
	private int multiple;
	
	// temp variables
	private double temp_ratio, hours, total_day;
	private double[] temp_sum;
	private TextView temp_view;
	private String temp_name, str;
	private int temp_id;
	
	private Calendar calendar;
	private String date;

	public StartscreenListAdapter(Context context, ArrayList<String> weekdaysArray, int currentWeeknumber, int currentYear, List<String> projects, TimelogDataSource datasource) {
	    super(context, R.layout.day_row_layout, weekdaysArray);
	    this.context = context;
	    this.weekdaysArray = weekdaysArray;
	    this.projects = projects;
	    this.datasource = datasource;
	    time.setToNow();
        
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        size = new Point();
    	display.getSize(size);
    	multiple = size.x - PADDING;
    	
    	calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.WEEK_OF_YEAR, currentWeeknumber);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
	}
	
	@Override
  	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.day_row_layout, parent, false);
	    TextView dayTitle = (TextView) convertView.findViewById(R.id.day_title);
	    dayTitle.setText(weekdaysArray.get(position));
	    
	    date = weekdaysArray.get(position);
	    date = date.split("\\s+")[1];

    	total = HOURS_IN_MS*WORK_TIME_PER_DAY;

	    updateProgressBar(convertView);
	    
	    return convertView;
	}
	
	public void updateProgressBar(View convertView) {
    	temp_sum = new double[projects.size()];
    	temp_ratio = 0;
    	total_day = 0;
    	
	    for (int i=0; i<projects.size(); i++) {
    		temp_sum[i] = datasource.getWorkTimeByNameDate(projects.get(i), date);
    		
    		total_day += temp_sum[i];
	    }
	    
	    for (int i=0; i<projects.size(); i++) {
			temp_name = "progress_" + projects.get(i);
		    temp_id = context.getResources().getIdentifier(temp_name, "id", context.getPackageName());
	    	temp_view = (TextView) convertView.findViewById(temp_id);

    		if (total < total_day) total = total_day;
	    	
    		if (temp_sum[i] != 0) {
	    		temp_ratio = (temp_sum[i]/total)*100;
	    		
	    		// update project textview
		    	hours = (double)temp_sum[i]/HOURS_IN_MS;
		    	str = String.format("%1.2f", hours);
	    		temp_view.setText(str + "h");
	    		temp_view.getLayoutParams().height = 50;
	    		temp_view.getLayoutParams().width = ((int)(temp_ratio*multiple/100));
    		} else {
    			temp_view.getLayoutParams().width = 0;
    		}
	    }
	}

}

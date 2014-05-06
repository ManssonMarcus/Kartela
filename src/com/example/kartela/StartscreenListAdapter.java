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
    	
    	this.total = 3600000*40;
	    for (int i=0; i<projects.size(); i++) {
    		temp_sum = datasource.getWorkTimeByName(projects.get(i), currentWeeknumber);
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
	    	Double hours = (double)temp_sum/3600000;
	    	String str = String.format("%1.2f", hours);
    		temp_view.setText(str + "h");
    		temp_view.getLayoutParams().height = 50;
    		temp_view.getLayoutParams().width = ((int)(temp_ratio*multiple/100));
	    }

	}

}

package com.example.kartela;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartscreenListAdapter extends ArrayAdapter<Timelog> {
	  
	  private final Context context;
	  private final List<Timelog> values;
	  private final ArrayList<String> weekdaysArray;

	public StartscreenListAdapter(Context context, List<Timelog> values, ArrayList<String> weekdaysArray) {
	    super(context, R.layout.day_row_layout, values);
	    this.context = context;
	    this.values = values;
	    this.weekdaysArray = weekdaysArray;
	}
	
	@Override
  	public View getView(int position, View convertView, ViewGroup parent) {

    	// temp variables
    	double temp_sum = 0, temp_ratio = 0, total_sum = 0;
    	TextView temp_view;
    	String temp_name;
    	int temp_id;
    	
		Log.d("grejs", "getView startscreenListAdapter");
	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View rowView = inflater.inflate(R.layout.day_row_layout, parent, false);
	
	    TextView dayTitle = (TextView) rowView.findViewById(R.id.day_title);
	    LinearLayout progressBar = (LinearLayout) rowView.findViewById(R.id.day_progress_bar);
	    
	    dayTitle.setText(weekdaysArray.get(position));
	    
//
//		temp_sum = datasource.getWorkTimeByName(projects.get(i), currentWeeknumber);
//		temp_view.setText(Integer.toString((int)temp_ratio) + " %");
//		temp_view.getLayoutParams().height = height;
//		temp_view.getLayoutParams().width = ((int)(temp_ratio*multiple/100));
//	    
	    return rowView;
	}

}

package com.example.kartela;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Timelog> {
  private final Context context;
  private final List<Timelog> values;
  private List<Timelog> sorted;

  public ListAdapter(Context context, List<Timelog> values) {
    super(context, R.layout.row_layout, values);
    this.context = context;
    this.values = values;
    
    sorted = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    	  
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View rowView = inflater.inflate(R.layout.row_layout, parent, false);

    TextView colorBarTextView = (TextView) rowView.findViewById(R.id.label);
    TextView textView = (TextView) rowView.findViewById(R.id.projectText);
    TextView textView2 = (TextView) rowView.findViewById(R.id.timeText);
    TextView dayLabel = (TextView) rowView.findViewById(R.id.dayLabel);
    View separator = (View) rowView.findViewById(R.id.separator);
    

    String project = sorted.get(position).getName();
    String time =  sorted.get(position).getWorkedTime();
    String comment = sorted.get(position).getComment();
    String date = sorted.get(position).getDate();
     
    if(position > 0){  	
	    if(!date.equals(sorted.get(position-1).getDate())){
	    	dayLabel.setVisibility(View.VISIBLE);
	    	separator.setVisibility(View.VISIBLE);
	    	dayLabel.setText(getDay(date) + " " + date);
	    }
	    else{
	    	dayLabel.setVisibility(View.GONE);
	    	separator.setVisibility(View.GONE);
	    	dayLabel.setText("");
	    }	    
    }
    else{
    	dayLabel.setVisibility(View.VISIBLE);
    	separator.setVisibility(View.VISIBLE);
    	dayLabel.setText(getDay(date) + " " + date);
    }
    
    if(!sorted.get(position).getEditable()){
    	textView.setTextColor(Color.GRAY);
    	textView2.setTextColor(Color.GRAY);
    }
    
    String outProject = project;
    String outTime = time+" ";
       
    textView.setText(outProject);
    textView2.setText(outTime);
  
    if (project.startsWith("Saab")) {
	    colorBarTextView.setBackgroundResource(R.drawable.saab_rect);
    } else if (project.startsWith("Volvo")) {
	    colorBarTextView.setBackgroundResource(R.drawable.volvo_rect);
    } else if (project.startsWith("Helikopter")) {
	    colorBarTextView.setBackgroundResource(R.drawable.helikopter_rect);
    } else if (project.startsWith("Combitech")) {
	    colorBarTextView.setBackgroundResource(R.drawable.combitech_rect);
    }
     
    return rowView;
  }  

  private String getDay(String date){
	  
	Calendar c = Calendar.getInstance();
	SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	Date d = null;
	try {
		d = inFormat.parse(date);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	c.setTime(d);
	
	String[] days = new String[] { "Söndag", "Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag" };
	
	String day = days[c.get(Calendar.DAY_OF_WEEK) - 1];

	  
	return day;
	  
  }
} 
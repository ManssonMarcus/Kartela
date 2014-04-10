package com.example.kartela;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Timelog> {
  private final Context context;
  private final List<Timelog> values;

  public ListAdapter(Context context, List<Timelog> values) {
    super(context, R.layout.row_layout, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    	  
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View rowView = inflater.inflate(R.layout.row_layout, parent, false);

    TextView colorBarTextView = (TextView) rowView.findViewById(R.id.label);
    TextView textView = (TextView) rowView.findViewById(R.id.labelText);

    String project = values.get(position).getName();
    String time =  values.get(position).getWorkedTime();
    String comment = values.get(position).getComment();
    String date = values.get(position).getDate();
    
    if(!values.get(position).getEditable()){
    	textView.setTextColor(Color.GRAY);
    }
    
    String fullString = project + " " + time;
       
    textView.setText(fullString);

    
    
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
} 
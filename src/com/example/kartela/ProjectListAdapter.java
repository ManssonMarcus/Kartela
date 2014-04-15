package com.example.kartela;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProjectListAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final List<String> projects;

  public ProjectListAdapter(Context context, List<String> projects) {
    super(context, R.layout.project_row_layout, projects);
    this.context = context;
    this.projects = projects;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    	  
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View rowView = inflater.inflate(R.layout.project_row_layout, parent, false);

    TextView colorBarTextView = (TextView) rowView.findViewById(R.id.projectLabel);
    TextView textView = (TextView) rowView.findViewById(R.id.projectLabelText);
    
    Log.d("test",Integer.toString(projects.size()));
    
    textView.setText(projects.get(position));
 
    if (projects.get(position).equals("Saab")) {
	    colorBarTextView.setBackgroundResource(R.drawable.saab_rect);
    } else if (projects.get(position).equals("Volvo")) {
    	colorBarTextView.setBackgroundResource(R.drawable.volvo_rect);
    } else if (projects.get(position).equals("Helikopter")) {
    	colorBarTextView.setBackgroundResource(R.drawable.helikopter_rect);
    } else if (projects.get(position).equals("Combitech")) {
    	colorBarTextView.setBackgroundResource(R.drawable.combitech_rect);
    }
    
    return rowView;
  }  
} 
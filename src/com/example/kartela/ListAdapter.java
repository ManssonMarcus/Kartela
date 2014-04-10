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
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.row_layout, parent, false);
    TextView colorBarTextView = (TextView) rowView.findViewById(R.id.label);
    TextView textView = (TextView) rowView.findViewById(R.id.labelText);

    textView.setText(values.get(position).getName());
    
    
    colorBarTextView.setBackgroundResource(R.drawable.shape_rect);

//    String[] colors = context.getResources().getStringArray(R.array.projects_color_array);
//    
//    Log.d("color", ""+Color.parseColor(colors[0]));
    
    return rowView;
  }
} 
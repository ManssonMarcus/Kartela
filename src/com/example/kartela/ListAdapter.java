package com.example.kartela;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Timelog> {
  private final Context context;
  private final List<Timelog> values;

  public ListAdapter(Context context, List<Timelog> values2) {
    super(context, R.layout.row_layout,values2);
    this.context = context;
    this.values = values2;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    
	  
	  
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View rowView = inflater.inflate(R.layout.row_layout, parent, false);
    
    TextView textView = (TextView) rowView.findViewById(R.id.label);
    TextView textView2 = (TextView) rowView.findViewById(R.id.labelText);

    GradientDrawable bgShape = (GradientDrawable)textView.getBackground();
	bgShape.setColor(Color.BLACK);
   
    
    
//    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    textView2.setText(values.get(position).getName() + " " + values.get(position).getWorkedTime());
    Resources res = rowView.getResources();

    
    // Change the icon for Windows and iPhone
    String s = values.get(position).getName();
    textView.setBackgroundResource(R.drawable.shape_rect);
    

    return rowView;
  }
  
  
  
  
} 
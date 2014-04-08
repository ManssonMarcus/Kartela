package com.example.kartela;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final String[] values;

  public ListAdapter(Context context, String[] values) {
    super(context, R.layout.row_layout, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.row_layout, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.label);
    TextView textView2 = (TextView) rowView.findViewById(R.id.labelText);
//    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    textView2.setText(values[position]);
    Resources res = rowView.getResources();
    Drawable shape = res.getDrawable(R.drawable.shape_rect);
    
    
    
    // Change the icon for Windows and iPhone
    String s = values[position];
    if (s.startsWith("Windows7") || s.startsWith("iPhone")
        || s.startsWith("Solaris")) {
    	textView.setBackgroundResource(R.drawable.shape_rect);
    } else {
        textView.setBackgroundResource(R.drawable.shape_rect);
    }

    return rowView;
  }
} 
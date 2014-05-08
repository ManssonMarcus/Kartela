package com.example.kartela;
import java.util.List;

import com.example.kartela.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class StartscreenPopupAdapter extends ArrayAdapter<Timelog>{

	  private final Context context;
	  private final List<Timelog> values;
	  
	  public StartscreenPopupAdapter(Context context, List<Timelog> values) {
		    super(context, R.layout.startscreen_popup, values);
		    this.context = context;
		    this.values = values;

		  }
	

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    	  
	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	    
	    View rowView = inflater.inflate(R.layout.startscreen_popup, parent, false);
	
		TextView projectname = (TextView)rowView.findViewById(R.id.projectname);
		projectname.setText(values.get(position).getName() + " " + values.get(position).getWorkedTime());
		
		TextView timespan = (TextView)rowView.findViewById(R.id.timespan);
		timespan.setText(values.get(position).getStartTime() + "-" + values.get(position).getEndTime());
		
		TextView comments = (TextView)rowView.findViewById(R.id.comments);
		comments.setText(values.get(position).getComment());
		
		TextView paustime = (TextView)rowView.findViewById(R.id.paustime);
		paustime.setText("Paus: " + values.get(position).getBreakTime() + "Minuter");
		
		TextView pnameView = (TextView)rowView.findViewById(R.id.projectname);

	    if (values.get(position).getName().startsWith("Saab")) {
		    pnameView.setBackgroundResource(R.layout.borderfill_saab);
	    } else if (values.get(position).getName().startsWith("Volvo")) {
	    	pnameView.setBackgroundResource(R.layout.borderfill_volvo);
	    } else if (values.get(position).getName().startsWith("Helikopter")) {
	    	pnameView.setBackgroundResource(R.layout.borderfill_helikopter);
	    } else if (values.get(position).getName().startsWith("Combitech")) {
	    	pnameView.setBackgroundResource(R.layout.borderfill);
	    }
	    
	    return rowView;
	  }
}

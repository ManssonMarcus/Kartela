package com.example.kartela;

import java.util.List;



import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayTimeReportActivity extends ListActivity {
	private TimelogDataSource datasource;
	private List<Timelog> values;
	
	@SuppressLint("NewApi")	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_time_report);
		
        datasource = new TimelogDataSource(this);
        datasource.open();
        
        values = datasource.getAllTimelogs();
        
        ListAdapter adapter = new ListAdapter(this, values);
        setListAdapter(adapter);
	}
	
	public void onClick(View view) {
	      @SuppressWarnings("unchecked")
	      ArrayAdapter<Timelog> adapter = (ArrayAdapter<Timelog>) getListAdapter();
	      Timelog timelog = null;
		  Log.d("klickade pa ", view.getId() + "");
	      
	      
	      switch (view.getId()) {
	      case R.id.send:  	  
	    	  
	    	  int result = datasource.lockAllTimelogs();
	        
	    	  adapter.clear();
	    	  
	    	  List<Timelog> values = datasource.getAllTimelogs();
	    	  for(int i=0;i<values.size();i++){
	    		  adapter.add(values.get(i));
	    	  } 	        
	        break;
	      }
	      adapter.notifyDataSetChanged();
	    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Log.d("kartela", "" + values.get(position).getName());
		
		String name = values.get(position).getName();
		String start = values.get(position).getStartTime();
		String end = values.get(position).getEndTime();
		int bt = values.get(position).getBreakTime();
		String comment = values.get(position).getComment();
		
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.popupview);//popup view is the layout you created
		
		dialog.setTitle(name);
		
		TextView txt1 = (TextView)dialog.findViewById(R.id.popup_start);
		txt1.setText(start);
		
		TextView txt2 = (TextView)dialog.findViewById(R.id.popup_end);
		txt2.setText(end);
		
		TextView txt3 = (TextView)dialog.findViewById(R.id.popup_break);
		txt3.setText(bt + "");
		
		TextView txt4 = (TextView)dialog.findViewById(R.id.popup_comment);
		txt4.setText(comment);
		
		dialog.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    protected void onResume() {
      datasource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
      datasource.close();
      super.onPause();
    }
}

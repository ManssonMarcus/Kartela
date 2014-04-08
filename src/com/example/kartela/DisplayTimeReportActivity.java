package com.example.kartela;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kartela.TimeReportActivity;

public class DisplayTimeReportActivity extends ListActivity {
	private TimelogDataSource datasource;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_time_report);
		
        datasource = new TimelogDataSource(this);
        datasource.open();
        
        List<Timelog> values = datasource.getAllTimelogs();
		
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Timelog> adapter = new ArrayAdapter<Timelog>(this,
            android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
	}
	
	public void onClick(View view) {
	      @SuppressWarnings("unchecked")
	      ArrayAdapter<Timelog> adapter = (ArrayAdapter<Timelog>) getListAdapter();
	      Timelog timelog = null;
	      switch (view.getId()) {
	      case R.id.testknapp:  	  
	        // save the new new timelog to the database    	
	        int test = datasource.lockAllTimelogs();
	        System.out.println("result: " + test);
	    	  adapter.clear();
	    	  
	    	  List<Timelog> values = datasource.getAllTimelogs();
	    	  for(int i=0;i<values.size();i++){
	    		  adapter.add(values.get(i));
	    	  }  
	        
	        break;
			case R.id.deleteall:   	  
			    if (getListAdapter().getCount() > 0) {
			      timelog = (Timelog) getListAdapter().getItem(0);
			      datasource.deleteTimelog(timelog);
			      adapter.remove(timelog);
			    }
			break;
	      }
	      adapter.notifyDataSetChanged();
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

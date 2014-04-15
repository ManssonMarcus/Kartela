package com.example.kartela;

import java.util.List;

import android.app.ListActivity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.TextView;

public class ProjectListActivity extends ListActivity {
	private TimelogDataSource datasource;
	private List<String> projects;
	private List<Timelog> values;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_list);
		
		datasource = new TimelogDataSource(this);
        datasource.open();
        
        values = datasource.getAllTimelogs();
        
        Resources res = getResources();
        
        projects = datasource.getAllProjects(res);
		
		updateProgressBar();
		
		ProjectListAdapter adapter = new ProjectListAdapter(this, projects);
        setListAdapter(adapter);
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
	
	public void updateProgressBar() {
	    	
	    	// set "100 % progress" to screen width
	    	Display display = getWindowManager().getDefaultDisplay();
	    	Point size = new Point();
	    	display.getSize(size);
	    	Log.d("test",Integer.toString(size.x));
	    	int paddings = 40;
	    	int multiple = size.x - paddings;
	    	
	    	int height = 100;
	    	
	    	// temp variables
	    	double temp_sum = 0, temp_ratio = 0, total_sum = 0;
	    	TextView temp_view;
	    	String temp_name;
	    	int temp_id;
	    	
	    	// get total worked time
	    	for (int i = 0; i < values.size(); i++) {
	    		total_sum = total_sum + values.get(i).getWorkedTimeInNumbers();
	    	}    	
	    	
	    	// update progress bar for each project
	    	for(int i = 0; i < projects.size(); i++) {
	    		temp_sum = datasource.getWorkTimeByName(projects.get(i));
	    		temp_ratio = (temp_sum/total_sum)*100;
	    		
	    		// check ratio to round up or down
	    		if(temp_ratio - Math.floor(temp_ratio) < 0.5) {
	    			temp_ratio = Math.floor(temp_ratio);
	    		}
	    		else {
	    			temp_ratio = Math.ceil(temp_ratio);
	    		}
	    		
	    		temp_name = "progress_" + projects.get(i);    		
	    		temp_id = getResources().getIdentifier(temp_name, "id", getApplicationContext().getPackageName());    		
	    		temp_view = (TextView) findViewById(temp_id);
	    		
	    		// update project textview
	    		temp_view.setText(Integer.toString((int)temp_ratio) + " %");
	    		temp_view.getLayoutParams().height = height;
	    		temp_view.getLayoutParams().width = ((int)(temp_ratio*multiple/100));
	    		temp_view.requestLayout();
	    		
	    		// reset variables
	    		temp_sum = 0;
	    		temp_ratio = 0;
	    	}
	    	
	    }
	
}
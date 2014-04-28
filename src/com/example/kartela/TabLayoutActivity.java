package com.example.kartela;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabLayoutActivity extends TabActivity {
    /** Called when the activity is first created. */
	
	static TabHost tabHost;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);
         
        tabHost = getTabHost();
         

         
        // Tab for displaying time reports
        TabSpec startspec = tabHost.newTabSpec("Start");        
        startspec.setIndicator("");
        Intent startIntent = new Intent(this, StartscreenActivity.class);
        startspec.setContent(startIntent);
        
        // Tab for reporting time
        TabSpec timespec = tabHost.newTabSpec("TimeReport");
        // setting Title and Icon for the Tab
        timespec.setIndicator("");
        Intent timeReportIntent = new Intent(this, TimeReportActivity.class);
        timespec.setContent(timeReportIntent);
         
        TabSpec projectspec = tabHost.newTabSpec("Project");        
        projectspec.setIndicator("");
        Intent projectIntent = new Intent(this, ProjectListActivity.class);
        projectspec.setContent(projectIntent);
        
        
        TabSpec reportspec = tabHost.newTabSpec("Send");        
        reportspec.setIndicator("");
        Intent reportIntent = new Intent(this, DisplayTimeReportActivity.class);
        reportspec.setContent(reportIntent);
        
        TabSpec settingsspec = tabHost.newTabSpec("Settings");        
        settingsspec.setIndicator("");
        Intent settingsIntent = new Intent(this, DisplaySettingsActivity.class);
        settingsspec.setContent(settingsIntent);
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(startspec); 
        
        //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.LTGRAY); 
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.clock);
        tabHost.addTab(timespec); 
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.addtime);
        tabHost.addTab(projectspec);
        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.projektknapp);
        tabHost.addTab(reportspec);
        tabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.paperplane);
        tabHost.addTab(settingsspec);
        tabHost.getTabWidget().getChildAt(4).setBackgroundResource(R.drawable.settings);
    }
    
    
}

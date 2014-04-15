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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);
         
        TabHost tabHost = getTabHost();
         

         
        // Tab for displaying time reports
        TabSpec displayspec = tabHost.newTabSpec("Display");        
        displayspec.setIndicator("");
        Intent displayIntent = new Intent(this, DisplayTimeReportActivity.class);
        displayspec.setContent(displayIntent);
        
        // Tab for reporting time
        TabSpec timespec = tabHost.newTabSpec("TimeReport");
        // setting Title and Icon for the Tab
        timespec.setIndicator("");
        Intent timeReportIntent = new Intent(this, TimeReportActivity.class);
        timespec.setContent(timeReportIntent);
         
        TabSpec projectspec = tabHost.newTabSpec("Project");        
        projectspec.setIndicator("");
        Intent projectIntent = new Intent(this, DisplayTimeReportActivity.class);
        projectspec.setContent(projectIntent);
        
        TabSpec reportspec = tabHost.newTabSpec("Send");        
        reportspec.setIndicator("");
        Intent reportIntent = new Intent(this, TimeReportActivity.class);
        reportspec.setContent(reportIntent);
        
        TabSpec settingsspec = tabHost.newTabSpec("Settings");        
        settingsspec.setIndicator("");
        Intent settingsIntent = new Intent(this, DisplayTimeReportActivity.class);
        settingsspec.setContent(settingsIntent);
         
        // Adding all TabSpec to TabHost
        
        tabHost.addTab(displayspec); 
        
        //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.LTGRAY); 
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.ic_action_accept);
        tabHost.addTab(timespec); 
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.ic_action_edit);
        tabHost.addTab(projectspec);
        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.ic_action_labels);
        tabHost.addTab(reportspec);
        tabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.ic_action_email);
        tabHost.addTab(settingsspec);
        tabHost.getTabWidget().getChildAt(4).setBackgroundResource(R.drawable.ic_action_settings);
        
        
        tabHost.setCurrentTab(0);
        
    }
}

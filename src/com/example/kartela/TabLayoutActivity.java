package com.example.kartela;

import android.app.TabActivity;
import android.content.Intent;
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
        displayspec.setIndicator("Min tid");
        Intent displayIntent = new Intent(this, DisplayTimeReportActivity.class);
        displayspec.setContent(displayIntent);
        
        // Tab for reporting time
        TabSpec timespec = tabHost.newTabSpec("TimeReport");
        // setting Title and Icon for the Tab
        timespec.setIndicator("Lägg in tid");
        Intent timeReportIntent = new Intent(this, TimeReportActivity.class);
        timespec.setContent(timeReportIntent);
         
        TabSpec projectspec = tabHost.newTabSpec("Project");        
        projectspec.setIndicator("Projekt");
        Intent projectIntent = new Intent(this, DisplayTimeReportActivity.class);
        projectspec.setContent(projectIntent);
        
        TabSpec reportspec = tabHost.newTabSpec("Send");        
        reportspec.setIndicator("Skicka in");
        Intent reportIntent = new Intent(this, TimeReportActivity.class);
        reportspec.setContent(reportIntent);
        
        TabSpec settingsspec = tabHost.newTabSpec("Settings");        
        settingsspec.setIndicator("Inställningar");
        Intent settingsIntent = new Intent(this, DisplayTimeReportActivity.class);
        settingsspec.setContent(settingsIntent);
         
        // Adding all TabSpec to TabHost
        
        tabHost.addTab(displayspec); 
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.ic_action_cloud);
        tabHost.addTab(timespec); 
        tabHost.addTab(projectspec);
        tabHost.addTab(reportspec);
        tabHost.addTab(settingsspec);
        
        tabHost.setCurrentTab(0);
        
    }
}

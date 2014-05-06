/*
Copyright (c) 2014, Student group C in course TNM082 at Linköpings University
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the {organization} nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.example.kartela;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabLayoutActivity extends TabActivity {
    /** Called when the activity is first created. */
	
	static TabHost tabHost;
	
	protected void onStart(){
		super.onStart();
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFFFFF"));
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);
         
        tabHost = getTabHost();

        // Tab for displaying time reports
        TabSpec startspec = tabHost.newTabSpec("Start");        
		startspec.setIndicator("", getResources().getDrawable(R.drawable.icon_clock_tab));
        Intent startIntent = new Intent(this, StartscreenActivity.class);
        startspec.setContent(startIntent);

        
        TabSpec timespec = tabHost.newTabSpec("TimeReport");        
        timespec.setIndicator("", getResources().getDrawable(R.drawable.icon_addtime_tab));
        Intent timeReportIntent = new Intent(this, TimeReportActivity.class);
        timespec.setContent(timeReportIntent);
         
        TabSpec projectspec = tabHost.newTabSpec("Project");
        projectspec.setIndicator("", getResources().getDrawable(R.drawable.icon_projektknapp_tab));
        Intent projectIntent = new Intent(this, ProjectListActivity.class);
        projectspec.setContent(projectIntent);
        
        TabSpec reportspec = tabHost.newTabSpec("Send");
        reportspec.setIndicator("", getResources().getDrawable(R.drawable.icon_paperplane_tab));
        Intent reportIntent = new Intent(this, DisplayTimeReportActivity.class);
        reportspec.setContent(reportIntent);
        
        TabSpec settingsspec = tabHost.newTabSpec("Settings");
        settingsspec.setIndicator("", getResources().getDrawable(R.drawable.icon_settings_tab));
        Intent settingsIntent = new Intent(this, DisplaySettingsActivity.class);
        settingsspec.setContent(settingsIntent);
         

        // Adding all TabSpec to TabHost
        tabHost.addTab(startspec); 
        tabHost.addTab(timespec); 
        tabHost.addTab(projectspec);
        tabHost.addTab(reportspec);
        tabHost.addTab(settingsspec);
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

//        	@Override
        	public void onTabChanged(String tabId) {

	        	for(int j=0;j<tabHost.getTabWidget().getChildCount();j++)
	            {
	        		tabHost.getTabWidget().getChildAt(j).setBackgroundColor(Color.parseColor("#999999")); //unselected
	            }
	        	tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFFFFF"));

        	}
        });
    }   
    
}

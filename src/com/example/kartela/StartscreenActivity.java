package com.example.kartela;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.NumberPicker;

public class StartscreenActivity extends Activity implements OnClickListener{
	private TimelogDataSource datasource;
	private List<Timelog> values;
	private Time time = new Time(); 
	private NumberPicker weekPicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startscreen);
		
		
		
		datasource = new TimelogDataSource(this);
        datasource.open();
		
        time.setToNow(); 
        int currentWeeknumber = time.getWeekNumber();
        
        
        
        Log.d("kartela", "veckonummer " + currentWeeknumber + "");
        
        values = datasource.getTimeInterval(currentWeeknumber);
	 }
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Log.d("kartela", "klickat");
		
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
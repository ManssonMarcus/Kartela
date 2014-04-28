package com.example.kartela;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;

public class StartscreenActivity extends Activity implements OnClickListener{
	private TimelogDataSource datasource;
	private List<Timelog> values;
	private Time time = new Time(); 
	private NumberPicker weekPicker;
	private ArrayList<String> weekdaysArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startscreen);
		
		
		
		datasource = new TimelogDataSource(this);
        datasource.open();
		
        time.setToNow(); 
        int currentWeeknumber = time.getWeekNumber();
        
        ListView weekdayList = (ListView)findViewById(R.id.listViewWeekdays);
        weekdaysArray = new ArrayList<String>();
        getWeekDays();
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, weekdaysArray);
        weekdayList.setAdapter(arrayAdapter);

        Log.d("kartela", "veckonummer " + currentWeeknumber + "");
        
        values = datasource.getTimeInterval(currentWeeknumber);
	 }
	
	void getWeekDays() {
		
		Calendar c = Calendar.getInstance();

		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);	
		weekdaysArray.add("måndag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE) );
		c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);	
		weekdaysArray.add("tisdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);	
		weekdaysArray.add("onsdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);	
		weekdaysArray.add("torsdag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);	
		weekdaysArray.add("fredag " + c.get(Calendar.YEAR)  + "-" + c.get(Calendar.MONTH) + "-" +c.get(Calendar.DATE));
		
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
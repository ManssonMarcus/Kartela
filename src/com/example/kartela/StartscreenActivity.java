package com.example.kartela;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

import android.widget.Button;
import android.widget.EditText;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.NumberPicker;
import android.widget.TextView;

public class StartscreenActivity extends Activity implements OnClickListener{
	private TimelogDataSource datasource;
	private List<Timelog> values;
	private Time time = new Time(); 

	private TextView tvCurrentWeek, tvTimespan;
	private Button btnDecreaseWeek, btnIncreaseWeek;
	private int currentWeeknumber;

	private NumberPicker weekPicker;
	private ArrayList<String> weekdaysArray;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startscreen);
	
		//add lsiteners to buttons
        btnDecreaseWeek = (Button) findViewById(R.id.minus_button);   
        btnDecreaseWeek.setOnClickListener(this);
        
        btnIncreaseWeek = (Button) findViewById(R.id.plus_button);   
        btnIncreaseWeek.setOnClickListener(this);
        
		datasource = new TimelogDataSource(this);
        datasource.open();
		
        time.setToNow(); 

        currentWeeknumber = time.getWeekNumber();
        
        tvCurrentWeek = (TextView) findViewById(R.id.textViewCurrentWeek);  
        tvCurrentWeek.setText(Integer.toString(currentWeeknumber));
        
        String timeSpan = updateTimeSpan(currentWeeknumber);
        tvTimespan = (TextView)findViewById(R.id.textViewTimespan);
        tvTimespan.setText(timeSpan);
                

        int currentWeeknumber1 = time.getWeekNumber();
        
        ListView weekdayList = (ListView)findViewById(R.id.listViewWeekdays);
        weekdaysArray = new ArrayList<String>();
        getWeekDays();
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, weekdaysArray);
        weekdayList.setAdapter(arrayAdapter);
        
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
	public void onClick(View v) {
        switch(v.getId()) {
        case R.id.minus_button:
        	if(currentWeeknumber <= 1){
        		currentWeeknumber = 52;
        	}
        	else{
        		currentWeeknumber--;
        	}
        	tvTimespan.setText(updateTimeSpan(currentWeeknumber));
        	values = datasource.getTimeInterval(currentWeeknumber);
        	tvCurrentWeek.setText(Integer.toString(currentWeeknumber));

          break;
        case R.id.plus_button:
        	if(currentWeeknumber >= 52){
        		currentWeeknumber = 1;
        	}
        	else{
        		currentWeeknumber++;
        	}
        	tvTimespan.setText(updateTimeSpan(currentWeeknumber));
        	values = datasource.getTimeInterval(currentWeeknumber);
        	tvCurrentWeek.setText(Integer.toString(currentWeeknumber));
          break;
      }
		
	}	
	
	private String updateTimeSpan(int v){
		
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, v);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
        Log.d("kartela", "start " + startDateInStr);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);
        Log.d("kartela", "end " + endDaString);
		
		return "Vecka " + v + " (" + startDateInStr + " - " + endDaString + ")";
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
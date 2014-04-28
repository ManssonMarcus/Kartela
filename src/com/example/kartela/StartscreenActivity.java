package com.example.kartela;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class StartscreenActivity extends Activity implements OnClickListener{
	private TimelogDataSource datasource;
	private List<Timelog> values;
	private Time time = new Time(); 
	private TextView tvCurrentWeek, tvTimespan;
	private Button btnDecreaseWeek, btnIncreaseWeek;
	private int currentWeeknumber;
	
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
                
        values = datasource.getTimeInterval(currentWeeknumber);
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
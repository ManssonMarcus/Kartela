package com.example.kartela;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class NotificationActivity extends Activity{
    /** Called when the activity is first created. */
    	TimePicker timePicker;
        DatePicker datePicker;
     
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.notifications_layout);
            
            /*Log.d("QuizLogTag", "i oncreate");
            
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            TimePreference p = new TimePreference(this);
            String s=(String) p.getSummary();
            Log.d("QuizLogTag",s);
            int hours=p.getHour();
            int minutes=p.getMinute();
            Log.d("QuizLogTag",hours+"");
            Log.d("QuizLogTag",minutes+"");*/
     
            //---Button view---
            Button btnOpen = (Button) findViewById(R.id.btnSetAlarm);
            btnOpen.setOnClickListener(new View.OnClickListener() {
     
                public void onClick(View v) {                
                	timePicker = (TimePicker) findViewById(R.id.timePicker);
                    datePicker = (DatePicker) findViewById(R.id.datePicker);                   
     
                    //---use the AlarmManager to trigger an alarm---
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);                 
     
                    //---get current date and time---
                    Calendar calendar = Calendar.getInstance();   
                    
                    //Preference pTime = findPreference("settings_time");
                    
                    
                    
                    /*String time = sharedPreferences.getString(SettingsActivity.PREF_TIME,"");
                    Log.d("QuizLogTag","NEAr");
                    Log.d("QuizLogTag",time);*/
     
                    //---sets the time for the alarm to trigger---
                    calendar.set(Calendar.YEAR, datePicker.getYear());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());                 
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    calendar.set(Calendar.SECOND, 0);
                    
     
                    //---PendingIntent to launch activity when the alarm triggers---
                    Intent i = new Intent("com.example.kartela.NotificationDisplay");
     
                    //---assign an ID of 1---
                    i.putExtra("NotifID", 1);                                
     
                    PendingIntent displayIntent = PendingIntent.getActivity(
                        getBaseContext(), 0, i, 0);               
     
                    //---sets the alarm to trigger---
                    alarmManager.set(AlarmManager.RTC_WAKEUP, 
                        calendar.getTimeInMillis(), displayIntent);
                }
            }); 
        }
        public void sendMessage(View view) 
        {
            Intent intent = new Intent(NotificationActivity.this, NotificationDetails.class);
            startActivity(intent);
        }
}

package com.example.kartela;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;

public class NotificationDetails extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationdetails);
 
        //---look up the notification manager service---
        NotificationManager nm = (NotificationManager) 
            getSystemService(NOTIFICATION_SERVICE);
 
        //---cancel the notification---
        nm.cancel(getIntent().getExtras().getInt("NotifID"));
        
        Intent intent = new Intent(this, TabLayoutActivity.class);
		//intent.putExtra("message","Activity started from updateTimeReport");
		startActivity(intent);
        
    }
}
package com.example.kartela;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

public class NotificationDisplay extends Activity{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        //---get the notification ID for the notification; 
        // passed in by the MainActivity---
        int notifID = getIntent().getExtras().getInt("NotifID");
 
        //---PendingIntent to launch activity if the user selects 
        // the notification---
        Intent i = new Intent("com.example.kartela.NotificationDetails");
        i.putExtra("NotifID", notifID);  
 
        PendingIntent detailsIntent = 
            PendingIntent.getActivity(this, 0, i, 0);
 
        NotificationManager nm = (NotificationManager)
            getSystemService(NOTIFICATION_SERVICE);
        Notification notif = new Notification(
            R.drawable.logga, 
            "Kartela - Rapportera arbetstider",
            System.currentTimeMillis());
 
        CharSequence from = "Kartela";
        CharSequence message = "Rapportera dina arbetade tider";        
        notif.setLatestEventInfo(this, from, message, detailsIntent);
 
        //---100ms delay, vibrate for 250ms, pause for 100 ms and
        // then vibrate for 500ms---
        notif.vibrate = new long[] { 100, 250, 100, 500};        
        nm.notify(notifID, notif);
        //---destroy the activity---
        finish();
    }
}


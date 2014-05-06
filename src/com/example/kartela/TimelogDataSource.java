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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TimelogDataSource {
	
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_PROJECTNAME, MySQLiteHelper.COLUMN_COMMENT,
									MySQLiteHelper.COLUMN_STARTTIME, MySQLiteHelper.COLUMN_ENDTIME, MySQLiteHelper.COLUMN_BREAKTIME,
									MySQLiteHelper.COLUMN_EDITABLE, MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_COLOR};
	
	//Constructor
	public TimelogDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	 
	//Create a timelog
	public Timelog createTimelog(String p_name, String p_comment, String p_startTime, String p_endTime, int p_breakTime, String p_date){
		
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_PROJECTNAME, p_name);
		values.put(MySQLiteHelper.COLUMN_COMMENT, p_comment);
		values.put(MySQLiteHelper.COLUMN_STARTTIME, p_startTime);
		values.put(MySQLiteHelper.COLUMN_ENDTIME, p_endTime);
		values.put(MySQLiteHelper.COLUMN_BREAKTIME, p_breakTime);
		values.put(MySQLiteHelper.COLUMN_EDITABLE, true);
		values.put(MySQLiteHelper.COLUMN_DATE, p_date);
		values.put(MySQLiteHelper.COLUMN_COLOR, "black");
	  		
		long insertId = database.insert(MySQLiteHelper.TABLE_TIMELOGS, null,values);
	  
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TIMELOGS,
			allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
			null, null, null);
		
	    cursor.moveToFirst();
	    Timelog newTimelog = cursorToTimelog(cursor);
	    cursor.close();
	    
	    return newTimelog;
	}
		
	//delete a timelog 
	public void deleteTimelog(Timelog timelog) {
		long id = timelog.getId();
	    System.out.println("Timelog deleted with id: " + id);
	    database.delete(MySQLiteHelper.TABLE_TIMELOGS, MySQLiteHelper.COLUMN_ID
		+ " = " + id, null);
	}
	
	//get all timelogs with the same projectname
	public List<Timelog> getTimelogsByName(String p_name){
    	if(p_name.length()<=0){
    		return getAllTimelogs();
    	}
    	
		List<Timelog> timeLogs = new ArrayList<Timelog>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TIMELOGS, null, MySQLiteHelper.COLUMN_PROJECTNAME + " = ?", 
	    				new String[] { p_name }, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	        Timelog timelog = cursorToTimelog(cursor);
	        timeLogs.add(timelog);
	    	cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    
	    return timeLogs;
	}
	
	public double getWorkTimeByName(String p_name) {
		return getWorkTimeByName(p_name, null);
	}
	
	public double getWorkTimeByName(String p_name, Integer week) {
		List<Timelog> timeLogs;
		
		if (week == null) {
			timeLogs = getTimelogsByName(p_name);
		} else {
			timeLogs = getTimeInterval(week, p_name);
		}

//		Log.d("logTime", timeLogs.get(0).getDate());
		double sum = 0;
		
		for(int i = 0; i < timeLogs.size(); i++) {
			sum = sum + timeLogs.get(i).getWorkedTimeInNumbers();
    	}
		
		return sum;
	}
	
	public Timelog getSpecificTimelog(long timelogId) {
    	
    	Timelog timelog = new Timelog();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TIMELOGS, null, null, null, null, null, null);
	    cursor.moveToFirst();
	    
	    while (!cursor.isAfterLast()) {
	        timelog = cursorToTimelog(cursor);
	        if(timelog.getId() == timelogId) {
	        	return timelog;
	        }
	        cursor.moveToNext();
	    }
	    
	    // make sure to close the cursor
	    cursor.close();
	    
	    return timelog;
    }
	
	//Returns a list of all timelogs in database
    public List<Timelog> getAllTimelogs() {
    	List<Timelog> allTimelogs = new ArrayList<Timelog>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TIMELOGS, null, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	        Timelog timelog = cursorToTimelog(cursor);
	        allTimelogs.add(timelog);
	    	cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    
	    return allTimelogs;
    }

	//Returns a list of all timelogs in database
    public List<Timelog> getAllTimelogsByDate() {
    	List<Timelog> allTimelogs = new ArrayList<Timelog>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TIMELOGS, null, null, null, null, null, MySQLiteHelper.COLUMN_DATE);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	        Timelog timelog = cursorToTimelog(cursor);
	        allTimelogs.add(timelog);
	    	cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    
	    return allTimelogs;
    }
    
    //Returns timelogs by weeknumber
    @SuppressLint("SimpleDateFormat")
	public List<Timelog> getTimeInterval(int weeknumber){
    	return getTimeInterval(weeknumber, null);
    }
    
    public List<Timelog> getTimeInterval(int weeknumber, String p_name) {    	
    	List<Timelog> returnTimelogs = new ArrayList<Timelog>();
    	List<Timelog> allTimelogs;
    	
    	if (p_name == null) {
    		allTimelogs = getAllTimelogs();
    	} else {
    		allTimelogs = getTimelogsByName(p_name);
    	}
    	
    	// Get calendar, clear it and set week number and year.
    	Calendar calendar = Calendar.getInstance();
    	 	
    	//calendar.set(Calendar.WEEK_OF_YEAR, weeknumber);
    	String pattern = "yyyy-MM-dd";
    	for(int i = 0; i < allTimelogs.size();i++){
    		//Log.d("kartela", allTimelogs.get(i).getDate());
    		
    		try {
    			calendar.setFirstDayOfWeek(Calendar.MONDAY);
    			SimpleDateFormat df = new SimpleDateFormat(pattern);
				Date date = df.parse(allTimelogs.get(i).getDate() + " " + allTimelogs.get(i).getEndTime());
				calendar.setTime(date);
				
				if(calendar.get(Calendar.WEEK_OF_YEAR) == weeknumber){
					returnTimelogs.add(allTimelogs.get(i));
				}
				
//				Log.d("kartela", "datum: " + allTimelogs.get(i).getDate() + " " + allTimelogs.get(i).getEndTime());
//				Log.d("kartela", "date: " + date);
//				Log.d("kartela", "vecko-nr: " +  calendar.get(Calendar.WEEK_OF_YEAR) + "");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.d("kartela", "BUGG");
				e.printStackTrace();
			}
    		
    		
    	}	
    	return returnTimelogs;
    }
    
    public List<String> getAllProjects(Resources res) {
    	String[] temp = res.getStringArray(R.array.projects_array);
    	List<String> projects = new ArrayList<String>();
    	
    	for(int i = 0; i < temp.length; i++) {
    		projects.add(temp[i]);
    	}
    	
    	return projects;
    }
    
	public int updateTimelog(Timelog timelog, String p_name, String p_comment, String p_startTime, String p_endTime, int p_breakTime, boolean p_editable, String p_date){
		long id = timelog.getId();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ID, id);
		values.put(MySQLiteHelper.COLUMN_PROJECTNAME, p_name);
		values.put(MySQLiteHelper.COLUMN_COMMENT, p_comment);
		values.put(MySQLiteHelper.COLUMN_STARTTIME, p_startTime);
		values.put(MySQLiteHelper.COLUMN_ENDTIME, p_endTime);
		values.put(MySQLiteHelper.COLUMN_BREAKTIME, p_breakTime);
		values.put(MySQLiteHelper.COLUMN_EDITABLE, p_editable);
		values.put(MySQLiteHelper.COLUMN_DATE, p_date);
        
	    System.out.println("Timelog updated with id: " + id);
	       
		return database.update(MySQLiteHelper.TABLE_TIMELOGS, values, MySQLiteHelper.COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
	}

	//locks all rows in the timelogtable for further changes
	public int lockAllTimelogs(){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_EDITABLE, false);
		return database.update(MySQLiteHelper.TABLE_TIMELOGS, values, null, null);
	}
	
	//locks all rows in the timelogtable for further changes
	public int lockSpecificTimelog(Timelog timelog){
		
		long id = timelog.getId();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_EDITABLE, false);
		
		return database.update(MySQLiteHelper.TABLE_TIMELOGS, values, MySQLiteHelper.COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
	}
	
	//Convert to timelog-class
	private Timelog cursorToTimelog(Cursor cursor) {
	    Timelog timelog = new Timelog();
	    
	    boolean editable = true;
	    int test = cursor.getInt(6);
	    if(test == 0) editable = false;
	        
	    timelog.setId(cursor.getLong(0));
	    timelog.setName(cursor.getString(1));
	    timelog.setComment(cursor.getString(2));
	    timelog.setStartTime(cursor.getString(3));
	    timelog.setEndTime(cursor.getString(4));
	    timelog.setBreakTime(cursor.getInt(5));
	    timelog.setEditable(editable);
	    timelog.setDate(cursor.getString(7));
	    timelog.setColor(cursor.getString(8));
		
		return timelog;
	}
}

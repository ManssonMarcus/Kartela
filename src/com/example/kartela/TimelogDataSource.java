package com.example.kartela;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

package com.example.kartela;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_TIMELOGS = "timelogs";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_PROJECTNAME = "projectname";
  public static final String COLUMN_COMMENT = "comment";
  public static final String COLUMN_STARTTIME = "starttime";
  public static final String COLUMN_ENDTIME = "endtime";
  public static final String COLUMN_BREAKTIME = "breaktime";
  public static final String COLUMN_DATE = "date";

  private static final String DATABASE_NAME = "timelogs.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_TIMELOGS + "(" + 
	  	COLUMN_ID				+ " integer primary key autoincrement," + 
	  	COLUMN_PROJECTNAME 		+ " text," + 
	  	COLUMN_COMMENT			+ " text," +
	  	COLUMN_STARTTIME		+ " time," + 
	  	COLUMN_ENDTIME			+ " time," +
	  	COLUMN_BREAKTIME		+ " time," +
	  	COLUMN_DATE				+ " date);";


      

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMELOGS);
    onCreate(db);
  }

} 
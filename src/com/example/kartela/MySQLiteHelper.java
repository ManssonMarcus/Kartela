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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//Lägger till kolumn för låsbar timelog
public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_TIMELOGS = "timelogs";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_PROJECTNAME = "projectname";
  public static final String COLUMN_COMMENT = "comment";
  public static final String COLUMN_STARTTIME = "starttime";
  public static final String COLUMN_ENDTIME = "endtime";
  public static final String COLUMN_BREAKTIME = "breaktime";
  public static final String COLUMN_DATE = "date";
  public static final String COLUMN_EDITABLE = "editable";
  public static final String COLUMN_COLOR = "color";
  
  private static final String DATABASE_NAME = "timelogs.db";
  private static final int DATABASE_VERSION = 3;

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
	  	COLUMN_EDITABLE			+ " boolean," +
	  	COLUMN_DATE				+ " date," +
	  	COLUMN_COLOR			+ " text);";


      

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
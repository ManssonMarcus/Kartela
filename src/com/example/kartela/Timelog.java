package com.example.kartela;

import java.util.Random;

import android.graphics.Color;


public class Timelog {
  private long id;
  private String name;
  private String comment;
  private String date;
  private String startTime,endTime;
  private int breakTime;
  private boolean editable;
  private String color;

  
  //ID-number
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  //Comment
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
  
  //Projectname
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  //Date
  public String getDate(){
	  return date;
  }
  
  public void setDate(String date){
	  this.date = date;
  }
  
  //StartTime
  public String getStartTime(){
	  return startTime;
  }
  
  public void setStartTime(String st){
	  this.startTime = st;
  }
  
  //EndTime
  public String getEndTime(){
	  return endTime;
  }
  
  public void setEndTime(String endTime){
	  this.endTime = endTime;
  }
  
  //BreakTime
  public int getBreakTime(){
	  return breakTime;
  }
  
  public void setBreakTime(int minutes){
	  this.breakTime = minutes;
  }
  
  //Editable
  public boolean getEditable(){
	  return editable;
  }
  
  public void setEditable(boolean editable){
	  this.editable = editable;
  }
  
  //Editable
  public String getColor(){
	  return color;
  }
  
  public void setColor(String color){
	  this.color = "svart";
  }

  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
    return id + ", " + name + ", " + comment + ", " + startTime + ", " + endTime +", " + breakTime + ", " + editable + ", " + date + ", "+ color;
  }
  
} 

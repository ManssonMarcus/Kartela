package com.example.kartela;

import android.os.Bundle;
import java.util.List;

import android.app.ListActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class TestDatabaseActivity extends ListActivity {
	
	private TimelogDataSource datasource;
	EditText textField;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);
        
        datasource = new TimelogDataSource(this);
        datasource.open();
        
        textField   = (EditText)findViewById(R.id.projectName);
        List<Timelog> values = datasource.getAllTimelogs();
       
        
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Timelog> adapter = new ArrayAdapter<Timelog>(this,
            android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
      @SuppressWarnings("unchecked")
      ArrayAdapter<Timelog> adapter = (ArrayAdapter<Timelog>) getListAdapter();
      Timelog timelog = null;
      switch (view.getId()) {
      case R.id.add:  	  
        // save the new new timelog to the database    	
        timelog = datasource.createTimelog(textField.getText().toString(), "comment","08:00","17:00",40,"2014-04-01");        
        adapter.add(timelog);
        break;
      case R.id.delete:   	  
        if (getListAdapter().getCount() > 0) {
          timelog = (Timelog) getListAdapter().getItem(0);
          datasource.deleteTimelog(timelog);
          adapter.remove(timelog);
        }
        break;
      case R.id.update:
    	  timelog = (Timelog) getListAdapter().getItem(0);
    	  datasource.updateTimelog(timelog, textField.getText().toString(), "ny kommentar", "10:55", "11:00", 5, "2015-11-02");
    	  adapter.clear();
    	  
    	  List<Timelog> values = datasource.getAllTimelogs();
    	  for(int i=0;i<values.size();i++){
    		  adapter.add(values.get(i));
    	  }  
	    break;
      case R.id.list:
    	  adapter.clear();
    	  List<Timelog> values1 = datasource.getTimelogsByName(textField.getText().toString());
    	  for(int i=0;i<values1.size();i++){
    		  adapter.add(values1.get(i));
    	  }
    	  
    	break;
      }
      adapter.notifyDataSetChanged();
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

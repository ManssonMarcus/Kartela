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

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProjectListAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final List<String> projects;

  public ProjectListAdapter(Context context, List<String> projects) {
    super(context, R.layout.project_row_layout, projects);
    this.context = context;
    this.projects = projects;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    	  
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View rowView = inflater.inflate(R.layout.project_row_layout, parent, false);

    TextView colorBarTextView = (TextView) rowView.findViewById(R.id.projectLabel);
    TextView textView = (TextView) rowView.findViewById(R.id.projectLabelText);
    
    Log.d("test",Integer.toString(projects.size()));
    
    textView.setText(projects.get(position));
 
    if (projects.get(position).equals("Saab")) {
	    colorBarTextView.setBackgroundResource(R.drawable.saab_rect);
    } else if (projects.get(position).equals("Volvo")) {
    	colorBarTextView.setBackgroundResource(R.drawable.volvo_rect);
    } else if (projects.get(position).equals("Helikopter")) {
    	colorBarTextView.setBackgroundResource(R.drawable.helikopter_rect);
    } else if (projects.get(position).equals("Combitech")) {
    	colorBarTextView.setBackgroundResource(R.drawable.combitech_rect);
    }
    
    return rowView;
  }  
} 
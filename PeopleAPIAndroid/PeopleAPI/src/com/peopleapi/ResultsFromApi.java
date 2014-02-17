/*
*
* Copyright (C) 2013,2014 Chris Berry twitter: @the_chrisberry
* This file is part of PeopleAPI. 
*
* PeopleAPI is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* PeopleAPI  is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
*/
package com.peopleapi;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class ResultsFromApi extends Activity {

	private TextView _results = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results_main);
		 Intent intent = getIntent();
		_results = (TextView) findViewById(R.id.txtResult);
		_results.setText(intent.getStringExtra("APIResult"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results_main, menu);
		return true;
	}

}

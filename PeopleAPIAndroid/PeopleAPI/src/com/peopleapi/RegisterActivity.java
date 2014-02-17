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

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	private EditText username = null;
	private EditText password = null;
	private EditText confirmPassword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_main);
		username = (EditText) findViewById(R.id.editUserName);
		password = (EditText) findViewById(R.id.editPassword);
		confirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_main, menu);
		return true;
	}

	public void registerUser(View view) {
		// register a user by passing parameters to the A-sync task
		String[] apiParms = { username.getText().toString(),
				password.getText().toString(),
				confirmPassword.getText().toString() };
		Object id = null;
		RegisterWithApi apiCall = new RegisterWithApi(id, RegisterActivity.this);
		apiCall.execute(apiParms);

	}

	public void setTheUIResponse(String result) {

		if (result.contains("201")) {
			String[] apiParms = { username.getText().toString(),
					password.getText().toString() };
			Object id = null;
			LogInToApi apiCall = new LogInToApi(id, RegisterActivity.this);
			apiCall.execute(apiParms);
		}
	}

	public void setTheMoveToResult(String result) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(RegisterActivity.this,	ResultsFromApi.class);
		intent.putExtra("APIResult", result);
		startActivity(intent);
	}

}

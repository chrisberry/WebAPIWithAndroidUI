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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LogInToApi extends AsyncTask<String, Integer, String> {
	private ProgressDialog pDialog;
	private Context context;
	private String curContext;

	public LogInToApi(Object id, LoginMainActivity mainActivity) {
		// set the context of the activity
		context = mainActivity;
		curContext = "mainActivity";
	}

	public LogInToApi(Object id, RegisterActivity registerActivity) {

		context = registerActivity;
		curContext = "registerActivity";
	}

	@Override
	protected void onPreExecute() {
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Logging In...");
		pDialog.setIndeterminate(true);
		pDialog.setCancelable(false);
		pDialog.show();
	};

	@Override
	protected String doInBackground(String... params) {
		String fromApiString = "";
		DefaultHttpClient client = (DefaultHttpClient) getNewHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout Limit
		HttpResponse response;
		try {
			HttpPost post = new HttpPost(context.getResources().getString(R.string.api_loginurl));
			StringEntity se = new StringEntity("grant_type=password&username="
					+ params[0] + "&password=" + params[1] + "");
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/x-www-form-urlencoded"));
			post.setEntity(se);
			response = client.execute(post);
			/* Checking response */
			if (response != null) {
				InputStream inputStream = response.getEntity().getContent();
				// Get the entity data here
				if (inputStream != null) {
					ByteArrayOutputStream content = new ByteArrayOutputStream();
					int readBytes = 0;
					byte[] sBuffer = new byte[512];
					while ((readBytes = inputStream.read(sBuffer)) != -1) {
						content.write(sBuffer, 0, readBytes);
					}
					fromApiString = new String(content.toByteArray());
					inputStream.close();
				}
			}

		} catch (Exception e) {

			fromApiString = e.getMessage().toString();
		}

		return fromApiString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		// this will send the message back to used in the method
		if (curContext == "mainActivity") {
			((LoginMainActivity) context).setTheUIResponse(result);
		} else {
			((RegisterActivity) context).setTheMoveToResult(result);
		}
		pDialog.dismiss();
	};

	private DefaultHttpClient getNewHttpClient() {
		// I mocked out a key store, you will want to generate a real store.
		// this is for testing only!
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
}

/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gcm.demo.app;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pubnub.api.Pubnub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main UI for the demo app.
 */
public class DemoActivity extends FragmentActivity  implements ActionBar.TabListener{
	int responseCounter;
	public static final String SENDER_ID="518909863631";
	public static final String PROPERTY_REG_ID="PROPERTY";
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST=2;
	private String regId;
	private GoogleCloudMessaging gcm;
	static Context context;
	private final Pubnub pubnub = new Pubnub("pub-c-5525471d-13e1-42d8-85f4-c20b1cf78f82" /* replace with your publish key */,
			"sub-c-b1982be6-b6d3-11e5-8f6e-0619f8945a4f" /* replace with your subscribe key */);
	private String android_id;
	private Button refresh;
	public static AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the app, one at a
	 * time.
	 */
	ViewPager mViewPager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	 	android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		context=this;
		CommonUtilities.context=this;
		System.out.println("UNIQUE "+android_id);
		responseCounter=0;
		setContentView(R.layout.main_activity);
		CommonUtilities.ClassMap=new HashMap<String,List<String>>();
		CommonUtilities.ClassMap.put("A", new ArrayList<String>());
		CommonUtilities.ClassMap.put("B", new ArrayList<String>());
		CommonUtilities.ClassMap.put("C", new ArrayList<String>());
		CommonUtilities.names=new ArrayList<String>();
		GetName("https://script.google.com/macros/s/AKfycbxibXIfH_9Y83iRAebIhQsMc0gpqFm8-iYY7K7Ak1PTt-UlZV2u/exec?class=C");
		GetName("https://script.google.com/macros/s/AKfycbxibXIfH_9Y83iRAebIhQsMc0gpqFm8-iYY7K7Ak1PTt-UlZV2u/exec?class=B");
		GetName("https://script.google.com/macros/s/AKfycbxibXIfH_9Y83iRAebIhQsMc0gpqFm8-iYY7K7Ak1PTt-UlZV2u/exec?class=A");
		try{register();}catch (Exception e){e.printStackTrace();}
		refresh=(Button)findViewById(R.id.button_refresh);
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mAppSectionsPagerAdapter.notifyDataSetChanged();
				Toast toast= Toast.makeText(context, "Refreshing", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between different app sections, select the corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if we have a reference to the
				// Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		mViewPager.setOffscreenPageLimit(3);

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by the adapter.
			// Also specify this Activity object, which implements the TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
							.setText(mAppSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
		}
	}


	private void register() {
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			try {
				regId = getRegistrationId(getApplicationContext());
				System.out.println("REGID" +regId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (regId.isEmpty()) {
				registerInBackground();
			} else {
				//toastUser("Registration ID already exists: " + regId);
			}
		} else {
			//Log.e(TAG, "No valid Google Play Services APK found.");
		}
	}

	private boolean checkPlayServices() {
		return true;
		/*int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if (resultCode != 2) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
			//	Log.e(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;*/
	}

	private String getRegistrationId(Context context) throws Exception {
		final SharedPreferences prefs =
				getSharedPreferences(DemoActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			return "";
		}

		return registrationId;
	}

	private void sendRegistrationId(String regId) {
		pubnub.enablePushNotificationsOnChannel(
				"your channel name",
				regId);
	}

	private void storeRegistrationId(Context context, String regId) throws Exception {
		final SharedPreferences prefs =
				getSharedPreferences(DemoActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.apply();
	}


	private void registerInBackground() {
		new AsyncTask() {
			@Override
			protected String doInBackground(Object[] params) {
				String msg;
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
					}
					regId = gcm.register(SENDER_ID);
					String URL="http://lewspage.hostei.com/scripts/insert.php?link="+regId+"&unique="+android_id;
					URL url = new URL(URL);
					HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
					InputStream inputStream = httpURLConnection.getInputStream();
					StringBuilder stringBuilder = new StringBuilder();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					bufferedReader.close();
					inputStream.close();
					httpURLConnection.disconnect();
					System.out.println("REGISTERED " +regId);
					CommonUtilities.registration_id=regId;
					msg = "Device registered, registration ID: " + regId;

					sendRegistrationId(regId);

					storeRegistrationId(getApplicationContext(), regId);
				} catch (Exception ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}
		}.execute(null, null, null);
	}

	private void GetName(String s){
		RequestQueue queue = VolleySingleton.getsInstance().getmRequestQueue();
		final char c=s.charAt(s.length()-1);
		StringRequest stringRequest = new StringRequest(Request.Method.GET, s,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						responseCounter++;
						JSONArray objects = null;
						try {
							objects = new JSONArray(response);
							for (int i = 0; i < objects.length(); i++) {
								JSONObject session = objects.getJSONObject(i);
								if (!session.getString("姓名").equals("姓名")){
									switch (c){
										case 'C':
											CommonUtilities.ClassMap.get("C").add(session.getString("姓名"));
											break;
										case 'B':
											CommonUtilities.ClassMap.get("B").add(session.getString("姓名"));
											break;
										case 'A':
											CommonUtilities.ClassMap.get("A").add(session.getString("姓名"));
											break;
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (responseCounter==3){
							CommonUtilities.names.addAll(CommonUtilities.ClassMap.get("C"));
							CommonUtilities.names.addAll(CommonUtilities.ClassMap.get("B"));
							CommonUtilities.names.addAll(CommonUtilities.ClassMap.get("A"));
							responseCounter=0;
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast toast= Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		queue.add(stringRequest);
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
				case 0:
					return new MainFragment(context);
				case 1:
					return new ViewFragment(context);
				case 2:
					return new SelectFragment(context);
				default:
					return null;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch(position){
				case 0:
					return "UPDATE ";
				case 1:
					return "ATTENDANCE";
				case 2:
					return "NAME LIST";
			}
			return "Section ";
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		System.out.println("COUNT" + tab.getPosition());
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

	}
}
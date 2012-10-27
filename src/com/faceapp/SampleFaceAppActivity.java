package com.faceapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.channing.fighclub.R;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class SampleFaceAppActivity extends Activity {

	public static final String TAG = "SampleFacebookAppActivity";


	TextView nameText;
	Button connectBtn;
	FeedListView feedList;
	FeedListAdapter feedAdapter;

	Button writeBtn;
	EditText writeInput;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        connectBtn = (Button) findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		connect();
        	}
        });


        feedList = (FeedListView) findViewById(R.id.feedList);
        feedAdapter = new FeedListAdapter(this);
        feedList.setAdapter(feedAdapter);
        feedList.setOnDataSelectionListener(new OnDataSelectionListener() {
			public void onDataSelected(AdapterView parent, View v, int position, long id) {
				FeedItem curItem = (FeedItem) feedAdapter.getItem(position);
				String curName = curItem.getName();

				Toast.makeText(getApplicationContext(), "Selected : " + curName, 1000).show();
			}
		});

    }




    private void connect() {
    	try {
    		Facebook mFacebook = new Facebook(BasicInfo.FACEBOOK_APP_ID);
    		BasicInfo.FacebookInstance = mFacebook;

    		if (!BasicInfo.RetryLogin && BasicInfo.FacebookLogin == true) {
    			mFacebook.setAccessToken(BasicInfo.FACEBOOK_ACCESS_TOKEN);
    			Log.v(TAG,"gonna show timeline");
    			showUserTimeline();
    		} else {
    			Log.v(TAG, "trying to authorize again");
    			mFacebook.authorize(this, BasicInfo.FACEBOOK_PERMISSIONS, 
    					new AuthorizationListener());
    		}
    	} catch (Exception ex) {
			ex.printStackTrace();
		}
    }



	private void showUserTimeline() {
		Log.d(TAG, "showUserTimeline() called.");

		connectBtn.setVisibility(View.GONE);

		getUserTimeline();
		/*
		InputMethodManager imm = 
				(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(writeInput.getWindowToken(), 0);
		*/
	}


	private void getUserTimeline() {
		try {
			Bundle bundle = new Bundle();
			bundle.putString("fields", "birthday, name, picture");
			String responseStr = BasicInfo.FacebookInstance.request("me/friends", bundle);
			//Log.v(TAG, "response string : " + responseStr);

			feedAdapter.clear();

			JSONObject resultObj = Util.parseJson(responseStr);
			JSONArray jArray = resultObj.getJSONArray("data");
			//Log.v(TAG, "SIZE: " + jArray.length());
			
			for(int i = 0; i < jArray.length(); i++) {
				JSONObject obj = new JSONObject("{}");
				try {
					obj = jArray.getJSONObject(i);
				} catch (JSONException e) {
					break;
				}
				
				String name = "";
				String id = "";
				
				try {
					id = obj.getString("id");
					name = obj.getString("name");
				} catch (JSONException e) {}
				
				String birthday = "";
				try {
					birthday = obj.getString("birthday");
				} catch (JSONException e) {}				
				String pictureUrl = null;
				try {
					JSONObject picture = new JSONObject(obj.getString("picture"));
					JSONObject data = new JSONObject(picture.getString("data"));
					pictureUrl = data.getString("url");
				} catch (JSONException e) {}


				FeedItem curItem = new FeedItem(name, id, birthday, pictureUrl);
				feedAdapter.addItem(curItem);
				
				//Log.v(TAG, "#" + i + " " + name + ", " + birthday + ", " + id + ", " + pictureUrl);
			}

			feedAdapter.notifyDataSetChanged();

		} catch(Exception ex) {
			ex.printStackTrace();
		}

	}



	protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
		super.onActivityResult(requestCode, resultCode, resultIntent);

		if (resultCode == RESULT_OK) {
			if (requestCode == 32665)
			{
				BasicInfo.FacebookInstance.authorizeCallback(requestCode, resultCode, resultIntent);
			}
		}
	}

	protected void onPause() {
		super.onPause();

		saveProperties();
	}

	protected void onResume() {
		super.onResume();

		loadProperties();

	}

	private void saveProperties() {
		SharedPreferences pref = getSharedPreferences("FACEBOOK", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		editor.putBoolean("FacebookLogin", BasicInfo.FacebookLogin);
		editor.putString("FACEBOOK_ACCESS_TOKEN", BasicInfo.FACEBOOK_ACCESS_TOKEN);
		editor.putString("FACEBOOK_NAME", BasicInfo.FACEBOOK_NAME);

		editor.commit();
	}

	private void loadProperties() {
		SharedPreferences pref = getSharedPreferences("FACEBOOK", MODE_PRIVATE);

		BasicInfo.FacebookLogin = pref.getBoolean("FacebookLogin", false);
		BasicInfo.FACEBOOK_ACCESS_TOKEN = pref.getString("FACEBOOK_ACCESS_TOKEN", "");
		BasicInfo.FACEBOOK_NAME = pref.getString("FACEBOOK_NAME", "");

	}


	public class AuthorizationListener implements DialogListener {
		
		public static final String TAG = "Facebook AuthorizationListener";

		public void onCancel() {
			Log.v(TAG, "FB Cancled");
		}

		public void onComplete(Bundle values) {
			try {
				Log.v(TAG, "in on complete");
				String resultStr = BasicInfo.FacebookInstance.request("me");
				JSONObject obj = new JSONObject(resultStr);
				BasicInfo.FACEBOOK_NAME = obj.getString("name");

				BasicInfo.FacebookLogin = true;
				BasicInfo.FACEBOOK_ACCESS_TOKEN = BasicInfo.FacebookInstance.getAccessToken();

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Log.d(TAG, "Authorization completed : " + BasicInfo.FACEBOOK_NAME);

			showUserTimeline();

		}


		public void onError(DialogError e) {
			Log.v(TAG, "auth error: " + e);
		}


		public void onFacebookError(FacebookError e) {
			Log.v(TAG, "facebook auth error: " + e);
		}

	}


}
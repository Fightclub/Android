package com.channing.fighclub;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.faceapp.BasicInfo;
import com.faceapp.FeedItem;
import com.faceapp.FeedListAdapter;
import com.faceapp.FeedListView;
import com.faceapp.OnDataSelectionListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class PeopleActivity extends Activity {

	public static final String TAG = "PeopleActivity";
	public String apikey = null;


	TextView nameText;
	ImageView connectBtn;
	FeedListView feedList;
	FeedListAdapter feedAdapter;
	Context context;

	Button writeBtn;
	EditText writeInput;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.people_view);
        setUpClickListensers();
        

        connectBtn = (ImageView) findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		connect();
        	}
        });
        
        //Intent thisIntent = getIntent();
        //String name = thisIntent.getStringExtra(HomeActivity.NAME);
        //String id = thisIntent.getStringExtra(HomeActivity.ID);
        
        /////// For PEOPLE ACTIVITY ONLY
        Button signupButton = (Button) findViewById(R.id.signupBtn);
        signupButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Log.v(TAG, "Signup clicked!");
        		Intent intent = new Intent(context, 
        				RegisterActivity2.class);
        		startActivity(intent);
        	}
        });
        
        Button logoutButton = (Button) findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		logout();
        	}
        });
        
        
        Button loginButton = (Button) findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
                
        		Log.v(TAG, "Login clicked!");
        		Intent intent = new Intent(context, 
        				LoginActivity2.class);
        		startActivity(intent);
        		
        	}
        });
        
        Button debugButton = (Button) findViewById(R.id.debugBtn);
        debugButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
                //SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                //String firstname = prefs.getString(Constants.FIRST_NAME, null);
        		SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                apikey = prefs.getString(Constants.API_KEY_KEY, null);
                Toast.makeText(getApplicationContext(), 
						"You're apikey is: " + apikey, 1000).show();
        	}
        });
        
        Button giftsButton = (Button) findViewById(R.id.giftsBtn);
        giftsButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        		SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
                apikey = prefs.getString(Constants.API_KEY_KEY, null);
                
                if (apikey != null) {
                	Intent intent = new Intent(context, 
            				GiftsActivity.class);
                	
            		startActivity(intent);
                } else {
                	Toast.makeText(getApplicationContext(), 
    						"You are not logged in", 1000).show();
                }
        		
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


    public void logout() {
    	SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE).edit();
    	editor.putString(Constants.API_KEY_KEY, null);
    	editor.putString(Constants.FIRST_NAME, null);
    	editor.putString(Constants.LAST_NAME, null);
    	editor.putString(Constants.EMAIL, null);
    	editor.putString(Constants.FB_EMAIL, null);
    	editor.putString(Constants.BDAY, null);
    	editor.commit();
    	Toast.makeText(getApplicationContext(), 
				getString(R.string.logged_out), 1000).show();
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
	
	
    private void setUpClickListensers() {
    	Button giftButton = (Button) findViewById(R.id.gifts_button);
        Button peopleButton = (Button) findViewById(R.id.people_button);
        ImageView searchButton = (ImageView) findViewById(R.id.search_button);
        ImageView cartButton = (ImageView) findViewById(R.id.cart_button);
        
        
        
        giftButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(context, 
        				HomeActivity.class);
        		startActivity(intent);
        	}
        });
        
        peopleButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		//Toast.makeText(getApplicationContext(),
        		//		getString(R.string.people), 1000).show();
        		
        		Intent intent = new Intent(context, 
        				PeopleActivity.class);
        		startActivity(intent);
        		
        	}
        });
        
        searchButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast.makeText(getApplicationContext(),
        				getString(R.string.search), 1000).show();
        	}
        });
        
        cartButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast.makeText(getApplicationContext(),
        				getString(R.string.cart), 1000).show();
        	}
        });
    }


}
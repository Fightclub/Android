/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.channing.fighclub;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidhive.LoginActivity;

public class RegisterActivity2 extends Activity {
	Button btnRegister;
	Button btnLinkToLogin;
	EditText inputFullName;
	EditText inputEmail;
	EditText inputPassword;
	TextView registerErrorMsg;
	
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	private static String registerURL = "https://fight-club-beta.herokuapp.com/network/a/user/new?";

	
	private static final String TAG = "RegisterActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		// Importing all assets like buttons, text fields
		inputFullName = (EditText) findViewById(R.id.registerName);
		inputEmail = (EditText) findViewById(R.id.registerEmail);
		inputPassword = (EditText) findViewById(R.id.registerPassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);
		
		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				Log.v(TAG, "Register button clicked");
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				
				String newUrl = registerURL 
						+ "first="+ Uri.encode(name) 
						+ "&last=" + Uri.encode(name)
						+ "&email="+ Uri.encode(email)
						+ "&password=" + Uri.encode(password);
				Log.v(TAG, "Encoded URL: " + newUrl);
				String response = HttpUtil.request(newUrl);
				Log.v(TAG, "register response: " + response);
				

				String errorMessage = 
						JsonUtil.handleJsonObject(response, "error");
				// If error message returned from server
				if (errorMessage != JsonUtil.JSON_OBJECT_FAIL) {
					Toast.makeText(getApplicationContext(), 
	        				errorMessage, 1000).show();
				} else {
					//return format
					//{"apikey": "x", "last": "x", "bday": null, "fbemail": null, 
					//"id": 1, "email": "x", "first": "x"}
					try {
						JSONObject jObject = new JSONObject(response);
						String apikey = jObject.getString(Constants.API_KEY_KEY);
						String first = jObject.getString(Constants.FIRST_NAME);
						String last = jObject.getString(Constants.LAST_NAME);
						String email2 = jObject.getString(Constants.EMAIL);
						String fbemail = jObject.getString(Constants.FB_EMAIL);
						String bday = jObject.getString(Constants.BDAY);
						
						
						SharedPreferences.Editor editor = 
								getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE).edit();
				    	editor.putString(Constants.API_KEY_KEY, apikey);
				    	editor.putString(Constants.FIRST_NAME, first);
				    	editor.putString(Constants.LAST_NAME, last);
				    	editor.putString(Constants.EMAIL, email2);
				    	editor.putString(Constants.FB_EMAIL, fbemail);
				    	editor.putString(Constants.BDAY, bday);
				    	editor.commit();
						
				    	
						Toast.makeText(getApplicationContext(), 
		        				first + " registered! " + apikey, 1000).show();
						finish();
					} catch (JSONException ex) {
						Log.v(TAG, "Register Response JSON failed: " + ex);
						Toast.makeText(getApplicationContext(), 
								getString(R.string.register_fail), 1000).show();
					}
					
				}
				
				
//				UserFunctions userFunction = new UserFunctions();
//				Log.v(TAG, "user functiosn created");
//				JSONObject json = userFunction.registerUser(name, email, password);
//				Log.v(TAG, "json returned: " + json);
//				// check for login response
//				try {
//					if (json.getString(KEY_SUCCESS) != null) {
//						registerErrorMsg.setText("");
//						String res = json.getString(KEY_SUCCESS); 
//						if(Integer.parseInt(res) == 1){
//							// user successfully registred
//							// Store user details in SQLite Database
//							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//							JSONObject json_user = json.getJSONObject("user");
//							
//							// Clear all previous data in database
//							userFunction.logoutUser(getApplicationContext());
//							db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));						
//							// Launch Dashboard Screen
//							Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
//							// Close all views before launching Dashboard
//							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							startActivity(dashboard);
//							// Close Registration Screen
//							finish();
//						}else{
//							// Error in registration
//							registerErrorMsg.setText("Error occured in registration");
//						}
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
			}
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity2.class);
				startActivity(i);
				// Close Registration View
				finish();
			}
		});
	}
}

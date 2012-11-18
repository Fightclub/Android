package com.channing.fighclub;

import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GiftCardActivity extends Activity {

	private Context context;
	private static final String TAG = "GiftCardActivity";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.gift_card);
		setUpClickListensers();
		Intent thisIntent = getIntent();
		String id = thisIntent.getStringExtra(Constants.ID);
		
		SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        String apikey = prefs.getString(Constants.API_KEY_KEY, null);
		
		//loadContents(id, apikey);
        loadContents("3", apikey);
		
	}
	
	
	private void loadContents(String id, String apikey) {
		String newUrl = Constants.REDEEM_URL
				+ "&apikey="+ Uri.encode(apikey)
				+ "&gift=" + Uri.encode(id);
		Log.v(TAG, "Encoded URL: " + newUrl);
		String response = HttpUtil.request(newUrl);
		Log.v(TAG, "register response: " + response);
		//returns
		/*
		{"redemptionInfo": {"barcode": "barcode url"}, 
		"product": {"icon": "iconurl", "id": ##, "name": "product name"}, 
		"expires": "2012-11-17T21:36:06.831", 
		"sender": {"last": "last name", "id": 5, "first": "first name"}}
		*/
		String errorMessage = 
				JsonUtil.handleJsonObject(response, "error");
		// If error message returned from server
		if (errorMessage != JsonUtil.JSON_OBJECT_FAIL) {
			Toast.makeText(getApplicationContext(), 
    				errorMessage, 1000).show();
		} else {
			try {
				JSONObject jObject = new JSONObject(response);
				String redemptionInfo = jObject.getString(Constants.REDEMPTION_INFO);
				String barcodeUrl = 
						JsonUtil.handleJsonObject(redemptionInfo, Constants.BARCODE);
				
				String productInfo = jObject.getString(Constants.PRODUCT);
				String productIconUrl = JsonUtil.handleJsonObject(productInfo, Constants.ICON);
				String productName = JsonUtil.handleJsonObject(productInfo, Constants.NAME);
				
				String senderInfo = jObject.getString(Constants.SENDER);
				String first = JsonUtil.handleJsonObject(senderInfo, Constants.FIRST_NAME);
				String last = JsonUtil.handleJsonObject(senderInfo, Constants.LAST_NAME);
				
				TextView senderView = (TextView) findViewById(R.id.suggested);
				senderView.setText(first + " " + last + " Suggested");
				
				TextView productView = (TextView) findViewById(R.id.product_name);
				productView.setText(productName);
				
				ImageView productImage = (ImageView) findViewById(R.id.product_image);
				UrlImageViewHelper.setUrlDrawable(productImage, productIconUrl, 
            			R.drawable.loading);
				
				ImageView barcodeImage = (ImageView) findViewById(R.id.barcode);
				UrlImageViewHelper.setUrlDrawable(barcodeImage, barcodeUrl, 
            			R.drawable.loading);
				
				ImageView brandImage = (ImageView) findViewById(R.id.logo);
				//UrlImageViewHelper.setUrlDrawable(barcodeImage, barcodeUrl, 
            	//		R.drawable.loading);
				
			} catch (JSONException ex) {
				Log.v(TAG, "Login Response JSON failed: " + ex);
				Toast.makeText(getApplicationContext(), 
						getString(R.string.register_fail), 1000).show();
			}
			
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void setUpClickListensers() {
		Button giftButton = (Button) findViewById(R.id.gifts_button);
		Button peopleButton = (Button) findViewById(R.id.people_button);
		ImageView searchButton = (ImageView) findViewById(R.id.search_button);
		ImageView cartButton = (ImageView) findViewById(R.id.cart_button);

		giftButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.gifts), 1000).show();
			}
		});

		peopleButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.people), 1000).show();
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

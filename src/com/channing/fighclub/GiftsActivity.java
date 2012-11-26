package com.channing.fighclub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GiftsActivity extends Activity {

	private Context context;
	private static final String TAG = "GiftsActivity";
	private String apikey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		setContentView(R.layout.gifts_view);
		setUpClickListensers();
		Intent thisIntent = getIntent();
		TextView title = (TextView) findViewById(R.id.gifts_title);
		title.setText("Your Gifts");
		loadContents();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void loadContents() {
		SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF,
				MODE_PRIVATE);
		apikey = prefs.getString(Constants.API_KEY_KEY, null);

		String newUrl = Constants.GIFTS_URL + "&apikey=" + Uri.encode(apikey);

		String response = HttpUtil.request(newUrl);
		String errorMessage = JsonUtil.handleJsonObject(response, "error");
		// If error message returned from server
		if (errorMessage != JsonUtil.JSON_OBJECT_FAIL) {
			Toast.makeText(getApplicationContext(), errorMessage, 1000).show();
		} else {
			try {
				// will get json
				/*
				{"received": 
					[{"product": 
						{"vendor": 
							{"icon": "http://www.starbucks.com/static/images/global/logo.png", 
							"id": 1, 
							"name": "Starbucks"}, 
						"name": "Tazo\u00ae Awake\u2122 Brewed Tea", 
						"icon": "http://www.starbucks.com/assets/44b40397d47c4c7f9621947c6768b952.jpg"}, 
					"id": 3}], 
				"sent": 
					[{"product": 
						{"vendor": 
							{"icon": "http://www.starbucks.com/static/images/global/logo.png", 
							"id": 1, 
							"name": "Starbucks"}, 
						"name": "Tazo\u00ae Awake\u2122 Brewed Tea", 
						"icon": "http://www.starbucks.com/assets/44b40397d47c4c7f9621947c6768b952.jpg"}, 
					"id": 4}]}
				*/
				JSONObject jObject = new JSONObject(response);
				JSONArray received = new JSONArray(
						jObject.getString(Constants.RECEIVED));
				for (int i = 0; i < received.length(); i++) {
					String item = received.getString(i);
					Log.v(TAG, "item: " + item);
					final String id = JsonUtil.handleJsonObject(item, Constants.ID);
					String product = JsonUtil.handleJsonObject(item, Constants.PRODUCT);
					String name = JsonUtil.handleJsonObject(product,
							Constants.NAME);
					String url = JsonUtil
							.handleJsonObject(product, Constants.ICON);
					String vendorInfo = JsonUtil.handleJsonObject(product,
							Constants.VENDOR);
					String vendorName = JsonUtil.handleJsonObject(vendorInfo,
							Constants.NAME);

					LinearLayout contents = (LinearLayout) findViewById(R.id.gifts_content);
					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					inflater.inflate(R.layout.gifts_entry, contents, true);

					RelativeLayout entry = (RelativeLayout) contents
							.findViewById(R.id.gifts_entry);
					
					entry.setId(Util.getUniqueId(this));
					
					ImageView productImage = (ImageView) entry
							.findViewById(R.id.gift_product_image);
					UrlImageViewHelper.setUrlDrawable(productImage, url,
							R.drawable.loading);

					TextView itemTitle = (TextView) entry
							.findViewById(R.id.gift_name);
					itemTitle.setText(name);
					
					// TODO since we're not getting sender info for now
					// I'm using vendor name instead.
					TextView vendorTitle = (TextView) entry
							.findViewById(R.id.sender_name);
					vendorTitle.setText(vendorName);
					
					entry.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {

							Intent intent = new Intent(context,
									GiftCardActivity.class);
							intent.putExtra(Constants.ID, id);
							startActivity(intent);
						}
					});
				}

			} catch (JSONException e) {
				Log.v(TAG, "Json parsing error: " + e);
			}
		}

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

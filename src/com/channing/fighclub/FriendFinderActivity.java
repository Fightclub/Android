package com.channing.fighclub;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;

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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.faceapp.SampleFaceAppActivity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

public class FriendFinderActivity extends Activity {

	private Context context;
	private static final String TAG = "FriendFinderActivity";
	private EditText emailOfFriend;
	private EditText giftMessage;
	private PayPal pp;
	private String price;
	private String id;
	private Button sendBtn;
	private CheckoutButton launchSimplePayment;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		setContentView(R.layout.friend_find);
		
		emailOfFriend = (EditText) findViewById(R.id.email_of_friend);
		giftMessage = (EditText) findViewById(R.id.gift_message);
		Intent thisIntent = getIntent();
		id = thisIntent.getStringExtra(Constants.ID);
		price = thisIntent.getStringExtra(Constants.PRICE);
		setUpPayPal();

		setUpClickListensers();

	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.PAYPAL_REQUEST_CODE:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Toast.makeText(getApplicationContext(), 
						"Please Wait", 1000).show();
				sendGift();
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(getApplicationContext(), 
						"Payment Cancelled", 1000).show();
				break;
			}
			break;
			
		case Constants.FACEBOOK_FRIEND_REQUEST_CODE:
			switch (resultCode) {
			case Activity.RESULT_OK:
				String friendName = data.getStringExtra(Constants.NAME);
				String friendEmail = data.getStringExtra(Constants.EMAIL);
				emailOfFriend.setText(friendEmail);
				Toast.makeText(getApplicationContext(), 
						"You Selected: " + friendName, 1000).show();
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(getApplicationContext(), 
						"Friend Selection Cancelled", 1000).show();
				break;
			}
			break;

		}

	}
	
	
	private void sendGift() {
		SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        String apikey = prefs.getString(Constants.API_KEY_KEY, null);
        if (apikey == null) {
        	Toast.makeText(getApplicationContext(), 
					"You have to be logged in", 1000).show();
        	return;
        }
		
		String newUrl = Constants.GIFT_URL
				+ "apikey="+ Uri.encode(apikey)
				+ "&product=" + Uri.encode(id)
				+ "&email=" + Uri.encode(emailOfFriend.getText().toString())
				+ "&message=" + Uri.encode(giftMessage.getText().toString());
		String response = HttpUtil.request(newUrl);
		//Log.v(TAG, "new url: " + newUrl);
		Log.v(TAG, "register response: " + response);
		
		String errorMessage = 
				JsonUtil.handleJsonObject(response, "error");
		// If error message returned from server
		if (errorMessage != JsonUtil.JSON_OBJECT_FAIL) {
			Toast.makeText(getApplicationContext(), 
    				errorMessage, 1000).show();
		} else {
			//returns
			/*
			{"product": 
				{"vendor": 
					{"icon": "vendro icon url", 
					"id": 1, 
					"name": "vendor name"}, 
				"name": "product name", 
				"icon": "product icon url"}, 
			"sender": {"last": "asdf", "id": 22, "first": "asdf"}, 
			"redeemed": null, 
			"created": "2012-11-18T08:30:54.371Z", 
			"receiver": {"last": "last name", "id": 5, "first": "first name"}, 
			"id": 6}
			*/

			String reInfo = 
					JsonUtil.handleJsonObject(response, Constants.RECEIVER);
			String first = 
					JsonUtil.handleJsonObject(reInfo, Constants.FIRST_NAME);
			Toast.makeText(getApplicationContext(), 
    				"Gift sent to " + first, Toast.LENGTH_LONG).show();
			finish();
		}
	}

	public void onResume() {
		super.onResume();

		pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
				PayPal.ENV_SANDBOX);
		pp.setShippingEnabled(false);
//		launchSimplePayment = pp.getCheckoutButton(this, PayPal.BUTTON_194x37,
//				CheckoutButton.TEXT_PAY);

	}
	
	
	private void setUpPayPal() {
		pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
				PayPal.ENV_LIVE);
		pp.setShippingEnabled(false);

//		LinearLayout layoutSimplePayment = new LinearLayout(this);
//		layoutSimplePayment.setLayoutParams(new LayoutParams(
//				LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT));
//		layoutSimplePayment.setOrientation(LinearLayout.VERTICAL);
//		launchSimplePayment = pp.getCheckoutButton(this, PayPal.BUTTON_194x37,
//				CheckoutButton.TEXT_PAY);
		sendBtn = (Button) findViewById(R.id.btnSend);

		sendBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
		        String apikey = prefs.getString(Constants.API_KEY_KEY, null);
		        if (apikey == null) {
		        	Toast.makeText(getApplicationContext(), 
							"You have to be logged in", 1000).show();
		        	return;
		        }
		        if (emailOfFriend.getText().toString().length() <=0) {
		        	Toast.makeText(getApplicationContext(), 
							"Please enter your friend's email", 1000).show();
		        	return;
		        }
		        
//				pp = PayPal.initWithAppID(context, "APP-80W284485P519543T",
//						PayPal.ENV_LIVE);
//				pp.setShippingEnabled(false);

				PayPalPayment payment = new PayPalPayment();
				payment.setSubtotal(new BigDecimal(price));
				payment.setCurrencyType("USD");
				payment.setRecipient("seller_1353212236_biz@gmail.com");
				payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
				Intent checkoutIntent = PayPal.getInstance().checkout(payment,
						context);

				startActivityForResult(checkoutIntent,
						Constants.PAYPAL_REQUEST_CODE);
			}
		});

//		layoutSimplePayment.addView(launchSimplePayment);
//		LinearLayout content = (LinearLayout) findViewById(R.id.sub_content_view);
//		content.addView(layoutSimplePayment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void setUpClickListensers() {
		ImageView searchButton = (ImageView) findViewById(R.id.search_button);
		ImageView cartButton = (ImageView) findViewById(R.id.cart_button);
		ImageView FriendFind = (ImageView) findViewById(R.id.facebook_friends);

		
		FriendFind.setOnClickListener(new OnClickListener() {
			// todo - get a FB friend select to pop up
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(),
//						"Facebook button!", 1000).show();
				Intent facebookFriendIntent = 
						new Intent(context, FindFacebookFriendActivity.class);
				startActivityForResult(facebookFriendIntent,
						Constants.FACEBOOK_FRIEND_REQUEST_CODE);
			}

		});


		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.search), 1000).show();
//				Intent intent = new Intent(context, BuyItemActivity.class);
//				startActivity(intent);
			}
		});

		cartButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, GiftCardActivity.class);
				startActivity(intent);
			}
		});
	}
}

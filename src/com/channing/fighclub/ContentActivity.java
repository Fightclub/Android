package com.channing.fighclub;

import java.math.BigDecimal;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

public class ContentActivity extends Activity {

	private Context context;
	private static final String TAG = "ContentActivity";
	private PayPal pp;
	private String price;
	private String id;
	private String name;
	private CheckoutButton launchSimplePayment;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		setContentView(R.layout.content_view);
		setUpClickListensers();
		Intent thisIntent = getIntent();
		name = thisIntent.getStringExtra(Constants.NAME);
		id = thisIntent.getStringExtra(Constants.ID);
		TextView titleView = (TextView) findViewById(R.id.content_view_title);
		titleView.setText(name);
		loadContents(id);

		pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
				PayPal.ENV_LIVE);
		pp.setShippingEnabled(false);

		LinearLayout layoutSimplePayment = new LinearLayout(this);

		layoutSimplePayment.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT,

				LayoutParams.WRAP_CONTENT));

		layoutSimplePayment.setOrientation(LinearLayout.VERTICAL);

		launchSimplePayment = pp.getCheckoutButton(this, PayPal.BUTTON_194x37,
				CheckoutButton.TEXT_PAY);

		launchSimplePayment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
		        String apikey = prefs.getString(Constants.API_KEY_KEY, null);
		        if (apikey == null) {
		        	Toast.makeText(getApplicationContext(), 
							"You have to be logged in", 1000).show();
		        	return;
		        }

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

		layoutSimplePayment.addView(launchSimplePayment);
		LinearLayout content = (LinearLayout) findViewById(R.id.sub_content_view);
		content.addView(layoutSimplePayment);
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.PAYPAL_REQUEST_CODE:
			switch (resultCode) {
			case Activity.RESULT_OK:
//				Toast.makeText(getApplicationContext(), 
//						"RESULT OK", 1000).show();
				sendGift();
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(getApplicationContext(), 
						"Payment Cancelled", 1000).show();
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
				+ "&email=" + Uri.encode("qwerty"); //TODO Specify Receiver!
		String response = HttpUtil.request(newUrl);
		Log.v(TAG, "new url: " + newUrl);
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
    				"Gift sent to " + first, 1000).show();
			finish();
		}
	}

	public void onResume() {
		super.onResume();

		pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
				PayPal.ENV_SANDBOX);
		pp.setShippingEnabled(false);
		launchSimplePayment = pp.getCheckoutButton(this, PayPal.BUTTON_194x37,
				CheckoutButton.TEXT_PAY);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void loadContents(String id) {

		String aOutput = HttpUtil.request(Constants.PRODUCT_URL + id);

		price = JsonUtil.handleJsonObject(aOutput, "price");
		// Log.v(TAG, "aOutput = " + aOutput);
		// Log.v(TAG, "productURl/ID = " + productUrl+id);
		// TODO String logo =
		String productImage = JsonUtil.handleJsonObject(aOutput, "icon");
		String description = JsonUtil.handleJsonObject(aOutput, "description");

		TextView priceView = (TextView) findViewById(R.id.content_view_price);
		ImageView productImageView = (ImageView) findViewById(R.id.product_image);
		// TODO ImageView LogoView
		TextView descriptionView = (TextView) findViewById(R.id.content_view_description);

		priceView.setText(price);
		UrlImageViewHelper.setUrlDrawable(productImageView, productImage);
		descriptionView.setText(description);

	}

	void setUpClickListensers() {
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

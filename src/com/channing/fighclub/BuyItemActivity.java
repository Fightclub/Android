package com.channing.fighclub;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;

public class BuyItemActivity extends Activity {

	private Context context;
	private static final String TAG = "BuyItemActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.buy_item);
		setUpClickListensers();
		Intent thisIntent = getIntent();
		String id = thisIntent.getStringExtra(Constants.ID);

		SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF,
				MODE_PRIVATE);
		String apikey = prefs.getString(Constants.API_KEY_KEY, null);


		PayPal pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
				PayPal.ENV_LIVE);

		LinearLayout layoutSimplePayment = new LinearLayout(this);

		layoutSimplePayment.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT,

				LayoutParams.WRAP_CONTENT));

		layoutSimplePayment.setOrientation(LinearLayout.VERTICAL);

		CheckoutButton launchSimplePayment = pp.getCheckoutButton(this,
				PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
		
		launchSimplePayment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				PayPalPayment payment = new PayPalPayment();

				payment.setSubtotal(new BigDecimal("5.00"));

				payment.setCurrencyType("USD");

				payment.setRecipient("nadapeter@gmail.com");

				payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);

				Intent checkoutIntent = PayPal.getInstance().checkout(payment,
						context);

				startActivityForResult(checkoutIntent, 1);
			}
		});

		layoutSimplePayment.addView(launchSimplePayment);
		LinearLayout content = (LinearLayout) findViewById(R.id.sub_content_view);
		content.addView(layoutSimplePayment);
	}
	
	
	public void onActivityResults(int requestCode, int resultCode, Intent data) {

	   switch(resultCode) {

	      case Activity.RESULT_OK:
	    	  Toast.makeText(getApplicationContext(), 
						"RESULT OK", 1000).show();

	          break;

	       case Activity.RESULT_CANCELED:
	    	   Toast.makeText(getApplicationContext(), 
						"RESULT CANCELLED", 1000).show();

	           break;

	       case PayPalActivity.RESULT_FAILURE:

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

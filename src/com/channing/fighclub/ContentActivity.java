package com.channing.fighclub;

import java.math.BigDecimal;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ContentActivity extends Activity {

	private Context context;
	private static final String TAG = "ContentActivity";
	private PayPal pp;
	private String price;
	private CheckoutButton launchSimplePayment;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		setContentView(R.layout.content_view);
		setUpClickListensers();
		Intent thisIntent = getIntent();
		String name = thisIntent.getStringExtra(Constants.NAME);
		String id = thisIntent.getStringExtra(Constants.ID);
		TextView titleView = (TextView) findViewById(R.id.content_view_title);
		titleView.setText(name);
		loadContents(id);

		pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
				PayPal.ENV_LIVE);

		LinearLayout layoutSimplePayment = new LinearLayout(this);

		layoutSimplePayment.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT,

				LayoutParams.WRAP_CONTENT));

		layoutSimplePayment.setOrientation(LinearLayout.VERTICAL);

		launchSimplePayment = pp.getCheckoutButton(this, PayPal.BUTTON_194x37,
				CheckoutButton.TEXT_PAY);

		launchSimplePayment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				PayPalPayment payment = new PayPalPayment();

				payment.setSubtotal(new BigDecimal(price));

				payment.setCurrencyType("USD");

				payment.setRecipient("seller_1353212236_biz@gmail.com");

				payment.setPaymentType(PayPal.PAYMENT_TYPE_SERVICE);

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

	public void onActivityResults(int requestCode, int resultCode, Intent data) {
		Toast.makeText(getApplicationContext(), 
				"request: " + requestCode + " Result: " + resultCode, 1000).show();
		switch (requestCode) {
		case Constants.PAYPAL_REQUEST_CODE:
			switch (resultCode) {
			case Activity.RESULT_OK:
				Toast.makeText(getApplicationContext(), 
						"RESULT OK", 1000).show();
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(getApplicationContext(), 
						"RESULT CANCELLED", 1000).show();
				break;
			}
			break;

		}

	}

	public void onResume() {
		super.onResume();

		pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
				PayPal.ENV_SANDBOX);

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

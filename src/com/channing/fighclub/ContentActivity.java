package com.channing.fighclub;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

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
import android.widget.TextView;
import android.widget.Toast;

public class ContentActivity extends Activity {

	private Context context;
	private static final String TAG = "ContentActivity";

	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		setContentView(R.layout.content_view);
		setUpClickListensers();
		Intent thisIntent = getIntent();
		String name = thisIntent.getStringExtra(HomeActivity.NAME);
		String id = thisIntent.getStringExtra(HomeActivity.ID);
		TextView titleView = (TextView) findViewById(R.id.content_view_title);	  
		titleView.setText(name);
		loadContents(id);
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
public void loadContents(String id) {
    	
    	String aOutput = HttpUtil.request(Constants.PRODUCT_URL + id);    	
    	
    	String price = JsonUtil.handleJsonObject (aOutput,"price");
    	//Log.v(TAG, "aOutput = " + aOutput);
    	//Log.v(TAG, "productURl/ID = " + productUrl+id);
    	// TODO String logo = 
    	String productImage = JsonUtil.handleJsonObject (aOutput, "icon");
    	String description = JsonUtil.handleJsonObject(aOutput, "description");
    	
    	TextView priceView = (TextView)  findViewById (R.id.content_view_price);
    	ImageView productImageView = (ImageView) findViewById (R.id.product_image);
    	//TODO ImageView LogoView
    	TextView descriptionView = (TextView)  findViewById (R.id.content_view_description);
    	
    	
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

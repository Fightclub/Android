package com.channing.fighclub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ContentActivity extends Activity {

	private Context context;
	private static final String TAG = "ContentActivity";
	private String price;
	private String id;
	private String productName;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		setContentView(R.layout.content_view);
		setUpClickListensers();
		Intent thisIntent = getIntent();
		productName = thisIntent.getStringExtra(Constants.NAME);
		id = thisIntent.getStringExtra(Constants.ID);
		TextView titleView = (TextView) findViewById(R.id.content_view_title);
		titleView.setText(productName);
		loadContents(id);

	}
	

	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void loadContents(String id) {

		String aOutput = HttpUtil.request(Constants.PRODUCT_URL + id);

		price = JsonUtil.handleJsonObject(aOutput, Constants.PRICE);
		// Log.v(TAG, "aOutput = " + aOutput);
		// Log.v(TAG, "productURl/ID = " + productUrl+id);
		// TODO String logo =
		String productImage = 
				JsonUtil.handleJsonObject(aOutput, Constants.ICON);
		String description = 
				JsonUtil.handleJsonObject(aOutput, Constants.DESCRIPTION);

		TextView priceView = (TextView) findViewById(R.id.content_view_price);
		ImageView productImageView = (ImageView) findViewById(R.id.product_image);
		// TODO ImageView LogoView
		TextView descriptionView = (TextView) findViewById(R.id.content_view_description);

		priceView.setText("$"+price);
		UrlImageViewHelper.setUrlDrawable(productImageView, productImage);
		descriptionView.setText(description);

	}

	void setUpClickListensers() {
        Button friendFind =  (Button) findViewById(R.id.Send_to_Friend);
		Button giftButton = (Button) findViewById(R.id.gifts_button);
		Button peopleButton = (Button) findViewById(R.id.people_button);
		ImageView searchButton = (ImageView) findViewById(R.id.search_button);
		ImageView cartButton = (ImageView) findViewById(R.id.givair_button);
        
        friendFind.setOnClickListener (new OnClickListener(){
			public void onClick(View v) {
        		Intent intent = new Intent(context,
                                           FriendFinderActivity.class);
        		intent.putExtra(Constants.PRODUCT_NAME, productName);
        		intent.putExtra(Constants.ID, id);
        		intent.putExtra(Constants.PRICE, price);
        		
        		startActivity(intent);
        	}
        });

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

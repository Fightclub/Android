package com.channing.fighclub;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class CategoriesActivity extends Activity {
	
	private Context context;
	private static final String TAG = "CategoriesActivity";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getApplicationContext();
        setContentView(R.layout.categories_view);
        setUpClickListensers();
        Intent thisIntent = getIntent();
        String name = thisIntent.getStringExtra(Constants.NAME);
        String id = thisIntent.getStringExtra(Constants.ID);
        TextView title = (TextView) findViewById(R.id.categories_title);
        title.setText(name);
        loadContents(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    public void loadContents(String id) {
    	
    	String aOutput = HttpUtil.request(Constants.CATEGORIES_URL + id);
        String productsString = 
        		JsonUtil.handleJsonObject(aOutput, "products");
        
    	LinearLayout productsContent = 
    			(LinearLayout) findViewById(R.id.categories_content);
    	

    	try {
        	JSONArray categoryProducts = new JSONArray(productsString);
        	int random = (int) Math.random() * Integer.MAX_VALUE 
        			- categoryProducts.length();;
        	for (int i=0; i < categoryProducts.length(); i++) {
        		String url = JsonUtil.handleJsonObject
        				(categoryProducts.getString(i), "icon");
        		final String name = JsonUtil.handleJsonObject
        				(categoryProducts.getString(i), "name");
        		final String itemId = JsonUtil.handleJsonObject
        				(categoryProducts.getString(i), "id");
        		String vendor = JsonUtil.handleJsonObject
        				(categoryProducts.getString(i), "vendor");
        		String vendorName = JsonUtil.handleJsonObject
        				(vendor, "name");
        		
        		LinearLayout contents = (LinearLayout) findViewById(R.id.categories_content);
        		LayoutInflater inflater = (LayoutInflater)
        				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		inflater.inflate(R.layout.categories_entry, contents, true);
        		
        		RelativeLayout entry = (RelativeLayout) contents.findViewById(R.id.categories_entry);
        		
        		entry.setId(random + i);
        		
        		ImageView productImage = (ImageView) entry.findViewById(R.id.category_product_image);
        		UrlImageViewHelper.setUrlDrawable(productImage, url, 
            			R.drawable.loading);
        		
        		TextView itemTitle = (TextView) entry.findViewById(R.id.content_name);
        		itemTitle.setText(name);
        		
        		entry.setOnClickListener(new OnClickListener() {
                 	public void onClick(View v) {
                 		Intent intent = new Intent(context,
                                                   ContentActivity.class);
                 		intent.putExtra(Constants.NAME, name);
                 		intent.putExtra(Constants.ID, itemId);
                 		startActivity(intent);
                 	}
        		});
        		
        		/*
        		TextView itemVendor = new TextView(this);
        		itemVendor.setText(vendorName);
        		productsContent.addView(itemVendor);
        		
        		TextView itemTitle = new TextView(this);
        		itemTitle.setText(name);
        		productsContent.addView(itemTitle);
        		
        		ImageView iv = new ImageView(this);
            	iv.setImageResource(R.drawable.loading);
            	int minDimen = Util.dpToPx(
            			getString(R.dimen.horz_scroll_height),
            			context);
            	iv.setMinimumWidth(minDimen);
            	iv.setMinimumHeight(minDimen);
            	
            	LinearLayout.LayoutParams lp = 
            			new LinearLayout.LayoutParams(
            					minDimen, 
            					LinearLayout.LayoutParams.MATCH_PARENT);//w,h
            	int m = Util.dpToPx(5, context);
            	lp.setMargins(m, m, m, m);//left,top,right,bottom
            	iv.setLayoutParams(lp);
            	//iv.setBackgroundResource(R.layout.background);

            	productsContent.addView(iv);
            	UrlImageViewHelper.setUrlDrawable(iv, url, 
            			R.drawable.loading);
            	*/
        	}
            
        } catch (JSONException e) {
        	Log.v(TAG, "Json parsing error: " + e);
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

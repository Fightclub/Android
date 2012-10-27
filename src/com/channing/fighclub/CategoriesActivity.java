package com.channing.fighclub;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class CategoriesActivity extends Activity {
	
	private Context context;
	private static final String TAG = "CategoriesActivity";
	
	public static final String categoryUrl = HomeActivity.defaultUrl
			+ "product/category?id=";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getApplicationContext();
        setContentView(R.layout.categories_view);
        setUpClickListensers();
        Intent thisIntent = getIntent();
        String name = thisIntent.getStringExtra(HomeActivity.NAME);
        String id = thisIntent.getStringExtra(HomeActivity.ID);
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
    	
    	String aOutput = HttpUtil.request(categoryUrl+id);
        String productsString = 
        		JsonUtil.handleJsonObject(aOutput, "products");
        
    	LinearLayout productsContent = 
    			(LinearLayout) findViewById(R.id.categories_content);
    	
    	try {
        	JSONArray categoryProducts = new JSONArray(productsString);
        	for (int i=0; i < categoryProducts.length(); i++) {
        		String url = JsonUtil.handleJsonObject
        				(categoryProducts.getString(i), "icon");
        		String name = JsonUtil.handleJsonObject
        				(categoryProducts.getString(i), "name");
        		String itemId = JsonUtil.handleJsonObject
        				(categoryProducts.getString(i), "id");
        		String vendor = JsonUtil.handleJsonObject
        				(categoryProducts.getString(i), "vendor");
        		String vendorName = JsonUtil.handleJsonObject
        				(vendor, "name");
        		
        		TextView itemVendor = new TextView(this);
        		itemVendor.setText(vendorName);
        		productsContent.addView(itemVendor);
        		
        		TextView itemTitle = new TextView(this);
        		itemTitle.setText(name);
        		productsContent.addView(itemTitle);
        		
        		ImageView iv = new ImageView(this);
            	iv.setImageResource(R.drawable.loading_small);
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
            			R.drawable.loading_small);
            	
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

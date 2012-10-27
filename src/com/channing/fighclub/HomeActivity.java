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

public class HomeActivity extends Activity {
	
	private Context context;
	private static final String TAG = "HomeActivity";
	
	public static final String defaultUrl = 
			"http://fight-club-beta.herokuapp.com/catalog/a/";
	public static final String featuredUrl = 
		"http://fight-club-beta.herokuapp.com/catalog/a/product/category?id=2";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        setUpClickListensers();
        loadContents();
        
        
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
    
    
    private void setImageClickListener(ImageView iv, String type, String name) {
    	final String title = name;
    	
    	if (type == getString(R.string.brands)) {
    		iv.setOnClickListener(new OnClickListener() {
            	public void onClick(View v) {
            		Intent intent = new Intent(context, 
            				BrandsActivity.class);
            		intent.putExtra(BrandsActivity.NAME, title);
            		startActivity(intent);
            	}
            });
    	} else if (type == getString(R.string.categories)) {
    		iv.setOnClickListener(new OnClickListener() {
            	public void onClick(View v) {
            		Intent intent = new Intent(context, 
            				BrandsActivity.class);
            		intent.putExtra(CategoriesActivity.NAME, title);
            		startActivity(intent);
            	}
    		});
    	} else {
    		iv.setOnClickListener(new OnClickListener() {
            	public void onClick(View v) {
            		Toast.makeText(getApplicationContext(),
            				title, 1000).show();
            	}
            });
    	}
    	
    }
    
    
    private void loadContents() {
    	
    	//Featured Item
        String featruedOutput = HttpUtil.request(featuredUrl);
        String featuredString = 
        		JsonUtil.handleJsonObject(featruedOutput, "products");

        HorizontalScrollView featuredLayout =
    			(HorizontalScrollView) findViewById(R.id.featured);
    	LinearLayout featuredHorzScroll = (LinearLayout) 
    			featuredLayout.findViewById(R.id.horz_scroll_linear);
		try {
        	JSONArray featuredProducts = new JSONArray(featuredString);
        	for (int i=0; i < featuredProducts.length(); i++) {
        		String url = JsonUtil.handleJsonObject
        				(featuredProducts.getString(i), "icon");
        		ImageView iv = new ImageView(this);
            	iv.setImageResource(R.drawable.loading_small);
            	int minDimen = Util.dpToPx(
            			getString(R.dimen.featured_horz_height),
            			context);
            	iv.setMinimumWidth(minDimen);
            	iv.setMinimumHeight(minDimen);
            	
            	LinearLayout.LayoutParams lp = 
            			new LinearLayout.LayoutParams(
            					LinearLayout.LayoutParams.WRAP_CONTENT, 
            					LinearLayout.LayoutParams.MATCH_PARENT); //w,h
            	int m = Util.dpToPx(5, context);
            	lp.setMargins(m, m, m, m);//left,top,right,bottom
            	iv.setLayoutParams(lp);
            	//iv.setBackgroundResource(R.layout.background);
            	
            	iv.setOnClickListener(new OnClickListener() {
                	public void onClick(View v) {
                		Intent contentIntent = new Intent(context, 
                				ContentActivity.class);
                		startActivity(contentIntent);
                	}
                });
            	
            	featuredHorzScroll.addView(iv);
            	UrlImageViewHelper.setUrlDrawable(iv, url, 
            			R.drawable.loading_small);
            	
        	}
            
        } catch (JSONException e) {
        	Log.v(TAG, "Json parsing error: " + e);
        }
		
		
		//Categories
		String aOutput = HttpUtil.request(defaultUrl);
        String categoryString = 
        		JsonUtil.handleJsonObject(aOutput, "categories");
        
        HorizontalScrollView categoryLayout =
    			(HorizontalScrollView) findViewById(R.id.category);
    	LinearLayout categoryHorzScroll = (LinearLayout) 
    			categoryLayout.findViewById(R.id.horz_scroll_linear);
		try {
        	JSONArray categoryProducts = new JSONArray(categoryString);
        	for (int i=0; i < categoryProducts.length(); i++) {
        		String url = JsonUtil.handleJsonObject
        				(categoryProducts.getString(i), "icon");
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
            	String title = JsonUtil.handleJsonObject(
            			categoryProducts.getString(i), "name");
            	setImageClickListener(iv, getString(R.string.categories), title);
            	categoryHorzScroll.addView(iv);
            	UrlImageViewHelper.setUrlDrawable(iv, url, 
            			R.drawable.loading_small);
            	
        	}
            
        } catch (JSONException e) {
        	Log.v(TAG, "Json parsing error: " + e);
        }
		
		
		// Vendors
		String vendorString = 
        		JsonUtil.handleJsonObject(aOutput, "vendors");
		HorizontalScrollView vendorLayout =
    			(HorizontalScrollView) findViewById(R.id.vendor);
    	LinearLayout vendorHorzScroll = (LinearLayout) 
    			vendorLayout.findViewById(R.id.horz_scroll_linear);
    	
		try {
        	JSONArray vendorProducts = new JSONArray(vendorString);
        	for (int i=0; i < vendorProducts.length(); i++) {
        		String url = JsonUtil.handleJsonObject
        				(vendorProducts.getString(i), "icon");
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
            	String title = JsonUtil.handleJsonObject(
            			vendorProducts.getString(i), "name");
            	setImageClickListener(iv, getString(R.string.brands), title);
            	//iv.setBackgroundResource(R.layout.background);
            	vendorHorzScroll.addView(iv);
            	UrlImageViewHelper.setUrlDrawable(iv, url, 
            			R.drawable.loading_small);
            	
        	}
            
        } catch (JSONException e) {
        	Log.v(TAG, "Json parsing error: " + e);
        }
    }
    

}

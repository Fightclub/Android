package com.channing.fighclub;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class HomeActivity extends Activity {
	
	private Context context;
	private static final String TAG = "HomeActivity";


	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        setUpClickListensers();

        //loadContents();
        LoadFeaturedContentsTask lfc = new LoadFeaturedContentsTask(context);
        lfc.execute();
        LoadCategoryContentsTask lcc = new LoadCategoryContentsTask(context);
        lcc.execute();
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
        		//Toast.makeText(getApplicationContext(),
        		//		getString(R.string.people), 1000).show();
        		
        		Intent intent = new Intent(context, 
        				PeopleActivity.class);
        		startActivity(intent);
        		
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
    
    
    private void setImageClickListener(ImageView iv, 
    		String type, String name, String id) {
    	final String title = name;
    	final String idx = id;
    	
    	if (type == getString(R.string.brands)) {
    		iv.setOnClickListener(new OnClickListener() {
            	public void onClick(View v) {
            		Intent intent = new Intent(context, 
            				BrandsActivity.class);
            		intent.putExtra(Constants.NAME, title);
            		intent.putExtra(Constants.ID, idx);
            		startActivity(intent);
            	}
            });
    	} else if (type == getString(R.string.categories)) {
    		iv.setOnClickListener(new OnClickListener() {
            	public void onClick(View v) {
            		Intent intent = new Intent(context, 
            				CategoriesActivity.class);
            		intent.putExtra(Constants.NAME, title);
            		intent.putExtra(Constants.ID, idx);
            		startActivity(intent);
            	}
    		});
    	} else if (type == getString(R.string.featured_products)) {
     		iv.setOnClickListener(new OnClickListener() {
             	public void onClick(View v) {
             		Intent intent = new Intent(context,
                                               ContentActivity.class);
             		intent.putExtra(Constants.NAME, title);
             		intent.putExtra(Constants.ID, idx);
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
    
    
    
    private class LoadFeaturedContentsTask extends AsyncTask<Void, Void, String> {
        Context mContext;
    	DisplayImageOptions options;
    	protected ImageLoader imageLoader = ImageLoader.getInstance();
    	
    	LoadFeaturedContentsTask(Context context) {
        	super();
        	this.mContext = context;
            options = new DisplayImageOptions.Builder()
    			.showStubImage(R.drawable.loading)
    			.showImageForEmptyUri(R.drawable.loading)
    			.cacheInMemory()
    			.cacheOnDisc()
    			.build();
        	imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        }
    	
    	protected String doInBackground(Void... V) {
        	//Featured Item
            String featuredOutput = HttpUtil.request(Constants.FEATURED_URL);
            return featuredOutput;
        }

        protected void onPostExecute(String featuredOutput) {
        	String featuredString = 
            		JsonUtil.handleJsonObject(featuredOutput, "products");

            HorizontalScrollView featuredLayout =
        			(HorizontalScrollView) findViewById(R.id.featured);
        	LinearLayout featuredHorzScroll = (LinearLayout) 
        			featuredLayout.findViewById(R.id.horz_scroll_linear);
    		try {
            	JSONArray featuredProducts = new JSONArray(featuredString);
            	for (int i=0; i < featuredProducts.length(); i++) {
            		String url = JsonUtil.handleJsonObject
            				(featuredProducts.getString(i), "banner");
                    String title = 
                    		JsonUtil.handleJsonObject(
                    				featuredProducts.getString(i), "name");
                	String id = 
                			JsonUtil.handleJsonObject(
                					featuredProducts.getString(i), "id");
            		ImageView iv = new ImageView(mContext);
                	iv.setImageResource(R.drawable.loading);
                	int minDimen = Util.dpToPx(
                			getString(R.dimen.featured_horz_height),
                			context);
                	iv.setMinimumWidth(minDimen);
                	iv.setMinimumHeight(minDimen);
                	
                	DisplayMetrics dm = new DisplayMetrics();
                	getWindowManager().getDefaultDisplay().getMetrics(dm);
                	int width = dm.widthPixels;
                	
                	LinearLayout.LayoutParams lp = 
                			new LinearLayout.LayoutParams(
                					width, 
                					LinearLayout.LayoutParams.MATCH_PARENT); //w,h
                	//int m = Util.dpToPx(5, context);
                	lp.setMargins(0, 0, 0, 0);//left,top,right,bottom
                	iv.setLayoutParams(lp);
                	iv.setScaleType(ImageView.ScaleType.FIT_XY);
                	//iv.setBackgroundResource(R.layout.background);
                	
                	setImageClickListener(iv, getString(R.string.featured_products), title, id);
                	featuredHorzScroll.addView(iv);
                	//UrlImageViewHelper.setUrlDrawable(iv, url, 
                	//		R.drawable.loading);
                	//iv.setImageResource(R.drawable.starbucks);
                	imageLoader.displayImage(url ,iv , options);
                	
            	}
                
            } catch (JSONException e) {
            	Log.v(TAG, "Json parsing error: " + e);
            }
        }
    }
    
    private class LoadCategoryContentsTask extends AsyncTask<Void, Void, String> {
        Context mContext;
    	DisplayImageOptions options;
    	protected ImageLoader imageLoader = ImageLoader.getInstance();
    	
    	LoadCategoryContentsTask(Context context) {
        	super();
        	this.mContext = context;
        	imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
    			.showStubImage(R.drawable.loading)
    			.showImageForEmptyUri(R.drawable.loading)
    			.displayer(new RoundedBitmapDisplayer(20))
    			.cacheInMemory()
    			.cacheOnDisc()
    			.build();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        }
    	
    	protected String doInBackground(Void... V) {
        	//Featured Item
    		String aOutput = HttpUtil.request(Constants.HOME_URL);
            return aOutput;
        }

        protected void onPostExecute(String aOutput) {
        	String categoryString = 
            		JsonUtil.handleJsonObject(aOutput, "categories");
            
            HorizontalScrollView categoryLayout =
        			(HorizontalScrollView) findViewById(R.id.category);
        	LinearLayout categoryHorzScroll = (LinearLayout) 
        			categoryLayout.findViewById(R.id.horz_scroll_linear);
    		try {
            	JSONArray categoryProducts = new JSONArray(categoryString);
            	for (int i=1; i < categoryProducts.length(); i++) {
            		// Starting for i = 1 so we won't show featured item as a category
            		String url = JsonUtil.handleJsonObject
            				(categoryProducts.getString(i), "icon");
            		ImageView iv = new ImageView(context);
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
                	String title = JsonUtil.handleJsonObject(
                			categoryProducts.getString(i), "name");
                	String id = JsonUtil.handleJsonObject(
                			categoryProducts.getString(i), "id");
                	setImageClickListener(iv, 
                			getString(R.string.categories), title, id);
                	categoryHorzScroll.addView(iv);
                	imageLoader.displayImage(url ,iv , options);
                	
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
            		ImageView iv = new ImageView(context);
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
                	String title = JsonUtil.handleJsonObject(
                			vendorProducts.getString(i), "name");
                	String id = JsonUtil.handleJsonObject(
                			vendorProducts.getString(i), "id");
                	setImageClickListener(iv, 
                			getString(R.string.brands), title, id);
                	//iv.setBackgroundResource(R.layout.background);
                	vendorHorzScroll.addView(iv);
                	imageLoader.displayImage(url ,iv , options);
                	
            	}
                
            } catch (JSONException e) {
            	Log.v(TAG, "Json parsing error: " + e);
            }
        }
    }
    

}

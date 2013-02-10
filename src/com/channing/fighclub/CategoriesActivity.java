package com.channing.fighclub;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
        //loadContents(id);
        LoadCategoryTask lct = new LoadCategoryTask(context, id, this);
        lct.execute();
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
        ImageView cartButton = (ImageView) findViewById(R.id.givair_button);
        
        
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
    
    private class LoadCategoryTask extends AsyncTask<Void, Void, String> {
        Context mContext;
    	DisplayImageOptions options;
    	String mId;
    	Activity mActivity;
    	protected ImageLoader imageLoader = ImageLoader.getInstance();
    	
    	LoadCategoryTask(Context context, String id, Activity activity) {
        	super();
        	this.mContext = context;
        	this.mId = id;
        	this.mActivity = activity;
        	imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
    			.displayer(new RoundedBitmapDisplayer(20))
    			.cacheInMemory()
    			.cacheOnDisc()
    			.build();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        }
    	
    	protected String doInBackground(Void... V) {
        	//Featured Item
    		String aOutput = HttpUtil.request(Constants.CATEGORIES_URL + mId);
            return aOutput;
        }

        protected void onPostExecute(String aOutput) {
        	String productsString = 
            		JsonUtil.handleJsonObject(aOutput, "products");
            
        	LinearLayout productsContent = 
        			(LinearLayout) findViewById(R.id.categories_content);
        	

        	try {
            	JSONArray categoryProducts = new JSONArray(productsString);
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
            		
            		entry.setId(Util.getUniqueId(mActivity));
            		
            		TextView itemTitle = (TextView) entry.findViewById(R.id.content_name);
            		itemTitle.setText(name);
            		
        			final ImageView iv = (ImageView) entry.findViewById(R.id.image);
        			final ProgressBar spinner = (ProgressBar) entry.findViewById(R.id.loading);
            		final View shadow = entry.findViewById(R.id.shadow);

                	imageLoader.displayImage(url ,iv , options, new SimpleImageLoadingListener() {
        				@Override
        				public void onLoadingStarted() {
        					spinner.setVisibility(View.VISIBLE);
        				}

        				@Override
        				public void onLoadingComplete(Bitmap loadedImage) {
        					spinner.setVisibility(View.GONE);
        					shadow.setVisibility(View.VISIBLE);
        				}
        			});
            		
            		entry.setOnClickListener(new OnClickListener() {
                     	public void onClick(View v) {
                     		Intent intent = new Intent(context,
                                                       ContentActivity.class);
                     		intent.putExtra(Constants.NAME, name);
                     		intent.putExtra(Constants.ID, itemId);
                     		startActivity(intent);
                     	}
            		});
            	}
                
            } catch (JSONException e) {
            	Log.v(TAG, "Json parsing error: " + e);
            }

        }
    }
}

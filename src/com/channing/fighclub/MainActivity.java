package com.channing.fighclub;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, 
        		R.layout.titlebar);
        
        Button giftButton = (Button) findViewById(R.id.gift_button);
        Button peopleButton = (Button) findViewById(R.id.people_button);
        
        giftButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast.makeText(getApplicationContext(), "Gift!", 1000).show();
        	}
        });
        
        peopleButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Toast.makeText(getApplicationContext(),"People!", 1000).show();
        	}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

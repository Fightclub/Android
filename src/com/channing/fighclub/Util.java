package com.channing.fighclub;

import android.content.Context;
import android.util.TypedValue;

public class Util{
    
	private static final String TAG = "Util";
	
	public static int dpToPx(float x, Context context) {
		return (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 
                 x,
                 context.getResources().getDisplayMetrics());
	}
	
	
	public static int dpToPx(String s, Context context) {
		int i = s.indexOf("d");
		float x;
		if (i == -1) {
			x = Float.parseFloat(s);
		} else {
			x = Float.parseFloat(s.substring(0,i));
		}
		return dpToPx(x, context);
	}
}

package com.channing.fighclub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil{
    
	private static final String TAG = "JsonUtil";
	
    public static String handleJsonObject(String str, String key) {
    	String rtn = "JSON Object Parsing Failed";
    	try {
    		JSONObject jObject = new JSONObject(str);
    		rtn = jObject.getString(key);
    	} catch (JSONException e) {
    		Log.v(TAG, "JSON parsing Failed: " + e);
    	} finally {
    		return rtn;
    	}
    }
    
    
    public static String handleJsonArray(String str, int index) {
    	String rtn = "JSON Array Parsing Failed";
    	try {
    		JSONArray jArray = new JSONArray(str);
    		rtn = jArray.getString(index);
    	} catch (JSONException e) {
    		Log.v(TAG, "JSON parsing Failed: " + e);
    	} finally {
    		return rtn;
    	}
    }

}

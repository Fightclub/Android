package com.channing.fighclub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil{
    
	private static final String TAG = "JsonUtil";
	public static final String JSON_OBJECT_FAIL = "JSON Object Parsing Failed";
	public static final String JSON_ARRAY_FAIL = "JSON Array Parsing Failed";
	
	
    public static String handleJsonObject(String str, String key) {
    	String rtn = JSON_OBJECT_FAIL;
    	try {
    		JSONObject jObject = new JSONObject(str);
    		rtn = jObject.getString(key);
    	} catch (JSONException e) {
    		Log.v(TAG, "JSON parsing Failed key:(" + key + "): " + e);
    	} finally {
    		return rtn;
    	}
    }
    
    
    public static String handleJsonArray(String str, int index) {
    	String rtn = JSON_ARRAY_FAIL;
    	try {
    		JSONArray jArray = new JSONArray(str);
    		rtn = jArray.getString(index);
    	} catch (JSONException e) {
    		Log.v(TAG, "JSON parsing Failed key:(" + index + "): " + e);
    	} finally {
    		return rtn;
    	}
    }

}

package com.channing.fighclub;

public class Constants {
	
	//This class stores constants needed for this app.
	public static final String API_KEY_KEY = "apikey";
	public static final String FIRST_NAME = "first";
	public static final String LAST_NAME = "last";
	public static final String EMAIL = "email";
	public static final String FB_EMAIL = "fbemail";
	public static final String BDAY = "bday";
	
	public static final String SHARED_PREF = "mypref";
	public static final String DEFAULT_HOST = 
			"https://fight-club-beta.herokuapp.com/";
	public static final String DEFAULT_INSECURE_HOST = 
			"http://fight-club-beta.herokuapp.com/";
	public static final String HOME_URL = DEFAULT_INSECURE_HOST + "catalog/a/";
	public static final String FEATURED_URL = HOME_URL + "product/category?id=2";
	public static final String BRANDS_URL = HOME_URL + "vendor?id=";
	public static final String CATEGORIES_URL = HOME_URL + "product/category?id=";
	public static final String PRODUCT_URL = HOME_URL + "product?id=";
	public static final String LOGIN_URL = DEFAULT_HOST + "network/a/user/login?";
	public static final String REGISTER_URL = DEFAULT_HOST + "network/a/user/new?";



}

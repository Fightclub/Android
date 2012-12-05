package com.channing.fighclub;

public class Constants {
	
	//This class stores constants needed for this app.
	public static final String API_KEY_KEY = "apikey";
	public static final String FIRST_NAME = "first";
	public static final String LAST_NAME = "last";
	public static final String EMAIL = "email";
	public static final String FB_EMAIL = "fbemail";
	public static final String BDAY = "bday";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String PRICE = "price";
	public static final String REDEMPTION_INFO = "redemptionInfo";
	public static final String DESCRIPTION = "description";
	public static final String BARCODE = "barcode";
	public static final String PRODUCT = "product";
	public static final String PRODUCT_NAME = "product_name";
	public static final String ICON = "icon";
	public static final String VENDOR = "vendor";
	public static final String SENDER = "sender";
	public static final String RECEIVER = "receiver";
	public static final String RECEIVED = "received";
	public static final String SENT = "sent";
	public static final String MESSAGE = "message";
	public static final String ERROR_CODE = "error_code";
	public static final String ERROR_MSG = "error_msg";
	
	public static final String SHARED_PREF = "mypref";
	public static final String DEFAULT_HOST = "https://fight-club-alpha.herokuapp.com/";
			//"https://fight-club-beta.herokuapp.com/";
	public static final String DEFAULT_INSECURE_HOST = "http://fight-club-alpha.herokuapp.com/";
			//"http://fight-club-beta.herokuapp.com/";
	public static final String HOME_URL = DEFAULT_INSECURE_HOST + "catalog/a/";
	public static final String FEATURED_URL = HOME_URL + "product/category?id=1";
	public static final String BRANDS_URL = HOME_URL + "vendor?id=";
	public static final String CATEGORIES_URL = HOME_URL + "product/category?id=";
	public static final String PRODUCT_URL = HOME_URL + "product?id=";
	public static final String LOGIN_URL = DEFAULT_HOST + "network/a/user/login?";
	public static final String REGISTER_URL = DEFAULT_HOST + "network/a/user/new?";
	public static final String REDEEM_URL = DEFAULT_HOST + "network/a/gift/redeem?";
	public static final String GIFT_URL = DEFAULT_HOST + "network/a/gift/new?";
	public static final String GIFTS_URL = DEFAULT_HOST + "network/a/user/gifts?";

	public static final int PAYPAL_REQUEST_CODE = 1000;
	public static final int FACEBOOK_FRIEND_REQUEST_CODE = 1001;
	
	public static final String PAYPAL_APP = "APP-3R448099WX274962U";
	public static final String PAYPAL_SANDBOX = "APP-80W284485P519543T";
	public static final String PAYPAL_EMAIL = "givairtransactions@gmail.com";
}

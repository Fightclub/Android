package com.faceapp;

import java.text.SimpleDateFormat;

import android.graphics.Bitmap;

import com.facebook.android.Facebook;

public class BasicInfo {

	public static final int REQ_CODE_FACEBOOK_LOGIN = 1001;

	public static boolean FacebookLogin = false;
	public static boolean RetryLogin = false;

	public static Facebook FacebookInstance = null;

	public static String[] FACEBOOK_PERMISSIONS = {"publish_stream", "read_stream", "user_photos", "email", "friends_birthday"};

	public static String FACEBOOK_ACCESS_TOKEN = "";
	public static String FACEBOOK_APP_ID = "299031653542769";
	public static String FACEBOOK_API_KEY = "299031653542769";
	public static String FACEBOOK_APP_SECRET = "e93d7424d790f5f1e14995f477c3da2d";

	public static String FACEBOOK_NAME = "";

	public static SimpleDateFormat OrigDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
	public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

	public static Bitmap BasicPicture = null;
}

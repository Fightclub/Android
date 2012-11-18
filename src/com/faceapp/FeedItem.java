package com.faceapp;

public class FeedItem {

	private String name;
	private String id;
	private String bday;
	private String picture;

	public FeedItem(String inName, String inId, String inBday, String inPicture) {
		name = inName;
		id = inId;
		bday = inBday;
		picture = inPicture;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBday() {
		return bday;
	}
	public void setBday(String bday) {
		this.bday = bday;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}



}

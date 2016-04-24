package com.android.safetycheck.model;

public class Region {

	private String id;
	private String name;
	private Float latitude;
	private Float longitude;

	public Region(String id, String name, Float latitude, Float longitude) {
		super();
		if (id.indexOf("#") == -1){
			this.id = id;
		} else {
			this.id = id.substring(id.indexOf("#"));
		}
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

}

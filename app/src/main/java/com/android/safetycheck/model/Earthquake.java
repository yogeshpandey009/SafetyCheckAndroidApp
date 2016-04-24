package com.android.safetycheck.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Earthquake implements Serializable {

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private String id;
	private Float magnitude;
	private Date time;
	private Float latitude;
	private Float longitude;
	private String timeAsFormat = "";
	
	public Earthquake() {
		super();
	}

	public Earthquake(String id, Float magnitude, String time,
			Float latitude, Float longitude) {
		super();
		this.setId(id);
		this.magnitude = magnitude;
		this.time = parseTime(time);
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Earthquake(Float magnitude, String time,
			Float latitude, Float longitude) {
		super();
		this.magnitude = magnitude;
		this.time = parseTime(time);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private Date parseTime(String time) {
		// 2015-11-12T00:22:32.520Z
		Date date = null;
		try {
			date = formatter.parse(time);
			// System.out.println(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if (id.indexOf("#") == -1){
			this.id = id;
		} else {
			this.id = id.substring(id.indexOf("#")+1);
		}
	}
	public Float getMagnitude() {
		return magnitude;
	}
	public void setMagnitude(Float magnitude) {
		this.magnitude = magnitude;
	}
	public String getTimeAsFormat() {
		return formatter.format(time);
	}
	public Date getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = new Date(Long.parseLong(time));
		//this.time = parseTime(time);
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

	@Override
	public String toString() {
		return "Earthquake{" +
				"id='" + id + '\'' +
				", magnitude=" + magnitude +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", time=" + time +
				'}';
	}

	public static void main(String[] args) {
		new Earthquake().parseTime("2015-11-12T00:22:32.520Z");
	}
	
}

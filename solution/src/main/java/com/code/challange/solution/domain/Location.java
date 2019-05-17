package com.code.challange.solution.domain;

import javax.persistence.*;

@Table(name="LOCATION")
@Entity
public class Location {
	@Id
	@GeneratedValue
	@Column(name="LOCATION_ID",updatable = false)
	private Integer locationId;
	private String locality;
	private double latitude;
	private double longitude;
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}

package com.code.challange.solution.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Table(name="ADDRESS")
@Entity
public class Address implements Serializable{
	
	private static final long serialVersionUID = -4173696750010830421L;
	
	@Id
	@GeneratedValue
	@Column(name="ADDRESS_ID",updatable = false)
	private Integer addressId;
	
	private String name;
	
	private Integer postCode;
	
	@ManyToOne(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STATE_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private State state;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LOCATION_ID", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Location location;

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPostCode() {
		return postCode;
	}

	public void setPostCode(Integer postCode) {
		this.postCode = postCode;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}

}

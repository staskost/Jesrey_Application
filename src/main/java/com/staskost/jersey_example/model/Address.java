package com.staskost.jersey_example.model;

public class Address {

	private int id;

	private String city;

	private String street;

	public Address() {
	}

	public Address(String city, String street) {
		this.city = city;
		this.street = street;
	}

	public Address(int id, String city, String street) {
		this.id = id;
		this.city = city;
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", city=" + city + ", street=" + street + "]";
	}
}

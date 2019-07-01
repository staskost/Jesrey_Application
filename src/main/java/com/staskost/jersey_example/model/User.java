package com.staskost.jersey_example.model;

public class User {

	private int id;

	private String name;

	private String email;

	private Role role;

	private Address address;

	public User() {
	}

	public User(int id, String name, String email, Role role, Address address) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.address = address;
	}

	public User(int id, String name, String email, Role role) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
	}

	public User(String name, String email, Address address) {
		super();
		this.name = name;
		this.email = email;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}

package com.staskost.jersey_example.model;

public final class Role {
	
    public static final Role USER  = new Role(1, "USER");
    public static final Role ADMIN = new Role(2, "ADMIN");
	
	private int id;
	
	private String name;

	public Role() {
		
	}

	public Role(int id, String name) {
		this.id = id;
		this.name = name;
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
	
    public static final Role getRoleFor(int id) throws Exception {
        if (id == USER.getId()) {
            return USER;
        }
        else if (id == ADMIN.getId()) {
            return ADMIN;
        }
        else {
            throw new RuntimeException("Invalid role id: " + id);
        }
    }

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}

}

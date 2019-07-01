package com.staskost.jersey_example.rest;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.staskost.jersey_example.model.Address;
import com.staskost.jersey_example.model.User;

public class RestClient {
	public static void main(String[] args) {
		
		Client client = ClientBuilder.newClient();

		WebTarget baseTarget = client.target("http://localhost:8081/");

		WebTarget usersTarget = baseTarget.path("users");

		WebTarget sιngleUserTarget = usersTarget.path("{userId}");

		// Get all users
		List<User> users = usersTarget
				.request()
				.get(new GenericType<List<User>>() {
		});
		System.out.println(users);

		// Get user
		User user = sιngleUserTarget
				.resolveTemplate("userId", "5")
				.request(MediaType.APPLICATION_JSON)
				.get(User.class);
		System.out.println(user);

		// Save user
		WebTarget usersaveTarget = baseTarget.path("users/save");
		User user2 = new User("Gina3", "gina3@mail.com", new Address("Marousi", "fragoklisias"));
		Response postReponce = usersaveTarget
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(user2));
		System.out.println(postReponce.getStatus());

		// Update user
		User user3 = new User("Gina4", "gina4@mail.com", new Address("Marousi", "fragoklisias"));
		Response putReponce = sιngleUserTarget
				.resolveTemplate("userId", "11")
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.json(user3));
		System.out.println(putReponce.getStatus());

		// Delete user
		Response deleteResponse = sιngleUserTarget
				.resolveTemplate("userId", "9")
				.request().delete();
		System.out.println(deleteResponse);

	}
}

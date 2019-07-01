package com.staskost.jersey_example.controller;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.staskost.jersey_example.database.DatabaseHelper;
import com.staskost.jersey_example.model.Address;
import com.staskost.jersey_example.model.User;



@Path("/users")
public class UserController {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllUsers() {
		List<User> users = DatabaseHelper.fetchAllUsers();
		return Response.ok(users.toArray(new User[users.size()])).build();
	}

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUserById(@PathParam("id") int id) throws Exception {
		User user = DatabaseHelper.getUserById(id);
		if (user != null) {
			return Response.status(200).entity(user).build();
		}
		return Response.status(404).build();
	}


	@GET
	@Path("/inAddress/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUsersInAddress(@PathParam("id") int id) {
		Address address = DatabaseHelper.getAddressById(id);
		if (address != null) {
			List<User> users = DatabaseHelper.getUsersOfAddress(id);
			return Response.ok(users.toArray(new User[users.size()])).build();
		} else {
			return Response.status(404).entity("Address Not Found").build();
		}

	}

	@POST
	@Path("/save")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response saveUser(User user) throws SQLException {
		User user2 = DatabaseHelper.getUserByEmail(user.getEmail());
		if (user2 != null) {
			return Response.status(400).entity("Email already exists").build();
		} else {
			DatabaseHelper.saveUser(user);
			return Response.status(200).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response updateUser(@PathParam("id") int id, User user) {
		User user2 = DatabaseHelper.getUserById(id);
		if (user2 != null) {
			User user3 = DatabaseHelper.getUserByEmail(user.getEmail());
			if (user3 != null) {
				return Response.status(400).entity("Email Already Exists").build();
			}
			DatabaseHelper.updateUser(id, user);
			return Response.status(200).build();
		} else {
			return Response.status(404).build();
		}
	}

	@DELETE
	@Path("address/{id}")
	public Response deleteAddress(@PathParam("id") int id) {

			int count = DatabaseHelper.getUsersCountOfAddress(id);
			if (count == 0) {
				DatabaseHelper.deleteAddress(id);
				return Response.status(200).build();
			} else {
				return Response.status(400).entity("Can not delete address..Address still in use").build();
			}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") int id) {
		User user = DatabaseHelper.getUserById(id);
		if (user != null) {
			DatabaseHelper.deleteUser(id);
			return Response.status(200).build();
		}
		return Response.status(404).build();
	}
}

package com.staskost.jersey_example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.staskost.jersey_example.model.Address;
import com.staskost.jersey_example.model.Role;
import com.staskost.jersey_example.model.User;

public class DatabaseHelper {

	public DatabaseHelper() {
	}

	private static Connection getConnection() {

		Connection conn = null;

		try {
			Properties connectionProps = new Properties();
			connectionProps.put("user", "root");
			connectionProps.put("password", "konnos1987");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/jersey_example?zeroDateTimeBehavior=convertToNull&characterEncoding=utf-8&autoReconnect=true",
					connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

	public static List<User> fetchAllUsers() {

		List<User> users = new ArrayList<User>();

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM user");) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Address a = fetchAddressForUser(rs.getInt(1));
				Role role = Role.getRoleFor(rs.getInt("fk_role_id"));
				users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), role, a));
			}
			return users;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	public static User getUserById(int id) {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE iduser = ?");) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			User user = null;
			if (rs.next()) {
				Address a = fetchAddressForUser(id);
				Role role = Role.getRoleFor(rs.getInt("fk_role_id"));
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), role, a);
			}
			return user;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static User getUserByEmail(String email) {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM user WHERE email = ?");) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			User user = null;
			if (rs.next()) {
				Address a = fetchAddressForUser(rs.getInt(1));
				Role role = Role.getRoleFor(rs.getInt("fk_role_id"));
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), role, a);
			}
			return user;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void saveUser(User user) throws SQLException {

		int addressId = 0;
		Address a = validateAddress(user.getAddress().getCity(), user.getAddress().getStreet());

		if (a != null) {
			addressId = a.getId();
		} else {
			saveAddress(user.getAddress().getCity(), user.getAddress().getStreet());
			addressId = getAddressId(user.getAddress().getCity(), user.getAddress().getStreet());
		}
		try (Connection conn = getConnection();
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO user(name, email, fk_address_id) VALUES(?, ?, ?)");) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setInt(3, addressId);
			ps.executeUpdate();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void deleteUser(int id) {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM user WHERE iduser = ?");) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void updateUser(int id, User user) {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("UPDATE user SET name = ?, email = ? WHERE iduser = ?");) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setInt(3, id);
			ps.executeUpdate();
			updateAddress(id, user.getAddress());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static List<User> getUsersOfAddress(int id) {

		List<User> users = new ArrayList<User>();

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT iduser, name, email, fk_user_id FROM user, address WHERE fk_address_id = idaddress AND idaddress = ?");) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			Address address = getAddressById(id);
			while (rs.next()) {
				Role role = Role.getRoleFor(rs.getInt("fk_role_id"));
				users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), role, address));
			}
			return users;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static Address getAddressById(int id) {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM address WHERE idaddress = ? ");) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			Address address = null;
			if (rs.next()) {
				address = new Address(rs.getInt(1), rs.getString(2), rs.getString(3));
			}
			return address;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static void saveAddress(String city, String street) throws SQLException {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO address(city, street) VALUES(?, ?" + ")");) {
			ps.setString(1, city);
			ps.setString(2, street);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static void updateAddress(int id, Address address) throws SQLException {

		Address userAddress = validateAddress(address.getCity(), address.getStreet());

		if (userAddress == null) {
			saveAddress(address.getCity(), address.getStreet());
			int addressId = getAddressId(address.getCity(), address.getStreet());
			updateUsersAddress(addressId, id);
		} else if (userAddress != null) {
			updateUsersAddress(userAddress.getId(), id);
		}
	}

	private static void updateUsersAddress(int addressId, int userId) {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("UPDATE user SET fk_address_id = ? WHERE iduser = ?");) {
			ps.setInt(1, addressId);
			ps.setInt(2, userId);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void deleteAddress(int id) {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM address WHERE idaddress = ?");) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static Address validateAddress(String city, String street) {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM address WHERE city = ? AND street = ?");) {
			ps.setString(1, city);
			ps.setString(2, street);
			ResultSet rs = ps.executeQuery();
			Address address = null;
			if (rs.next()) {
				address = new Address(rs.getInt(1), rs.getString(2), rs.getString(3));
				return address;
			} else {
				return null;
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static int getUsersCountOfAddress(int id) {

		int count = 0;
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM user WHERE fk_address_id = ?");) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("COUNT(*)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static int getAddressId(String city, String street) {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT idaddress FROM address WHERE city = ? AND street = ? ");) {
			ps.setString(1, city);
			ps.setString(2, street);
			ResultSet rs = ps.executeQuery();
			int id = 0;
			if (rs.next()) {
				id = rs.getInt("idaddress");
			}
			return id;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static Address fetchAddressForUser(int id) throws SQLException {

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT idaddress, city, street FROM address, user WHERE idaddress = fk_address_id AND iduser = ?");) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			Address address = null;
			if (rs.next()) {
				address = new Address(rs.getInt("idaddress"), rs.getString("city"), rs.getString("street"));
			}
			return address;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}

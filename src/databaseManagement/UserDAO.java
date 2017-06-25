package databaseManagement;

import java.sql.*;
import java.util.SortedSet;
import java.util.TreeSet;

import model.User;
import others.PhotoAble;

public class UserDAO {
	private static final String USERS_TABLE = "users";
	private static final String COL_USER_ID = "user_id";
	private static final String COL_FIRST_NAME = "first_name";
	private static final String COL_LAST_NAME = "last_name";
	private static final String COL_USERNAME = "username";
	private static final String COL_PASSWORD = "password";
	private static final String COL_SALT = "salt";
	private static final String COL_EMAIL = "email";
	private static final String COL_IS_ACTIVE = "is_active";
	private static final String COL_IS_ADMIN = "is_admin";
	private static final String COL_HAS_PHOTO = "has_photo";
	private static final String ALL_USERS_COLUMNS = COL_USER_ID + "," + COL_FIRST_NAME + "," + COL_LAST_NAME + ","
			+ COL_USERNAME + "," + COL_PASSWORD + "," + COL_SALT + "," + COL_EMAIL + "," + COL_IS_ACTIVE + ","
			+ COL_IS_ADMIN + "," + COL_HAS_PHOTO;

	private static final String FRIEND_LISTS_TABLE = "";

	private static final String PHOTOS_TABLE = "";
	private static final String COL_PHOTO_ID = "photo_id";
	private static final String COL_PHOTO_FILE = "photo_file";
	private static final String COL_IS_DEFAULT_PHOTO = "is_default";

	public UserDAO() {
	}

	/**
	 * Selects from the database all the necessary parameters for the user
	 * filtered with the given ID and returns a User object constructed with
	 * those parameters.
	 * 
	 * @param ID
	 *            int value of the User's ID in the database
	 * 
	 * @return User object representing user, who has this ID
	 */
	public User getUserByID(int ID) throws SQLException {
		return getUserWithCondition(COL_USER_ID + " = " + ID);
	}

	/**
	 * Selects from the database all the necessary parameters for the user
	 * filtered with the given username and returns a User object constructed
	 * with those parameters.
	 * 
	 * @param username
	 *            String representation of some user's username
	 * 
	 * @return User object representing user who has this username
	 */
	public User getUserByUsername(String username) {
		return getUserWithCondition(COL_USERNAME + " = " + username);
	}

	/*
	 * Helper function for the public "getUser" functions. Returns a User
	 * constructed from the parameters returned by the "select query" from the
	 * table USERS_TABLE. Argument "condition" is a string representing MySQL
	 * condition syntax ("where COLUMN = VALUE..."). "condition" specifies which
	 * row of the table we are interested in. This gives us an ability to get
	 * User by ID or username or other Unique properties of the user.
	 */
	private User getUserWithCondition(String condition) {
		String selectQuery = "select * from " + USERS_TABLE + " u, " + PHOTOS_TABLE + "p where " + "u." + COL_PHOTO_ID
				+ " = " + "p." + COL_PHOTO_ID + "and " + condition;

		ResultSet resultSet = getResultSetWithQuery(selectQuery);

		User res = getUserFromResultSet(resultSet);

		return res;
	}

	/*
	 * Argument "query" is a String representing MySQL "Select" query. The
	 * function Returns a ResultSet returned by the
	 * statement.executeQuery(query).
	 */
	private ResultSet getResultSetWithQuery(String query) {
		ResultSet res = null;

		try {
			Connection connection = DataSource.getDataSource().getConnection();
			Statement statement = connection.createStatement();
			res = statement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/*
	 * Returns a User object which is constructed from the values of the current
	 * row of the given ResultSet object
	 */
	private User getUserFromResultSet(ResultSet resultSet) {
		int userID = -1;
		String firstName = "";
		String lastName = "";
		String username = "";
		String password = "";
		String salt = "";
		String email = "";
		int photoID = -1;

		try {
			userID = resultSet.getInt(COL_USER_ID);
			firstName = resultSet.getString(COL_FIRST_NAME);
			lastName = resultSet.getString(COL_LAST_NAME);
			username = resultSet.getString(COL_USERNAME);
			password = resultSet.getString(COL_PASSWORD);
			salt = resultSet.getString(COL_SALT);
			email = resultSet.getString(COL_EMAIL);
			photoID = resultSet.getInt(COL_PHOTO_ID);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		User res = new User(firstName, lastName, email, username, password, salt, userID);

		if (photoID > 0) {
			try {
				String photo = resultSet.getString(COL_PHOTO_FILE);
				boolean isDefault = resultSet.getBoolean(COL_IS_DEFAULT_PHOTO);
				res.setPhoto(photo, photoID, isDefault);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return res;
	}

	// TODO
	/*
	 * public boolean usernameExists(String username, String password) {
	 * 
	 * }
	 */

	/**
	 * Constructs User objects for all USERS_TABLE rows
	 * 
	 * @return sorted set of all users from the database(SortedSet<User>)
	 */
	public SortedSet<User> getAllUsers() {
		SortedSet<User> result = new TreeSet<User>();
		ResultSet rs = getResultSetWithQuery("select * from " + USERS_TABLE);

		try {
			while (true) {
				result.add(getUserFromResultSet(rs));
				if (rs.isLast())
					break;
				rs.next();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return result;
	}

	/**
	 * TODO Inserts all properties of the given User object to the database
	 * 
	 * @param newUser
	 *            User object
	 */
	public void insertUser(User newUser) throws SQLException {
		try {
			Connection connection = DataSource.getDataSource().getConnection();
			PreparedStatement prepSt = connection
					.prepareStatement("insert into " + USERS_TABLE + " (" + ALL_USERS_COLUMNS + ") values");
			prepSt.executeQuery("insert into " + USERS_TABLE + " (" + ALL_USERS_COLUMNS
					+ ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			prepSt.setInt(1, newUser.getID());
			prepSt.setString(2, newUser.getFirstName());
			prepSt.setString(3, newUser.getLastName());
			prepSt.setString(4, newUser.getUsername());
			prepSt.setString(5, newUser.getPassword());
			prepSt.setString(6, newUser.getSalt());
			prepSt.setString(7, newUser.getEmail());
			prepSt.setBoolean(8, true);
			prepSt.setBoolean(9, newUser.isAdmin());
			prepSt.setBoolean(10, newUser.hasPhoto());

			if (newUser.hasPhoto()) {
				// TODO
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deactivates account of the given user
	 * 
	 * @param u
	 *            User object representing a user whose account is going to be
	 *            deactivated
	 */
	public void deactivateUser(User u) {
		try {
			Connection connection = DataSource.getDataSource().getConnection();
			Statement statement = connection.createStatement();
			statement.executeQuery("update " + USERS_TABLE + " set " + COL_IS_ACTIVE + " = false where " + COL_USER_ID
					+ " = " + u.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
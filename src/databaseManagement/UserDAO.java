package databaseManagement;

import java.sql.*;
import java.util.SortedSet;
import java.util.TreeSet;

import model.User;

public class UserDAO {
	private static final String TABLE_USERS = "users";
	private static final String COL_USER_ID = "user_id";
	private static final String COL_FIRST_NAME = "first_name";
	private static final String COL_LAST_NAME = "last_name";
	private static final String COL_USERNAME = "username";
	private static final String COL_PASSWORD = "password";
	private static final String COL_SALT = "salt";
	private static final String COL_EMAIL = "email";
	private static final String COL_IS_ACTIVE = "is_active";
	private static final String COL_IS_ADMIN = "is_admin";

	private static final String TABLE_PHOTOS = "";
	private static final String COL_PHOTO_ID = "photo_id";
	private static final String COL_PHOTO_FILE = "photo_file";
	private static final String COL_IS_DEFAULT_PHOTO = "is_default";

	private static final String TABLE_FRIEND_LISTS = "friend_lists";
	private static final String COL_FRIEND1 = "user1_id";
	private static final String COL_FRIEND2 = "user2_id";
	private static final String COL_AWAITING_RESPONSE = "awaiting_response";
	private static final String COL_FRIENDSHIP_ACTIVE = "friendship_active";

	/**
	 * Selects from the database all the necessary parameters for the user
	 * filtered with the given ID and returns a User object constructed with
	 * those parameters.
	 * 
	 * @param userID
	 *            int value of the User's ID in the database
	 * 
	 * @return User object representing user, who has this ID. If no such user
	 *         could be found, returns null
	 */
	public User getUserByID(int userID) {
		return getUserWithCondition(COL_USER_ID + " = " + userID);
	}

	/**
	 * Selects from the database all the necessary parameters for the user
	 * filtered with the given username and returns a User object constructed
	 * with those parameters.
	 * 
	 * @param username
	 *            String representation of some user's username
	 * 
	 * @return User object representing user who has this username. If no such
	 *         user could be found, returns null
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
	 * 
	 * If no user could be found, returns null
	 */
	private User getUserWithCondition(String condition) {
		String selectQuery = "select * from " + TABLE_USERS + " u, " + TABLE_PHOTOS + " p where " + "u." + COL_PHOTO_ID
				+ " = " + "p." + COL_PHOTO_ID + " and " + condition + ";";

		ResultSet resultSet = getResultSetWithQuery(selectQuery);

		User res = null;

		try {
			if (resultSet.first())
				res = getUserFromResultSetRow(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		res.setFriends(getUserFriends(res.getID(), true));
		res.setFriendRequests(getUserFriends(res.getID(), false));

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
	 * row of the given ResultSet object. If the row is empty or there's no rows
	 * returns null
	 */
	private User getUserFromResultSetRow(ResultSet resultSet) {
		User res = null;

		try {
			int userID = resultSet.getInt(COL_USER_ID);
			String firstName = resultSet.getString(COL_FIRST_NAME);
			String lastName = resultSet.getString(COL_LAST_NAME);
			String username = resultSet.getString(COL_USERNAME);
			String password = resultSet.getString(COL_PASSWORD);
			String salt = resultSet.getString(COL_SALT);
			String email = resultSet.getString(COL_EMAIL);
			int photoID = resultSet.getInt(COL_PHOTO_ID);

			res = new User(firstName, lastName, email, username, password, salt, userID);

			if (photoID > 0) {
				String photo = resultSet.getString(COL_PHOTO_FILE);
				boolean isDefault = resultSet.getBoolean(COL_IS_DEFAULT_PHOTO);
				res.setPhoto(photo, photoID, isDefault);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Constructs User objects for all USERS_TABLE rows
	 * 
	 * @return sorted set of all users from the database(SortedSet<User>)
	 */
	public SortedSet<User> getAllUsers() {
		SortedSet<User> result = new TreeSet<User>();

		ResultSet rs = getResultSetWithQuery("select * from " + TABLE_USERS);

		try {
			while (rs.next()) {
				result.add(getUserFromResultSetRow(rs));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return result;
	}

	/**
	 * @return number of users of the website
	 */
	public int getNumberOfUsers() {
		int res = 0;

		String countQuery = "select count(1) numberOfUsers from " + TABLE_USERS + ";";

		ResultSet rs = getResultSetWithQuery(countQuery);

		try {
			if (rs.first())
				res = rs.getInt("numberOfUsers");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Inserts all properties of the given User object to the database
	 * 
	 * @param newUser
	 *            User object
	 */
	public void insertUser(User newUser) {
		try {
			Connection connection = DataSource.getDataSource().getConnection();

			String allUserColumns = COL_USER_ID + "," + COL_FIRST_NAME + "," + COL_LAST_NAME + "," + COL_USERNAME + ","
					+ COL_PASSWORD + "," + COL_SALT + "," + COL_EMAIL + "," + COL_PHOTO_ID + "," + COL_IS_ACTIVE + ","
					+ COL_IS_ADMIN;

			String query = "insert into " + TABLE_USERS + " (" + allUserColumns
					+ ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			PreparedStatement prepSt = connection.prepareStatement(query);

			prepSt.setInt(1, newUser.getID());
			prepSt.setString(2, newUser.getFirstName());
			prepSt.setString(3, newUser.getLastName());
			prepSt.setString(4, newUser.getUsername());
			prepSt.setString(5, newUser.getPassword());
			prepSt.setString(6, newUser.getSalt());
			prepSt.setString(7, newUser.getEmail());
			if (newUser.hasPhoto()) {
				prepSt.setInt(8, newUser.getPhotoID());
			} else {
				prepSt.setNull(8, Types.INTEGER);
			}
			prepSt.setBoolean(9, true);
			prepSt.setBoolean(10, newUser.isAdmin());

			prepSt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * If "friendShipActive" is true than returns a set of usernames of all
	 * users who are in the "friend list" of the user with given userID. If
	 * "friendShipActive" is false returns a set of usernames of all users who
	 * has sent this user a "friend request".
	 */
	private SortedSet<String> getUserFriends(int userID, boolean friendshipActive) {
		SortedSet<String> result = new TreeSet<String>();

		String beginning = "select u." + COL_USERNAME + " from " + TABLE_USERS + " u, " + TABLE_FRIEND_LISTS
				+ "f, where f.";

		String end = " = " + userID + " and f." + COL_AWAITING_RESPONSE + " = " + !friendshipActive + " and f."
				+ COL_FRIENDSHIP_ACTIVE + " = " + friendshipActive + ";";

		String query1 = beginning + COL_FRIEND1 + end;

		ResultSet rs1 = getResultSetWithQuery(query1);

		try {
			while (rs1.next()) {
				result.add(rs1.getString(COL_USERNAME));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String query2 = beginning + COL_FRIEND2 + end;

		ResultSet rs2 = getResultSetWithQuery(query2);

		try {
			while (rs2.next()) {
				result.add(rs2.getString(COL_USERNAME));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @param user1ID
	 *            ID of one of "friends"
	 * @param user2ID
	 *            ID of other "friend"
	 * @return True if users with given IDs are "friends"
	 */
	public boolean areFriends(int user1ID, int user2ID) {
		boolean res = false;

		String query = "select (count(1)>0) areFriends from " + TABLE_FRIEND_LISTS + " where ((" + COL_FRIEND1 + " = "
				+ user1ID + " AND " + COL_FRIEND2 + " = " + user2ID + ") OR (" + COL_FRIEND1 + " = " + user2ID + " and "
				+ COL_FRIEND2 + " = " + user1ID + "))" + " and " + COL_FRIENDSHIP_ACTIVE + " = true;";

		ResultSet rs = getResultSetWithQuery(query);

		try {
			res = rs.getBoolean("areFriends");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Adds a friendship relation between users with given IDs. Depending on the
	 * given arguments this relation could mean either a friend request or an
	 * active friendship
	 * 
	 * @param user1ID
	 *            ID of one of "friends"
	 * @param user2ID
	 *            ID of other "friend"
	 * @param awaitingResponse
	 *            if this is true this friendship relation is yet just a "friend
	 *            request". If it is False than second user has either accepted
	 *            or denied the "friend request"
	 * @param friendshipActive
	 *            If this is true than the friendship relation is active
	 */
	public void insertFriendShip(int user1ID, int user2ID, boolean awaitingResponse, boolean friendshipActive) {
		try {
			Connection connection = DataSource.getDataSource().getConnection();

			PreparedStatement prepSt = connection.prepareStatement("insert into " + TABLE_USERS + " (" + COL_FRIEND1
					+ COL_FRIEND2 + COL_AWAITING_RESPONSE + COL_FRIENDSHIP_ACTIVE + ") values (?, ?, ?, ?)");

			prepSt.setInt(1, user1ID);
			prepSt.setInt(2, user2ID);
			prepSt.setBoolean(3, awaitingResponse);
			prepSt.setBoolean(4, friendshipActive);

			prepSt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates a friendship relation between users with given IDs. Depending on
	 * the given arguments this relation could mean either a friend request or
	 * an active friendship
	 * 
	 * @param user1ID
	 *            ID of one of "friends"
	 * @param user2ID
	 *            ID of other "friend"
	 * @param awaitingResponse
	 *            If it is False than second user has either accepted or denied
	 *            the "friend request"
	 * @param friendshipActive
	 *            If this is true than second user has accepted the "friend
	 *            request". If this is False than either second friend has
	 *            denied "friend request" or one of users has removed another
	 *            from his "friend list"
	 */
	public void updateFriendshipStatus(int user1ID, int user2ID, boolean awaitingResponse, boolean friendshipActive) {
		String where = "where (" + COL_FRIEND1 + " = " + user1ID + " AND " + COL_FRIEND2 + " = " + user2ID + ") OR ("
				+ COL_FRIEND1 + " = " + user2ID + " and " + COL_FRIEND2 + " = " + user1ID + ")";

		String setCol = COL_AWAITING_RESPONSE + " = " + awaitingResponse + ", " + COL_FRIENDSHIP_ACTIVE
				+ friendshipActive;

		updateWithQuery(TABLE_FRIEND_LISTS, setCol, where);
	}

	/**
	 * Checks if a user with such username exists in the database
	 * 
	 * @param username
	 *            username of some user we want to know exists or not
	 * @return True if a user with such username exists, False otherwise.
	 */
	public boolean usernameExists(String username) {
		boolean res = false;

		String query = "select (count(1)>0) userExists from " + TABLE_USERS + " u where u." + COL_USERNAME + " = "
				+ username + ";";

		ResultSet rs = getResultSetWithQuery(query);

		try {
			if (rs.first())
				res = rs.getBoolean("userExists");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Checks password of the user with the given ID
	 * 
	 * @param ID
	 *            ID of the user whose password we are checking
	 * @param password
	 *            hash code of the user's password
	 * @return True if the passoword of the user whose ID is "userID" matches
	 *         the given argument "password", False otherwise
	 */
	public boolean passwordIsValid(int userID, String password) {
		boolean res = false;

		String query = "select (count(1)>0) userCanPass from " + TABLE_USERS + " u where u." + COL_USER_ID + " = "
				+ userID + "and u." + COL_PASSWORD + " = " + password + ";";

		ResultSet rs = getResultSetWithQuery(query);

		try {
			if (rs.first())
				res = rs.getBoolean("userCanPass");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Return "salt" for hashing password of the user with the given ID
	 * 
	 * @param userID
	 *            ID of the user whose password "salt" we need
	 * @return "salt" for hashing password of the user with the given ID. Empty
	 *         string if no such user exists or his password doesn't use salt
	 */
	public String getUserSalt(int userID) {
		String res = "";

		String query = "select " + COL_SALT + " from " + TABLE_USERS + " u where u." + COL_USER_ID + " = " + userID
				+ ";";

		ResultSet rs = getResultSetWithQuery(query);

		try {
			if (rs.first())
				res = rs.getString(COL_SALT);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Sets photo of the user with the given ID to the provided new photo
	 * 
	 * @param photoID
	 * @param photoFileName
	 */
	public void updateUserPhoto(int userID, String newPhotoFileName) {
		String selectQuery = "select " + COL_PHOTO_ID + " from " + TABLE_USERS + " where " + COL_USER_ID + " = "
				+ userID;

		ResultSet rs = getResultSetWithQuery(selectQuery);

		try {
			int photoID = rs.getInt(COL_PHOTO_ID);

			updateWithQuery(TABLE_PHOTOS,
					COL_PHOTO_FILE + " = " + newPhotoFileName + ", " + COL_IS_DEFAULT_PHOTO + " = false",
					"where " + COL_PHOTO_ID + " = " + photoID);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// updateWithQuery
	}

	/**
	 * Sets username of the user with the given ID to the provided new username
	 * 
	 * @param userID
	 * @param newUsername
	 */
	public void updateUsername(int userID, String newUsername) {
		updateUserColWhereIdIs(userID, COL_USERNAME, newUsername);
	}

	/**
	 * Sets password of the user with the given ID to the provided new password
	 * 
	 * @param userID
	 * @param newPassword
	 */
	public void updateUserPassword(int userID, String newPassword) {
		updateUserColWhereIdIs(userID, COL_PASSWORD, newPassword);
	}

	/**
	 * Sets email of the user with the given ID to the provided new email
	 * 
	 * @param userID
	 * @param newEmail
	 */
	public void updateUserEmail(int userID, String newEmail) {
		updateUserColWhereIdIs(userID, COL_EMAIL, newEmail);
	}

	/**
	 * Deactivates account of the user with the given ID
	 * 
	 * @param userID
	 *            ID of the user in the database
	 */
	public void deactivateUserAccount(int userID) {
		updateUserColWhereIdIs(userID, COL_IS_ACTIVE, "false");
	}

	private void updateUserColWhereIdIs(int userID, String col, String newVal) {
		updateWithQuery(TABLE_USERS, col + " = " + newVal, "where " + COL_USER_ID + " = " + userID);
	}

	private void updateWithQuery(String tableName, String setCol, String where) {
		try {
			Connection connection = DataSource.getDataSource().getConnection();
			Statement statement = connection.createStatement();
			statement.executeQuery("update " + tableName + " set " + setCol + " " + where + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
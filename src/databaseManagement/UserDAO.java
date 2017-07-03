package databaseManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import model.User;

public class UserDAO extends BasicQuizWebSiteDAO {

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
		PreparedStatement ps = prepareStatementToGetUserWith(DbContract.COL_USER_ID + " = ?");

		try {
			ps.setInt(1, userID);

			ResultSet rs = ps.executeQuery();

			return setUpUserFullyFrom(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
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
		PreparedStatement ps = prepareStatementToGetUserWith(DbContract.COL_USERNAME + " = ?");

		try {
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();

			return setUpUserFullyFrom(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private PreparedStatement prepareStatementToGetUserWith(String condition) {
		String tables = DbContract.TABLE_USERS + " u, " + DbContract.TABLE_PHOTOS + " p";
		String where = "u." + DbContract.COL_PHOTO_ID + " = " + "p." + DbContract.COL_PHOTO_ID;

		if (!condition.isEmpty())
			where = where + " and u." + condition;

		return prepareSelectStatementWith("*", tables, where);
	}

	private User setUpUserFullyFrom(ResultSet rs) {
		User result = getUserFromResultSetRow(rs);

		result.setFriends(getUserFriends(result.getID(), true));
		result.setFriendRequests(getUserFriends(result.getID(), false));

		return result;
	}

	/*
	 * Returns a User object which is constructed from the values of the current
	 * row of the given ResultSet object. If the row is empty or there's no rows
	 * returns null
	 */
	private User getUserFromResultSetRow(ResultSet resultSet) {
		User res = null;

		try {
			int userID = resultSet.getInt(DbContract.COL_USER_ID);
			String firstName = resultSet.getString(DbContract.COL_FIRST_NAME);
			String lastName = resultSet.getString(DbContract.COL_LAST_NAME);
			String username = resultSet.getString(DbContract.COL_USERNAME);
			String password = resultSet.getString(DbContract.COL_PASSWORD);
			String email = resultSet.getString(DbContract.COL_EMAIL);
			int photoID = resultSet.getInt(DbContract.COL_PHOTO_ID);

			res = new User(firstName, lastName, email, username, password, userID);

			String photo = resultSet.getString(DbContract.COL_PHOTO_FILE);
			res.setPhoto(photoID, photo);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Constructs User objects for all USERS_TABLE rows
	 * 
	 * @return sorted Map of all users from the database (SortedMap<String,
	 *         User>). Keys of the map are the username's of users and the
	 *         values are User objects themselves
	 */
	public SortedMap<String, User> getAllUsers() {
		SortedMap<String, User> result = new TreeMap<String, User>();

		PreparedStatement ps = prepareStatementToGetUserWith("");

		try {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				User current = setUpUserFullyFrom(rs);
				result.put(current.getUsername(), current);
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

		String col = "count(1) numberOfUsers";

		PreparedStatement ps = prepareSelectStatementWith(col, DbContract.TABLE_USERS, "");

		try {
			ResultSet rs = ps.executeQuery();

			if (rs.next())
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
			String[] allUserColumns = { DbContract.COL_FIRST_NAME, DbContract.COL_LAST_NAME,
					DbContract.COL_USERNAME, DbContract.COL_PASSWORD, DbContract.COL_EMAIL, DbContract.COL_PHOTO_ID,
					DbContract.COL_USER_IS_ACTIVE, DbContract.COL_IS_ADMIN };

			PreparedStatement prepSt = prepareInsertStatementWith(DbContract.TABLE_USERS, allUserColumns);

			prepSt.setString(1, newUser.getFirstName());
			prepSt.setString(2, newUser.getLastName());
			prepSt.setString(3, newUser.getUsername());
			prepSt.setString(4, newUser.getPassword());
			prepSt.setString(5, newUser.getEmail());

			if (newUser.hasPhoto()) {
				prepSt.setInt(6, newUser.getPhotoID());
			} else {
				prepSt.setNull(6, DbContract.DEFAULT_USER_PHOTO_ID);
			}
			prepSt.setBoolean(7, true);
			prepSt.setBoolean(8, newUser.isAdmin());

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

		String col = "u." + DbContract.COL_USERNAME;
		String tables = DbContract.TABLE_USERS + " u, " + DbContract.TABLE_FRIEND_LISTS + " f";

		String condition = "f.? = ?" + " and f." + DbContract.COL_AWAITING_RESPONSE + " = ?" + " and f."
				+ DbContract.COL_FRIENDSHIP_ACTIVE + " = ?";

		PreparedStatement ps = prepareSelectStatementWith(col, tables, condition);

		String currUsername = "";

		try {
			ps.setString(1, DbContract.COL_FRIEND1);
			ps.setInt(2, userID);
			ps.setBoolean(3, !friendshipActive);
			ps.setBoolean(4, friendshipActive);

			ResultSet rs1 = ps.executeQuery();

			while (rs1.next()) {
				currUsername = rs1.getString(DbContract.COL_USERNAME);
				result.add(currUsername);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			ps.setString(1, DbContract.COL_FRIEND2);

			ResultSet rs2 = ps.executeQuery();

			while (rs2.next()) {
				currUsername = rs2.getString(DbContract.COL_USERNAME);
				result.add(currUsername);
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

		String col = "(count(1)>0) areFriends";
		String condition = "((" + DbContract.COL_FRIEND1 + " = ?" + " AND " + DbContract.COL_FRIEND2 + " = ?" + ") OR ("
				+ DbContract.COL_FRIEND1 + " = ?" + " and " + DbContract.COL_FRIEND2 + " = ?))" + " and "
				+ DbContract.COL_FRIENDSHIP_ACTIVE + " = true;";

		PreparedStatement ps = prepareSelectStatementWith(col, DbContract.TABLE_FRIEND_LISTS, condition);

		try {
			ps.setInt(1, user1ID);
			ps.setInt(2, user2ID);
			ps.setInt(3, user2ID);
			ps.setInt(4, user1ID);

			ResultSet rs = ps.executeQuery();

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
			String[] cols = { DbContract.COL_FRIEND1, DbContract.COL_FRIEND2, DbContract.COL_AWAITING_RESPONSE,
					DbContract.COL_FRIENDSHIP_ACTIVE };

			PreparedStatement prepSt = prepareInsertStatementWith(DbContract.TABLE_USERS, cols);

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
		String condition = "(" + DbContract.COL_FRIEND1 + " = ? AND " + DbContract.COL_FRIEND2 + " = ?) OR ("
				+ DbContract.COL_FRIEND1 + " = ? AND " + DbContract.COL_FRIEND2 + " = ?)";

		String setCol = DbContract.COL_AWAITING_RESPONSE + " = ?, " + DbContract.COL_FRIENDSHIP_ACTIVE + " = ?";

		PreparedStatement ps = prepareUpdateStatementWith(DbContract.TABLE_FRIEND_LISTS, setCol, condition);

		try {
			ps.setBoolean(1, awaitingResponse);
			ps.setBoolean(2, friendshipActive);
			ps.setInt(3, user1ID);
			ps.setInt(4, user2ID);
			ps.setInt(5, user2ID);
			ps.setInt(6, user1ID);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

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

		String col = "(count(1)>0) userExists";
		String table = DbContract.TABLE_USERS + " u";
		String condition = "u." + DbContract.COL_USERNAME + " = ?";

		PreparedStatement ps = prepareSelectStatementWith(col, table, condition);

		try {
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();

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

		String col = "(count(1)>0) userCanPass";
		String table = DbContract.TABLE_USERS + " u";
		String condition = "u." + DbContract.COL_USER_ID + " = ? and u." + DbContract.COL_PASSWORD + " = ?";

		PreparedStatement ps = prepareSelectStatementWith(col, table, condition);

		try {
			ps.setInt(1, userID);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next())
				res = rs.getBoolean("userCanPass");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error in Class: UserDAO, Method : passwordIsValid");
		}

		return res;
	}

	/*	*//**
			 * TODO Sets photo of the user with the given ID to the provided new
			 * photo
			 * 
			 * @param userID
			 * @param newPhotoFileName
			 */
	/*
	 * public void updateUserPhoto(int userID, String newPhotoFileName) { String
	 * table = DbContract.TABLE_USERS + " u, "; String col = "(u." +
	 * DbContract.COL_PHOTO_ID + " = " + DbContract.DEFAULT_USER_PHOTO_ID +
	 * ") hasDefaultPhoto"; String condition = DbContract.COL_USER_ID + " = ?";
	 * 
	 * PreparedStatement ps = prepareSelectStatementWith(col, table, condition);
	 * 
	 * try { ps.setInt(1, userID);
	 * 
	 * ResultSet rs = ps.executeQuery();
	 * 
	 * if (rs.next()) { rs.getBoolean("hasDefaultPhoto"); }
	 * 
	 * ps.executeUpdate();
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); }
	 * 
	 * // ps = prepareUpdateStatementWith(, "", ""); }
	 * 
	 *//**
		 * TODO Removes photo of the user with the given ID and sets it to the
		 * default photo
		 * 
		 * @param userID
		 *//*
		 * public void removeUserPhoto(int userID) { String setCols =
		 * DbContract.COL_PHOTO_ID + " = " + DbContract.DEFAULT_USER_PHOTO_ID;
		 * String condition = DbContract.COL_USER_ID + " = ?";
		 * 
		 * PreparedStatement ps =
		 * prepareUpdateStatementWith(DbContract.TABLE_USERS, setCols,
		 * condition);
		 * 
		 * try { ps.setInt(1, userID);
		 * 
		 * ps.executeUpdate();
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); } }
		 */

	/**
	 * Sets username of the user with the given ID to the provided new username
	 * 
	 * @param userID
	 * @param newUsername
	 */
	public void updateUsername(int userID, String newUsername) {
		updateUsersStringColWith(userID, DbContract.COL_USERNAME, newUsername);
	}

	/**
	 * Sets password of the user with the given ID to the provided new password
	 * 
	 * @param userID
	 * @param newPassword
	 */
	public void updateUserPassword(int userID, String newPassword) {
		updateUsersStringColWith(userID, DbContract.COL_PASSWORD, newPassword);
	}

	/**
	 * Sets email of the user with the given ID to the provided new email
	 * 
	 * @param userID
	 * @param newEmail
	 */
	public void updateUserEmail(int userID, String newEmail) {
		updateUsersStringColWith(userID, DbContract.COL_EMAIL, newEmail);
	}

	/*
	 * Sets some String type column value of the user with ID "userID" in User's
	 * table to "newVal"
	 */
	private void updateUsersStringColWith(int userID, String col, String newVal) {
		String setCol = col + " = ?";
		String condition = DbContract.COL_USER_ID + " = ?";

		PreparedStatement ps = prepareUpdateStatementWith(DbContract.TABLE_USERS, setCol, condition);

		try {
			ps.setString(1, newVal);
			ps.setInt(2, userID);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Deactivates account of the user with the given ID
	 * 
	 * @param userID
	 *            ID of the user in the database
	 */
	public void deactivateUserAccount(int userID) {
		changeUserAccountStatus(userID, false);
	}

	/**
	 * Reactivates account of the user with the given ID
	 * 
	 * @param userID
	 *            ID of the user in the database
	 */
	public void reactivateUserAccount(int userID) {
		changeUserAccountStatus(userID, true);
	}

	/*
	 * Deactivates or reactivates the user account with the ID "userID"
	 * (depending on the boolean parameter "isActive" respectively)
	 */
	private void changeUserAccountStatus(int userID, boolean isActive) {
		String setCol = DbContract.COL_USER_IS_ACTIVE + " = ?";

		updateUsersBooleanColWith(userID, setCol, isActive);
	}

	/**
	 * Turns the user with ID "userID" into an admin or removes him from this
	 * position
	 * 
	 * @param userID
	 * @param isAdmin
	 *            If true the user will become an admin. Or will stop being an
	 *            admin if the value is false
	 */
	public void updateAdminStatus(int userID, boolean isAdmin) {
		String col = DbContract.COL_IS_ADMIN + " = ?";

		updateUsersBooleanColWith(userID, col, isAdmin);
	}

	/*
	 * Sets some boolean type column value of the user with ID "userID" in
	 * User's table to "newVal"
	 */
	private void updateUsersBooleanColWith(int userID, String col, Boolean newVal) {
		String setCol = col + " = ?";
		String condition = DbContract.COL_USER_ID + " = ?";

		PreparedStatement ps = prepareUpdateStatementWith(DbContract.TABLE_USERS, setCol, condition);

		try {
			ps.setBoolean(1, newVal);
			ps.setInt(2, userID);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
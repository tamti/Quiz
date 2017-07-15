package databaseManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import model.Announcement;
import model.Challenge;
import model.Message;
import model.User;

public class UserDAO extends BasicQuizWebSiteDAO {

	/**
	 * Selects from the database all the necessary parameters for the user
	 * filtered with the given username and returns a User object constructed
	 * with those parameters.
	 * 
	 * @param username
	 *
	 * @return User object representing user who has this username. Null if no
	 *         such user could be found
	 */
	public User getUserByUsername(String username) {
		SortedMap<String, User> map = getUserMap(DbContract.COL_USERNAME, username);

		User result = map.get(username);

		return result;
	}

	/**
	 * Selects from the database all the necessary parameters for the user
	 * filtered with the given email and returns a User object constructed with
	 * those parameters.
	 * 
	 * @param email
	 *
	 * @return User object representing user who has this email. Null if no such
	 *         user could be found
	 */
	public User getUserByEmail(String Email) {
		SortedMap<String, User> map = getUserMap(DbContract.COL_EMAIL, Email);

		User result = map.get(Email);

		return result;
	}

	public String getUsername(int userID) {
		String result = "";

		String condition = DbContract.COL_USER_ID + " = ?";

		String query = prepareSelectStatementWith(DbContract.COL_USERNAME, DbContract.TABLE_USERS, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, userID);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					result = rs.getString(DbContract.COL_USERNAME);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/*
	 * Helper function for "getUser..." methods. If parameter "val" is an empty
	 * string returns a Map keys of which are usernames and values are User
	 * objects. If "val" is not an empty string returns Map with only one entry:
	 * User whose value of the column [col] is [val]
	 */
	private SortedMap<String, User> getUserMap(String col, String val) {
		SortedMap<String, User> result = null;

		if (!val.isEmpty()) {
			String condition = col + " = ?";

			String query = prepareSelectStatementWith("*", DbContract.TABLE_USERS, condition);

			try (Connection con = DataSource.getDataSource().getConnection();
					PreparedStatement ps = con.prepareStatement(query)) {

				ps.setString(1, val);

				result = getUserWithPreparedStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {

			String query = prepareSelectStatementWith("*", DbContract.TABLE_USERS, "");

			try (Connection con = DataSource.getDataSource().getConnection();
					PreparedStatement ps = con.prepareStatement(query)) {

				result = getUserWithPreparedStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private SortedMap<String, User> getUserWithPreparedStatement(PreparedStatement ps) {
		SortedMap<String, User> result = new TreeMap<String, User>();
		try (ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int userID = rs.getInt(DbContract.COL_USER_ID);
				String firstName = rs.getString(DbContract.COL_FIRST_NAME);
				String lastName = rs.getString(DbContract.COL_LAST_NAME);
				String password = rs.getString(DbContract.COL_PASSWORD);
				String email = rs.getString(DbContract.COL_EMAIL);
				String username = rs.getString(DbContract.COL_USERNAME);
				int photoID = rs.getInt(DbContract.COL_PHOTO_ID);

				SortedSet<String> friends = getUserFriends(userID, true);
				SortedSet<String> friendRequests = getUserFriends(userID, false);

				User u = new User(userID, firstName, lastName, email, username, password, friends, friendRequests);
				// String photo = rs.getString(DbContract.COL_PHOTO_FILE);

				u.setPhotoID(photoID);

				result.put(u.getUsername(), u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Constructs User objects for all USERS_TABLE rows
	 * 
	 * @return Map of all users from the database (SortedMap<String, User>).
	 *         Keys of the map are the usernames and the values are User objects
	 *         themselves
	 */
	public SortedMap<String, User> getAllUsers() {
		return getUserMap("", "");
	}

	/**
	 * @return number of users of the website
	 */
	public int getNumberOfUsers() {
		int res = 0;

		String col = "count(1) numberOfUsers";

		String query = prepareSelectStatementWith(col, DbContract.TABLE_USERS, "");

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

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
	 * @return ID of the new User in the database
	 */
	public int insertUser(User newUser) {
		String[] allUserColumns = { DbContract.COL_FIRST_NAME, DbContract.COL_LAST_NAME, DbContract.COL_USERNAME,
				DbContract.COL_PASSWORD, DbContract.COL_EMAIL, DbContract.COL_PHOTO_ID, DbContract.COL_USER_IS_ACTIVE,
				DbContract.COL_IS_ADMIN };

		String query = prepareInsertStatementWith(DbContract.TABLE_USERS, allUserColumns);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, newUser.getFirstName());
			ps.setString(2, newUser.getLastName());
			ps.setString(3, newUser.getUsername());
			ps.setString(4, newUser.getPassword());
			ps.setString(5, newUser.getEmail());
			ps.setInt(6, DbContract.DEFAULT_USER_PHOTO_ID);
			ps.setBoolean(7, true);
			ps.setBoolean(8, newUser.isAdmin());

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return getLastIdOf(DbContract.TABLE_USERS, DbContract.COL_USER_ID);
	}

	/*
	 * If "friendShipActive" is true than returns a set of usernames of all
	 * users who are in the "friend list" of the user with given userID. If
	 * "friendShipActive" is false returns a set of usernames of all users who
	 * have sent this user a "friend request" and haven't yet received an
	 * answer.
	 */
	public SortedSet<String> getUserFriends(int userID, boolean friendshipActive) {
		SortedSet<String> result = getUserFriendsFromSide(userID, friendshipActive, DbContract.COL_FRIEND1,
				DbContract.COL_FRIEND2);

		result.addAll(getUserFriendsFromSide(userID, friendshipActive, DbContract.COL_FRIEND2, DbContract.COL_FRIEND1));

		return result;
	}

	/*
	 * Parameters "sideCol" and "otherSideCol" should be either of column names
	 * DbContract.COL_FRIEND1 and DbContract.COL_FRIEND2 (SHOULD NOT BE SAME).
	 * This method returns a sorted set of usernames of "friends" of the user
	 * whose value in the column [sideCol] is [userID]. "otherSideCol" is needed
	 * for joining tables DbContract.TABLE_USERS and
	 * DbContract.TABLE_FRIEND_LISTS
	 */
	private SortedSet<String> getUserFriendsFromSide(int userID, boolean friendshipActive, String sideCol,
			String otherSideCol) {
		SortedSet<String> result = new TreeSet<String>();

		String col = "u." + DbContract.COL_USERNAME;
		String tables = DbContract.TABLE_USERS + " u, " + DbContract.TABLE_FRIEND_LISTS + " f";

		String condition = "u." + DbContract.COL_USER_ID + " = f." + otherSideCol + " AND f." + sideCol + " = ? AND f."
				+ DbContract.COL_AWAITING_RESPONSE + " = ?" + " AND f." + DbContract.COL_FRIENDSHIP_ACTIVE + " = ?";

		String query = prepareSelectStatementWith(col, tables, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, userID);
			ps.setBoolean(2, !friendshipActive);
			ps.setBoolean(3, friendshipActive);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					String currUsername = rs.getString("u." + DbContract.COL_USERNAME);
					result.add(currUsername);
				}
			} catch (SQLException e) {
				e.printStackTrace();
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

		String query = prepareSelectStatementWith(col, DbContract.TABLE_FRIEND_LISTS, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, user1ID);
			ps.setInt(2, user2ID);
			ps.setInt(3, user2ID);
			ps.setInt(4, user1ID);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					res = rs.getBoolean("areFriends");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

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
		String[] cols = { DbContract.COL_FRIEND1, DbContract.COL_FRIEND2, DbContract.COL_AWAITING_RESPONSE,
				DbContract.COL_FRIENDSHIP_ACTIVE };

		String query = prepareInsertStatementWith(DbContract.TABLE_FRIEND_LISTS, cols);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, user1ID);
			ps.setInt(2, user2ID);
			ps.setBoolean(3, awaitingResponse);
			ps.setBoolean(4, friendshipActive);

			ps.executeUpdate();

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

		String query = prepareUpdateStatementWith(DbContract.TABLE_FRIEND_LISTS, setCol, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

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

		String query = prepareSelectStatementWith(col, table, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, username);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.first())
					res = rs.getBoolean("userExists");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Sets photo of the user with the given ID to the provided new photo
	 * 
	 * @param userID
	 * @param newPhotoFileName
	 */
	/*
	 * TODO public void updateUserPhoto(int userID, String newPhotoFileName) {
	 * String table = DbContract.TABLE_USERS + " u, "; String col = "(u." +
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
	 * ps = prepareUpdateStatementWith(, "", ""); }
	 */

	/**
	 * Removes photo of the user with the given ID and sets it to the default
	 * photo
	 * 
	 * @param userID
	 */
	/*
	 * TODO public void removeUserPhoto(int userID) { String setCols =
	 * DbContract.COL_PHOTO_ID + " = " + DbContract.DEFAULT_USER_PHOTO_ID;
	 * String condition = DbContract.COL_USER_ID + " = ?";
	 * 
	 * PreparedStatement ps = prepareUpdateStatementWith(DbContract.TABLE_USERS,
	 * setCols, condition);
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
	 * Sets some String type column [col] value of the specified user entry in
	 * table DbContract.TABLE_USERS to "newVal"
	 */
	private void updateUsersStringColWith(int userID, String col, String newVal) {
		String setCol = col + " = ?";
		String condition = DbContract.COL_USER_ID + " = ?";

		String query = prepareUpdateStatementWith(DbContract.TABLE_USERS, setCol, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

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
		updateUsersBooleanColWith(userID, DbContract.COL_USER_IS_ACTIVE, isActive);
	}

	public boolean UserActivationStatus(int userID){
		boolean res = false;

		String col = DbContract.COL_USER_IS_ACTIVE;
		String table = DbContract.TABLE_USERS;
		String condition = DbContract.COL_USER_ID + " = ?";

		String query = prepareSelectStatementWith(col, table, condition);
		
		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, userID);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.first())
					res = rs.getBoolean(DbContract.COL_USER_IS_ACTIVE);

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
		
	}
	
	/**
	 * Updates value of the column DbContract.COL_IS_ADMIN of the specified user
	 * 
	 * @param userID
	 * @param isAdmin
	 *            If true Sets value of the column DbContract.COL_IS_ADMIN to
	 *            true. Sets to false otherwise
	 */
	public void updateAdminStatus(int userID, boolean isAdmin) {
		updateUsersBooleanColWith(userID, DbContract.COL_IS_ADMIN, isAdmin);
	}

	// getting info is user admin or not
	public boolean getAdminStatus(int userID) {
		boolean res = false;

		String col = DbContract.COL_IS_ADMIN;
		String table = DbContract.TABLE_USERS;
		String condition = DbContract.COL_USER_ID + " = ?";

		String query = prepareSelectStatementWith(col, table, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, userID);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.first())
					res = rs.getBoolean(DbContract.COL_IS_ADMIN);

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/*
	 * Sets some boolean type column [col] value of the specified user entry in
	 * table DbContract.TABLE_USERS to "newVal"
	 */
	private void updateUsersBooleanColWith(int userID, String col, Boolean newVal) {
		String setCol = col + " = ?";
		String condition = DbContract.COL_USER_ID + " = ?";

		String query = prepareUpdateStatementWith(DbContract.TABLE_USERS, setCol, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setBoolean(1, newVal);
			ps.setInt(2, userID);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Returns all messages sent to the specified user
	 * 
	 * @param receiverID
	 * @return SortedSet of Messages sent to the specified user
	 */
	public SortedSet<Message> getMessage(int receiverID) {
		SortedSet<Message> result = new TreeSet<Message>();

		String condition = DbContract.COL_RECEIVER_ID + " = ?";

		String query = prepareSelectStatementWith("*", DbContract.TABLE_MESSAGES, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, receiverID);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					int senderID = rs.getInt(DbContract.COL_SENDER_ID);
					String txt = rs.getString(DbContract.COL_MESSAGE_TEXT);
					Timestamp sentOn = rs.getTimestamp(DbContract.COL_TIME_SENT);

					Message msg = new Message(senderID, receiverID, txt, sentOn);

					result.add(msg);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * Returns all challenges sent to the specified user with specified
	 * condition
	 * 
	 * @param receiverID
	 * @param challangeSeen
	 *            whether the challenge has already been accepted or declined by
	 *            the user or not he still hasn't respond/seen it
	 * @param challangeAccepted
	 *            whether the challenge has been accepted (and therefore
	 *            completed as well) or not
	 * @return if challangeSeen is false returns all challenges which user
	 *         hasn't respond to yet; if challangeSeen is true and
	 *         challangeAccepted is true as well returns all challenges which
	 *         user has already accepted and therefore completed
	 */
	public SortedSet<Challenge> getChallangeRequest(int receiverID, boolean challangeSeen, boolean challangeAccepted) {
		SortedSet<Challenge> result = new TreeSet<Challenge>();

		String tables = DbContract.TABLE_CHALLENGES + " c, " + DbContract.TABLE_MESSAGES + " m";
		String condition = "m." + DbContract.COL_MESSAGE_ID + " = c." + DbContract.COL_MESSAGE_ID + " AND m."
				+ DbContract.COL_RECEIVER_ID + " = ? AND c." + DbContract.COL_CHALLANGE_SEEN + " = ? AND c."
				+ DbContract.COL_CHALLANGE_ACCEPTED + " = ?";

		String query = prepareSelectStatementWith("*", tables, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, receiverID);
			ps.setBoolean(2, challangeSeen);
			ps.setBoolean(3, challangeAccepted);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					int senderID = rs.getInt(DbContract.COL_SENDER_ID);
					int quizID = rs.getInt(DbContract.COL_QUIZ_ID);
					String txt = rs.getString(DbContract.COL_MESSAGE_TEXT);
					Timestamp sentOn = rs.getTimestamp(DbContract.COL_TIME_SENT);

					Challenge msg = new Challenge(senderID, receiverID, quizID, txt, sentOn, challangeSeen,
							challangeAccepted);

					result.add(msg);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public int insertMessage(Message msg) {
		String[] cols = { DbContract.COL_SENDER_ID, DbContract.COL_RECEIVER_ID, DbContract.COL_TIME_SENT,
				DbContract.COL_MESSAGE_TEXT };

		String query = prepareInsertStatementWith(DbContract.TABLE_MESSAGES, cols);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, msg.getSender());
			ps.setInt(2, msg.getReceiver());
			ps.setTimestamp(3, msg.getTime());
			ps.setString(4, msg.getText());

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return getLastIdOf(DbContract.TABLE_MESSAGES, DbContract.COL_MESSAGE_ID);
	}

	public void insertChallenge(Challenge challenge) {
		int msgID = insertMessage(challenge);
		challenge.setMsgID(msgID);
		insertChallengePart(challenge);
	}

	private void insertChallengePart(Challenge challenge) {
		String[] cols = { DbContract.COL_MESSAGE_ID, DbContract.COL_QUIZ_ID, DbContract.COL_CHALLANGE_SEEN,
				DbContract.COL_CHALLANGE_ACCEPTED };

		String query = prepareInsertStatementWith(DbContract.TABLE_CHALLENGES, cols);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, challenge.getMsgID());
			ps.setInt(2, challenge.getQuiz());
			ps.setBoolean(3, challenge.challengeSeen());
			ps.setBoolean(4, challenge.challengeAccepted());

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addAnnouncement(int senderID, Announcement ann) {
		String[] cols = { DbContract.COL_ANNOUNCER_ID, DbContract.COL_ANNOUNCED_ON, DbContract.COL_ANNOUNCEMENT_TEXT };

		String query = prepareInsertStatementWith(DbContract.TABLE_ANNOUNCEMENTS, cols);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, senderID);
			ps.setTimestamp(2, ann.getTime());
			ps.setString(3, ann.getText());

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Announcement> getAllAnnouncements() {
		ArrayList<Announcement> result = new ArrayList<Announcement>();

		String cond = "1=1 ORDER BY " + DbContract.COL_ANNOUNCED_ON + " desc";

		String query = prepareSelectStatementWith("*", DbContract.TABLE_ANNOUNCEMENTS, cond);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String sentFrom = getUsername(rs.getInt(DbContract.COL_ANNOUNCER_ID));
				String txt = rs.getString(DbContract.COL_ANNOUNCEMENT_TEXT);
				Timestamp sentOn = rs.getTimestamp(DbContract.COL_ANNOUNCED_ON);

				Announcement ann = new Announcement(sentFrom, txt, sentOn);
				result.add(ann);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
package model;

import java.util.SortedMap;

import databaseManagement.UserDAO;
import others.PasswordStorage;
import others.PasswordStorage.CannotPerformOperationException;
import others.PasswordStorage.InvalidHashException;

public class AccountManager {
	private UserDAO uDao;
	private SortedMap<String, User> users;

	/**
	 * Initializes all necessary objects for behind the scene operations
	 */
	public AccountManager() {
		uDao = new UserDAO();
		users = uDao.getAllUsers();
	}

	/**
	 * 
	 * Creates a new user account (add all necessary information about new user
	 * to the database) and returns a User object constructed with the
	 * parameters of the newly create account
	 * 
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param username
	 * @param password
	 * @return User object of the newly created user, constructed with the given
	 *         parameters
	 */
	public User createAccount(String firstName, String lastName, String email, String username, String password) {
		String passwordHash = "";

		try {
			passwordHash = PasswordStorage.createHash(password);
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
		}

		User newUser = new User(firstName, lastName, email, username, passwordHash);

		uDao.insertUser(newUser);

		users.put(username, newUser);

		return newUser;
	}

	/**
	 * 
	 * Checks if the given login info is valid
	 * 
	 * @param username
	 * @param password
	 * @return True if a user with given username exists and the given password
	 *         matches with the password of that user. False otherwise
	 */
	public boolean canPass(String username, String password) {
		boolean result = false;

		if (users.containsKey(username)) {

			User u = users.get(username);

			String correctHash = u.getPassword();

			try {
				result = PasswordStorage.verifyPassword(password, correctHash);

			} catch (CannotPerformOperationException | InvalidHashException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	// TODO
	public void addProfilePictureTo(String username, String profilePictureName) {
		User u = users.get(username);

		// uDao.updatePhoto();
		// u.setPhoto(0, profilePictureName);

	}

	/**
	 * 
	 * Deactivates an account of the specified user
	 * 
	 * @param username
	 */
	public void deactivateAccount(String username) {
		int userID = users.get(username).getID();

		uDao.deactivateUserAccount(userID);

		users.remove(username);
	}

	/**
	 * 
	 * Saves a friend request info. Used when a user sends a friend request to
	 * the other user. The receiver of the friend request will then accept or
	 * reject it
	 * 
	 * @param senderUsername
	 * @param receiverUsername
	 */
	public void sendFriendRequest(String senderUsername, String receiverUsername) {
		User sender = users.get(senderUsername);
		User reciever = users.get(receiverUsername);

		uDao.insertFriendShip(sender.getID(), reciever.getID(), true, false);

		reciever.addFriendRequest(sender.getUsername());
	}

	/**
	 * 
	 * saves a info about new friend relation. Used when a user accepts other's
	 * friend request
	 * 
	 * @param friend1Username
	 * @param friend2Username
	 */
	public void makeFriends(String friend1Username, String friend2Username) {
		User user1 = users.get(friend1Username);
		User user2 = users.get(friend2Username);

		uDao.updateFriendshipStatus(user1.getID(), user2.getID(), false, true);

		user1.addFriend(friend2Username);
		user2.addFriend(friend1Username);
	}

	/**
	 * 
	 * removes a user from the friend list of the other user
	 * 
	 * @param username
	 *            username of the user who is removing the second user from his
	 *            friend list
	 * @param friendUsername
	 *            username of the user who will be removed from the friend list
	 */
	public void removeFromFriends(String username, String friendUsername) {
		User user = users.get(username);
		User friend = users.get(friendUsername);

		uDao.updateFriendshipStatus(user.getID(), friend.getID(), false, false);

		user.removeFriend(friend);
		friend.removeFriend(user);
	}

	/**
	 * 
	 * Assigns the specified user as an admin, so that he would be able to use
	 * admin authority
	 * 
	 * @param username
	 */
	public void makeAdmin(String username) {
		User newAdmin = users.get(username);
		uDao.updateAdminStatus(newAdmin.getID(), true);
	}

	/**
	 * 
	 * removes the specified user from the admin position
	 * 
	 * @param username
	 */
	public void removeFromAdminPost(String username) {
		User oldAdmin = users.get(username);
		uDao.updateAdminStatus(oldAdmin.getID(), false);
	}

}

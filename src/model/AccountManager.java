package model;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.SortedSet;

import databaseManagement.StatisticsDAO;
import databaseManagement.UserDAO;
import others.PasswordStorage;
import others.PasswordStorage.CannotPerformOperationException;
import others.PasswordStorage.InvalidHashException;

public class AccountManager {
	protected UserDAO uDao;
	private StatisticsDAO sDao;
	private SortedMap<String, User> users;

	/**
	 * Initializes all necessary objects for behind the scene operations
	 */
	public AccountManager() {
		uDao = new UserDAO();
		sDao = new StatisticsDAO();
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
	 * @return True if new User account was created with no problems. False if
	 *         user with such username already existed or given password had
	 *         less than 6 characters or other errors occurred
	 */
	public boolean createAccount(String firstName, String lastName, String email, String username, String password) {

		String passwordHash = "";

		try {
			passwordHash = PasswordStorage.createHash(password);
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
			return false;
		}

		User newUser = new User(firstName, lastName, email, username, passwordHash);

		int idOfNewUser = uDao.insertUser(newUser);

		if (idOfNewUser < 1)
			return false;

		newUser.setID(idOfNewUser);

		users.put(username, newUser);

		return true;
	}

	/**
	 * 
	 * @param username
	 * @return User object corresponding to the user with given username
	 */
	public User getUser(String username) {
		if (users.containsKey(username))
			return users.get(username);

		User u = uDao.getUserByUsername(username);
		
		if (u != null) {
			SortedSet<Statistics> stats = sDao.getStatisticsByUser(u.getID());
			
			u.setStats(stats);
			
			users.put(u.getUsername(), u);
		}
		return u;
	}

	public SortedMap<String, User> getAllUsers() {
		return users;
	}
	
	public String getUserById(int user_id) {
		if (!uDao.getUsername(user_id).equals("")) {
			return uDao.getUsername(user_id);
		} else {
			throw new RuntimeException("User doesn't excist");
		}

	}

	/**
	 * 
	 * @param email
	 * @return User object corresponding to the user with given username
	 */
	/*
	 * public User getUserByEmail(String email) { User u =
	 * uDao.getUserByEmail(email);
	 * 
	 * if (u != null) {
	 * 
	 * users.put(u.getUsername(), u); }
	 * 
	 * return u; }
	 */

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

		if (usernameExists(username)) {

			User u = getUser(username);

			String correctHash = u.getPassword();

			try {
				result = PasswordStorage.verifyPassword(password, correctHash);

			} catch (CannotPerformOperationException | InvalidHashException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public boolean usernameExists(String username) {
		return users.containsKey(username) || uDao.usernameExists(username);
	}

	// TODO
	// public void addProfilePictureTo(String username, String
	// profilePictureName) {
	// User u = users.get(username);

	// uDao.updatePhoto();
	// u.setPhoto(0, profilePictureName);

	// }

	/**
	 * 
	 * Deactivates an account of the specified user
	 * 
	 * @param username
	 */
	public void deactivateAccount(String username) {
		int userID = getUser(username).getID();

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
		User sender = getUser(senderUsername);
		
		User receiver = getUser(receiverUsername);

		uDao.insertFriendShip(sender.getID(), receiver.getID(), true, false);

		receiver.addFriendRequest(sender.getUsername());
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
		User user1 = getUser(friend1Username);
		User user2 = getUser(friend2Username);

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
	public void removeFromFriendsOf(String username, String friendUsername) {
		User user = getUser(username);
		User friend = getUser(friendUsername);

		uDao.updateFriendshipStatus(user.getID(), friend.getID(), false, false);

		user.removeFriend(friend.getUsername());
		friend.removeFriend(user.getUsername());
	}

	
	public User [] searchUsers(String text){
		ArrayList<User> users = new ArrayList<>();
		AccountManager man = new AccountManager();
		SortedMap<String, User> alluser = man.getAllUsers() ;
		for (SortedMap.Entry<String, User> entry : alluser.entrySet()){
			if(entry.getKey().contains(text)){
				users.add(entry.getValue());
				System.out.println(entry.getKey());
			}
		}
		User [] answer = new User [users.size()];
		return users.toArray(answer);
		
	}
	
	/**
	 * 
	 * Assigns the specified user as an admin, so that he would be able to use
	 * admin authority
	 * 
	 * @param username
	 */
	public boolean isAdminAcc(String username) {
		User user = getUser(username);
		return uDao.getAdminStatus(user.getID());
	}

	public boolean isActiveAccount(String username) {
		return uDao.UserActivationStatus(username);
	}

	public ArrayList<Announcement> getAnnouncements() {
		ArrayList<Announcement> ann = new ArrayList<Announcement>();
		ann = uDao.getAllAnnouncements();
		return ann;
	}
	
	public void SendMessage(String from, String to, String msg){
		int from_id = getUser(from).getID();
		int to_id = getUser(to).getID();
		Message m = new Message(from_id, to_id, msg);
		uDao.insertMessage(m);
	}

	public SortedSet<Message> getMessagesOf(int userID) {		
		return uDao.getMessage(userID);
	}
	
	public void SendChallenge(String from, String to, int quizID, String msg) {
		int fromID = getUser(from).getID();
		int toID = getUser(to).getID();
		Challenge chal = new Challenge(fromID, toID, quizID, msg, false, false);
		uDao.insertChallenge(chal);
	}
	
	public SortedSet<Challenge> getChallengesOf(int userID) {
		return uDao.getChallangeRequest(userID);
	}
	
	public void acceptChallenge(int challengeID) {
		uDao.updateChallenge(challengeID, true, true);
	}
	
	public void denyChallenge(int challengeID) {
		uDao.updateChallenge(challengeID, true, false);
	}
}

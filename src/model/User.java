package model;

import java.util.SortedSet;
import java.util.TreeSet;

import others.PhotoAble;

public class User extends PhotoAble implements Comparable<User> {
	private int ID;
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String password;
	private String salt;
	private boolean isAdmin;
	private SortedSet<String> friends;
	private SortedSet<String> friendRequests;
	private SortedSet<Statistics> stats;

	public User(String firstName, String lastName, String email, String username, String password, String salt,
			SortedSet<String> friends, SortedSet<String> friendRequests) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.friends = friends;
		this.friendRequests = friendRequests;
		isAdmin = false;
		stats = new TreeSet<Statistics>();
	}

	public User(String firstName, String lastName, String email, String username, String password, String salt) {
		this(firstName, lastName, email, username, password, salt, new TreeSet<String>(), new TreeSet<String>());
	}

	public User(String firstName, String lastName, String email, String username, String password, String salt,
			int ID) {
		this(firstName, lastName, email, username, password, salt);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getURL() {
		return "User.jsp?username=" + username;
	}

	public void addFriend(String username) {
		friendRequests.remove(username);
		friends.add(username);
	}

	public SortedSet<String> getFriends() {
		return friends;
	}

	public void setFriends(SortedSet<String> friends) {
		this.friends = friends;
	}

	public void addFriendRequest(String username) {
		friendRequests.add(username);
	}

	public SortedSet<String> getFriendRequests() {
		return friendRequests;
	}

	public void setFriendRequests(SortedSet<String> friendRequests) {
		this.friendRequests = friendRequests;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void makeAdmin() {
		isAdmin = true;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public boolean isFriend(String username) {
		return friends.contains(username);
	}

	public void removeFriend(User friend) {
		friends.remove(friend);
	}

	public SortedSet<Statistics> getStats() {
		return stats;
	}

	public void setStats(SortedSet<Statistics> stats) {
		this.stats = stats;
	}

	/**
	 * @return firstName + " " + lastName; Full name of the user
	 */
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

	public boolean equals(User other) {
		return this.ID == other.getID();
	}

	/**
	 * Compares User objects. Comparison is made with their usernames
	 * 
	 * @param other
	 *            User object to compare to
	 * @return a negative integer, zero, or a positive integer as the
	 *         other.getUsername() is greater than, equal to, or less than
	 *         this.username, ignoring case considerations.
	 */
	@Override
	public int compareTo(User other) {
		return this.username.compareToIgnoreCase(other.getUsername());
	}
}
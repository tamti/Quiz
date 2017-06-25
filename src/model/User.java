package model;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import others.PhotoAble;

@SuppressWarnings("rawtypes")
public class User extends PhotoAble implements Comparable {
	private int ID;
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String password;
	private String salt;
	private boolean isAdmin;
	private SortedSet <User> friends;
	
	public User(String firstName, String lastName, String email, String username, String password, String salt,
			SortedSet<User> friends) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.friends = friends;
		isAdmin = false;
	}

	public User(String firstName, String lastName, String email, String username, String password, String salt) {
		this(firstName, lastName, email, username, password, salt, new TreeSet<User>());
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
	
	public String getURL(){
		return "User.jsp?username="+username;
	}
	
	public void addFriend(User user){
		friends.add(user);
	}
	
	public SortedSet<User> getFriends() {
		return friends;
	}
	
	public void setFriends(SortedSet<User> friends) {
		this.friends = friends;
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
	
	public boolean isFriend(User u) {
		return friends.contains(u);
	}
	
	public boolean isFriend(String username) {
		Iterator it = friends.iterator();
		while (it.hasNext()) {
			User curr = (User) it.next();
			if (curr.getUsername().equals(username))
				return true;
		}
		return false;
	}
	
	public boolean isFriend(int ID) {
		Iterator it = friends.iterator();
		while (it.hasNext()) {
			User curr = (User) it.next();
			if (curr.getID() == ID)
				return true;
		}
		return false;
	}
	
	public void removeFriend(User friend){
		friends.remove(friend);
	}
	
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
	
	@Override
	public boolean equals(Object o) {
		User other = (User) o;
		return username.equals(other.getUsername());
	}

	@Override
	public int compareTo(Object o) {
		User other = (User) o;
		return username.compareToIgnoreCase(other.toString());
	}
}
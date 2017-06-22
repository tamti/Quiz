package model;

import java.util.ArrayList;

public class User {
	int userID;
	String firstname;
	String lastname;
	String email;
	String username;
	String password;
	ArrayList <User> friends;
	
	public User(String firstname, String lastname, String email,
			String username, String password, int ID) {
		super();
		this.userID = ID;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.username = username;
		this.password = password;
		friends = new ArrayList<>();
	}
	public User(String firstname, String lastname, String email,
			String username, String password) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.username = username;
		this.password = password;
		friends = new ArrayList<>();
	}
	public User(String firstname, String lastname, String email,
			String username, String password, ArrayList<User> friends) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.username = username;
		this.password = password;
		this.friends = friends;
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getURL(){
		return "User.jsp?username="+username;
	}
	
	public void addFriend(User user){
		friends.add(user);
	}
	public ArrayList<User> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<User> friends) {
		this.friends = friends;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
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
	public boolean isFriend(User other){
		for(int i=0; i<friends.size(); i++){
			User friend = friends.get(i);
			if(friend.getUsername().equals(other.getUsername())) 
				return true;
		}
		return false;
	}
	public void removeFriend(String userName){
		for(int i =0; i<=friends.size(); i++){
			if(friends.get(i).getUsername().equals(userName)){
				friends.remove(i);
				break;
			}
		}
	}
	
	@Override
	public String toString(){
		return firstname + " " + lastname;
	}
}
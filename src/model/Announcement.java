package model;

import java.sql.Timestamp;

public class Announcement {
	private String username;
	private String txt;
	private Timestamp sentOn;
	
	public Announcement (String senderUsername, String txt, Timestamp sentOn) {
		this.username = senderUsername;
		this.txt = txt;
		this.sentOn = sentOn;
	}
	
	public Announcement(String senderUsername, String txt) {
		this(senderUsername, txt ,new Timestamp(new java.util.Date().getTime()));
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getText() {
		return txt;
	}

	public Timestamp getTime() {
		return sentOn;
	}

	public String getSender() {
		return username;
	}

}

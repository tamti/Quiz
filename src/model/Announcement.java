package model;

import java.sql.Timestamp;

public class Announcement {
	private int annID;
	private int sentFrom;
	private String txt;
	private Timestamp sentOn;
	
	public Announcement (int sentFrom, String txt, Timestamp sentOn) {
		this.sentFrom = sentFrom;
		this.txt = txt;
		this.sentOn = sentOn;
	}
	
	public Announcement(int sentFrom, String txt) {
		this(sentFrom, txt, new Timestamp(new java.util.Date().getTime()));
	}
	
	public String getText() {
		return txt;
	}

	public Timestamp getTime() {
		return sentOn;
	}

	public int getSender() {
		return sentFrom;
	}

}

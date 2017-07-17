package model;

import java.sql.Timestamp;

public class Message implements Comparable<Message> {
	private int msgID;
	private int sentFrom;
	private int sentTo;
	private String txt;
	private Timestamp sentOn;

	public Message(int sentFrom, int sentTo, String txt, Timestamp sentOn) {
		this.sentFrom = sentFrom;
		this.sentTo = sentTo;
		this.txt = txt;
		this.sentOn = sentOn;
	}

	public Message(int sentFrom, int sentTo, String txt) {
		this(sentFrom, sentTo, txt, new Timestamp(new java.util.Date().getTime()));
	}
	
	public void setMsgID(int msgID) {
		this.msgID = msgID;
	}
	
	public int getMsgID() {
		return msgID;
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

	public int getReceiver() {
		return sentTo;
	}

	@Override
	public int compareTo(Message other) {
		return other.getTime().compareTo(this.getTime());
	}
}

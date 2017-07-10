package model;

import java.sql.Timestamp;

public class Challenge extends Message {
	private int quizID;
	private boolean challengeSeen;
	private boolean challengeAccepted;

	public Challenge(int senderID, int receiverID, int quizID, String msg, boolean challengeSeen, boolean challengeAccepted) {
		super(senderID, receiverID, msg);
		this.quizID = quizID;
		this.challengeSeen = challengeSeen;
		this.challengeAccepted = challengeAccepted;
	}
	
	public Challenge(int senderID, int receiverID, int quizID, String msg, Timestamp sentOn, boolean challengeSeen, boolean challengeAccepted) {
		super(senderID, receiverID, msg, sentOn);
		this.quizID = quizID;
		this.challengeSeen = challengeSeen;
		this.challengeAccepted = challengeAccepted;
	}

	public int getQuiz() {
		return quizID;
	}

	public boolean challengeSeen() {
		return challengeSeen;
	}

	public void setChallengeSeen(boolean challengeSeen) {
		this.challengeSeen = challengeSeen;
	}
	
	public boolean challengeAccepted() {
		return challengeAccepted;
	}
	
}

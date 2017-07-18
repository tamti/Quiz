package model;

import java.sql.Timestamp;

public class Challenge extends Message {
	private int challengeID;
	private int quizID;
	private boolean challengeSeen;
	private boolean challengeAccepted;

	public Challenge(int senderID, int receiverID, int quizID, String msg, boolean challengeSeen, boolean challengeAccepted) {
		super(senderID, receiverID, msg);
		this.challengeID = -1;
		this.quizID = quizID;
		this.challengeSeen = challengeSeen;
		this.challengeAccepted = challengeAccepted;
	}
	
	public Challenge(int challengeID, int senderID, int receiverID, int quizID, String msg, Timestamp sentOn, boolean challengeSeen, boolean challengeAccepted) {
		super(senderID, receiverID, msg, sentOn);
		this.challengeID = challengeID;
		this.quizID = quizID;
		this.challengeSeen = challengeSeen;
		this.challengeAccepted = challengeAccepted;
	}
	
	public int getChallengeID() {
		return challengeID;
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

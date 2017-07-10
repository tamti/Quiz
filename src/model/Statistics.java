package model;

import java.sql.Date;
import java.util.Calendar;

public class Statistics implements Comparable<Statistics> {
	private int quizID;
	private int userID;
	private Date takenOn;
	private int usedTimeInSeconds;
	private int numCorrectAnswers;
	private double numEarnedPoints;
	private boolean notYetFullyGraded;

	/**
	 * 
	 * @param quizID
	 *            ID of the quiz this data refers to
	 * @param userID
	 *            ID of the user this data refers to
	 * @param takenOn
	 *            Date on which user with ID "userID" took quiz with ID "quizID"
	 * @param usedTime
	 *            time (IN SECONDS) user with ID "userID" took to complete the
	 *            quiz with ID "quizID"
	 * @param numCorrectAnswers
	 *            number of questions user with ID "userID" answered correctly
	 *            in the quiz with ID "quizID"
	 * @param numEarnedPoints
	 *            points user with ID "userID" got in a quiz with ID "quizID"
	 * @param notYetFullyGraded
	 *            True if there are questions of type "graded" in the quiz with
	 *            ID "quizID" and this questions haven't yet been graded. False
	 *            otherwise
	 */
	public Statistics(int quizID, int userID, Date takenOn, int usedTimeInSeconds, int numCorrectAnswers,
			double numEarnedPoints, boolean notYetFullyGraded) {
		this.quizID = quizID;
		this.userID = userID;
		this.takenOn = takenOn;
		this.usedTimeInSeconds = usedTimeInSeconds;
		this.numCorrectAnswers = numCorrectAnswers;
		this.numEarnedPoints = numEarnedPoints;
		this.notYetFullyGraded = notYetFullyGraded;
	}

	/**
	 * 
	 * Constructs Statistics object with current date
	 * 
	 * @param quizID
	 *            ID of the quiz this data refers to
	 * @param userID
	 *            ID of the user this data refers to
	 * @param usedTime
	 *            time (IN SECONDS) user with ID "userID" took to complete the
	 *            quiz with ID "quizID"
	 * @param numCorrectAnswers
	 *            number of questions user with ID "userID" answered correctly
	 *            in the quiz with ID "quizID"
	 * @param numEarnedPoints
	 *            points user with ID "userID" got in a quiz with ID "quizID"
	 * @param notYetFullyGraded
	 *            True if there are questions of type "graded" in the quiz with
	 *            ID "quizID" and this questions haven't yet been graded. False
	 *            otherwise
	 */
	public Statistics(int quizID, int userID, int usedTimeInSeconds, int numCorrectAnswers, double numEarnedPoints,
			boolean notYetFullyGraded) {

		this(quizID, userID, new Date(Calendar.getInstance().getTimeInMillis()), usedTimeInSeconds, numCorrectAnswers,
				numEarnedPoints, notYetFullyGraded);
	}

	/**
	 * 
	 * @return userID ID of the user this data refers to
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * 
	 * @return ID of the quiz this data refers to
	 */
	public int getQuizID() {
		return quizID;
	}

	/**
	 * 
	 * @return time user used to complete the quiz (in seconds)
	 */
	public int getUsedTime() {
		return usedTimeInSeconds;
	}

	/**
	 * 
	 * @return Date on which user with ID "userID" took quiz with ID "quizID"
	 */
	public Date getDate() {
		return takenOn;
	}

	/**
	 * 
	 * @return time user took to complete the quiz
	 */
	public int getNumCorrectAnswers() {
		return numCorrectAnswers;
	}

	/**
	 * 
	 * @return points user got in a quiz
	 */
	public double getpoints() {
		return numEarnedPoints;
	}

	/**
	 * 
	 * @return True if there are questions of type "graded" in the quiz and this
	 *         questions haven't yet been graded for this user. False otherwise
	 */
	public boolean needsGrading() {
		return notYetFullyGraded;
	}

	/**
	 * 
	 * @param newScore
	 *            updates user's score for the quiz. Could be used if there are
	 *            questions of type "graded" in the quiz and author has graded
	 *            user's answers (or in other situations)
	 */
	public void updateScore(double newScore) {
		numEarnedPoints = newScore;
	}

	/**
	 * Compares Statistics objects by scores in the quiz and used time.
	 * 
	 * @param other
	 *            Statistics object to compare "this" to
	 * 
	 * @return zero, negative or positive number respectively if other equals,
	 *         is more or is less than "this"
	 */
	@Override
	public int compareTo(Statistics other) {
		int result = Double.compare(this.numEarnedPoints, other.getpoints());

		if (result == 0) {
			result = Integer.compare(this.usedTimeInSeconds, other.getUsedTime());
		}

		return result;
	}

}
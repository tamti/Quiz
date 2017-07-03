package model;

import java.sql.Date;
import java.sql.Time;
import java.util.SortedSet;
import java.util.TreeSet;

public class Quiz implements Comparable<Quiz> {
	private int ID;
	private String quizName;
	private String description;
	private boolean isOnePage;
	private boolean showAnswersImmediately;
	private Time allowedTime;
	private boolean random;
	private Date dateCreated;
	private int maxPoints;
	private SortedSet<Question> questions;
	private int ownerID;
	private SortedSet<Statistics> stats;

	public Quiz(int ID, int ownerID, String quizName, String description, Date dateCreated, boolean answersImmediately,
			boolean isOnePage, Time allowedTime, int maxPoints) {
		this.ID = ID;
		this.ownerID = ownerID;
		this.quizName = quizName;
		this.description = description;
		this.dateCreated = dateCreated;
		this.showAnswersImmediately = answersImmediately;
		this.isOnePage = isOnePage;
		this.allowedTime = allowedTime;
		this.maxPoints = maxPoints;
		questions = new TreeSet<Question>();
		stats = new TreeSet<Statistics>();
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public SortedSet<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(SortedSet<Question> questions) {
		this.questions = questions;
	}

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public boolean showAnswersImmediately() {
		return showAnswersImmediately;
	}

	public void setShowAnswersImmediately(boolean showAnswersImmediately) {
		this.showAnswersImmediately = showAnswersImmediately;
	}

	public Time getAllowedTime() {
		return allowedTime;
	}

	public void setAllowedTime(Time allowedTime) {
		this.allowedTime = allowedTime;
	}

	public boolean isOnePage() {
		return isOnePage;
	}

	public void setOnePage(boolean isOnePage) {
		this.isOnePage = isOnePage;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

	public Date getDate() {
		return dateCreated;
	}

	public void setDate(Date date) {
		this.dateCreated = date;
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}

	public SortedSet<Statistics> getStats() {
		return stats;
	}

	public void setStats(SortedSet<Statistics> stats) {
		this.stats = stats;
	}

	/**
	 * Compares Quiz objects. Comparison is made with their IDs
	 * 
	 * @param other
	 *            Quiz object to compare to
	 * @return the value 0 if this.ID == other.getID(); a value less than 0 if
	 *         this.ID < other.getID(); and a value greater than 0 if this.ID >
	 *         other.getID();
	 */
	@Override
	public int compareTo(Quiz other) {
		return Integer.compare(this.ID, other.getID());
	}

}

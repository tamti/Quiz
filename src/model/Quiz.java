package model;

import java.sql.Date;
import java.util.Calendar;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Quiz implements Comparable<Quiz> {
	private int ID;
	private String quizName;
	private String description;
	private boolean isOnePage;
	private boolean showAnswersImmediately;
	private int allowedTimeInMinutes;
	private boolean random;
	private Date dateCreated;
	private int maxPoints;
	private SortedMap<Integer, Question> questions;
	private int ownerID;
	private SortedSet<Statistics> stats;

	public Quiz(int ID, int ownerID, String quizName, String description, Date dateCreated, boolean answersImmediately,
			boolean isOnePage, int allowedTimeInMinutes, int maxPoints) {
		this.ID = ID;
		this.ownerID = ownerID;
		this.quizName = quizName;
		this.description = description;
		this.dateCreated = dateCreated;
		this.showAnswersImmediately = answersImmediately;
		this.isOnePage = isOnePage;
		this.allowedTimeInMinutes = allowedTimeInMinutes;
		this.maxPoints = maxPoints;

		questions = new TreeMap<Integer, Question>();
		stats = new TreeSet<Statistics>();
	}

	public Quiz(int ownerID, String quizName, String description, boolean answersImmediately, boolean isOnePage,
			int allowedTimeInMinutes, int maxPoints) {

		this(-1, ownerID, quizName, description, new Date(Calendar.getInstance().getTimeInMillis()), answersImmediately,
				isOnePage, allowedTimeInMinutes, maxPoints);
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public SortedMap<Integer, Question> getQuestions() {
		return questions;
	}

	public void setQuestions(SortedMap<Integer, Question> questions) {
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

	public int getAllowedTime() {
		return allowedTimeInMinutes;
	}

	public void setAllowedTime(int allowedTimeInMinutes) {
		this.allowedTimeInMinutes = allowedTimeInMinutes;
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

	public void addQuestion(Question newQuestion) {
		questions.put(newQuestion.getID(), newQuestion);
	}

	public void removeQuestion(Integer questionID) {
		questions.remove(questionID);
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

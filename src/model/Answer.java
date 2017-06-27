package model;

public class Answer implements Comparable<Answer> {
	private int ID;
	private String answerStr;
	private boolean isCorrect;
	private boolean hasNO;
	private int NO;

	/**
	 * Constructs an Answer object without specifying it's ID
	 * 
	 * @param answerStr
	 *            String representation of the answer
	 * @param isCorrect
	 *            boolean variable specifying whether this is a correct answer
	 *            to some question or not
	 */
	public Answer(String answerStr, boolean isCorrect) {
		this.answerStr = answerStr;
		this.isCorrect = isCorrect;
		hasNO = false;
		NO = -1;
	}

	/**
	 * Constructs an Answer object with specified ID
	 * 
	 * @param ID
	 *            ID of the answer in the database
	 * @param answerStr
	 *            String representation of the answer
	 * @param isCorrect
	 *            boolean variable specifying whether this is a correct answer
	 *            to some question or not
	 */
	public Answer(int ID, String answerStr, boolean isCorrect) {
		this(answerStr, isCorrect);
		this.ID = ID;
	}

	/**
	 * returns Answer's ID in the database
	 * 
	 * @return int value of the Answers's ID in the database
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Sets Answer's ID for the database and other uses
	 * 
	 * @param ID
	 *            int value of the Answers's ID in the database
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/**
	 * @return String representing the answer's text
	 */
	public String getAnswerStr() {
		return answerStr;
	}

	/**
	 * Whether this is a correct answer to some question or not
	 * 
	 * @return True if this is a correct answer to some question, False
	 *         otherwise.
	 */
	public boolean isCorrect() {
		return isCorrect;
	}

	/**
	 * Some answers to some questions need just to be ordered correctly. Such
	 * answers have their correct number
	 * 
	 * @return True if the answer has a number along with other answers to some
	 *         question, False otherwise
	 */
	public boolean hasNO() {
		return hasNO;
	}

	/**
	 * Some answers to some questions need just to be ordered correctly. Such
	 * answers have their correct number. This function sets that number to this
	 * question
	 */
	public void setNO(int NO) {
		this.NO = NO;
	}

	/**
	 * Some answers to some questions need just to be ordered correctly. Such
	 * answers have their correct number. This function sets that number to this
	 * question
	 * 
	 * @return If this answer is one of ordered answers, returns its number.
	 *         Returns -1 otherwise
	 */
	public int getNO() {
		return NO;
	}

	/**
	 * Compares Answer objects. Comparison is made with their IDs
	 * 
	 * @param other
	 *            Answer object to compare to
	 * @return the value 0 if this.ID == other.getID(); a value less than 0 if
	 *         this.ID < other.getID(); and a value greater than 0 if this.ID >
	 *         other.getID();
	 */
	@Override
	public int compareTo(Answer other) {
		return Integer.compare(this.ID, other.getID());
	}
}

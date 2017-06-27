package model;

import java.util.SortedSet;
import java.util.TreeSet;

import others.PhotoAble;
import others.QuestionType;

public class Question extends PhotoAble implements Comparable<Question> {
	private int ID;
	private QuestionType type;
	private String questionStr;
	private SortedSet<Answer> answers;

	/**
	 * 
	 * @param ID
	 *            ID of the question in the database
	 * @param questionStr
	 *            a string representing question's text
	 * @param type
	 *            Of which type is this question
	 * @param answers
	 *            set of answers (Answer objects) to this question
	 */
	public Question(int ID, String questionStr, QuestionType type, SortedSet<Answer> answers) {
		this.ID = ID;
		this.questionStr = questionStr;
		this.type = type;
		this.answers = answers;
	}

	/**
	 * Constructs Question object without initial set of answers (Answer
	 * objects)to it
	 * 
	 * @param ID
	 *            ID of the question in the database
	 * @param questionStr
	 *            a string representing question's text
	 * @param type
	 *            Of which type is this question
	 */
	public Question(int ID, String questionStr, QuestionType type) {
		this(ID, questionStr, type, new TreeSet<Answer>());
	}

	/**
	 * returns Qusetion's ID in the database
	 * 
	 * @return int value of the Questions's ID in the database
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @return String representing the Question's text
	 */
	public String getQuestionStr() {
		return questionStr;
	}

	/**
	 * Questions have different types. This function is to determine of which
	 * type is this Question
	 * 
	 * @return Enum value of the Type of this Question
	 */
	public QuestionType getType() {
		return type;
	}

	/**
	 * Questions have different types. This function is to assign a type to this
	 * Question
	 * 
	 * @param type
	 *            QuestionType Enum
	 */
	public void setType(QuestionType type) {
		this.type = type;
	}

	/**
	 * Adds a new answer to the set of answers to this Question
	 * 
	 * @param newAnswer
	 *            Answer - an object representing Answer to this question (with
	 *            all needed components)
	 */
	public void addAnswer(Answer newAnswer) {
		answers.add(newAnswer);
	}

	/**
	 * Removes an answer from the set of answers to this Question
	 * 
	 * @param newAnswer
	 *            Answer - an object representing Answer to this question (with
	 *            all needed components)
	 */
	public void removeAnswer(Answer a) {
		answers.remove(a);
	}

	/**
	 * 
	 * @return Sorted set of all answers (Answer objects) to this question
	 */
	public SortedSet<Answer> getAnswers() {
		return answers;
	}

	
	/**
	 * Compares Question objects. Comparison is made with their IDs
	 * 
	 * @param other
	 *            Question object to compare to
	 * @return the value 0 if this.ID == other.getID(); a value less than 0 if
	 *         this.ID < other.getID(); and a value greater than 0 if this.ID >
	 *         other.getID();
	 */
	@Override
	public int compareTo(Question other) {
		return Integer.compare(this.ID, other.getID());
	}
}

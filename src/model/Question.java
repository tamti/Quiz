package model;

import java.util.ArrayList;

import others.PhotoAble;
import others.QuestionType;

public class Question extends PhotoAble implements Comparable<Question> {
	private int ID;
	private QuestionType type;
	private String questionStr;
	private ArrayList<Answer> answers;
	private double maxPoints;

	/**
	 * 
	 * @param ID
	 *            ID of the question in the database
	 * @param questionStr
	 *            a string representing question's text
	 * @param type
	 *            Of which type is this question
	 * @param answers
	 * @param maxPoints
	 *            a real number - max number of points a user will get for
	 *            answering correctly
	 */
	public Question(int ID, String questionStr, QuestionType type, double maxPoints) {
		this.ID = ID;
		this.questionStr = questionStr;
		this.type = type;
		this.maxPoints = maxPoints;
		answers = new ArrayList<Answer>();
	}

	/**
	 * Constructs Question object without its ID
	 * 
	 * @param questionStr
	 *            a string representing question's text
	 * @param type
	 *            Of which type is this question
	 * @param maxPoints
	 *            a real number - max number of points a user will get for
	 *            answering correctly
	 */
	public Question(String questionStr, QuestionType type, double maxPoints) {
		this(-1, questionStr, type, maxPoints);
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
	 * 
	 * @param newAnswer
	 *            Answer - an object representing Answer to this question (with
	 *            all needed components)
	 */
	public void addAnswer(Answer newAnswer) {
		answers.add(newAnswer);
	}

	/**
	 * 
	 * @param answerID
	 */
	public void removeAnswer(Integer answerID) {
		answers.remove(answerID);
	}

	/**
	 * 
	 * @return SortedMap<answerID, Answer>
	 */
	public ArrayList<Answer> getAnswers() {
		return answers;
	}
	
	public ArrayList<String> getQuestionsCorrectAnswer(){
		ArrayList <String> result = new ArrayList<String>();
		for(int i =0; i< answers.size(); i++){
			if(answers.get(i).isCorrect()){
				result.add(answers.get(i).getAnswerStr());
			}
		}
		
		return result;
	}

	/**
	 * 
	 * @param answers
	 */
	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}

	/**
	 * 
	 * @return a real number - max number of points a user will get for
	 *         answering correctly
	 */
	public double getMaxPoints() {
		return maxPoints;
	}

	/**
	 * 
	 * @param maxPoints
	 *            a real number - max number of points a user will get for
	 *            answering correctly
	 */
	public void setMaxPoints(double maxPoints) {
		this.maxPoints = maxPoints;
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

	/**
	 * @param ID
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public void setQuestion(String newQuestionStr) {
		this.questionStr = newQuestionStr;
	}
}

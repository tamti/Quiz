package model;

import java.sql.Date;
import java.sql.Time;
import java.util.SortedMap;

import databaseManagement.QuizDAO;
import others.QuestionType;

public class QuizManager {
	private QuizDAO qDao;
	private SortedMap<String, Integer> quizInfo;

	/**
	 * Initializes all necessary objects for behind the scene operations
	 */
	public QuizManager() {
		qDao = new QuizDAO();
		quizInfo = qDao.getAllQuizInfo();
	}

	/**
	 * 
	 * Creates a new Quiz without any questions
	 * 
	 * @param ownerID
	 * @param quizName
	 * @param description
	 * @param dateCreated
	 * @param answersImmediately
	 * @param isOnePage
	 * @param allowedTime
	 * @param maxPoints
	 * @return newly created Quiz object
	 */
	public Quiz createQuiz(int ownerID, String quizName, String description, Date dateCreated,
			boolean answersImmediately, boolean isOnePage, Time allowedTime, int maxPoints) {
		Quiz newQuiz = new Quiz(ownerID, quizName, description, dateCreated, answersImmediately, isOnePage, allowedTime,
				maxPoints);

		int newQuizID = qDao.insertQuiz(newQuiz);
		newQuiz.setID(newQuizID);

		quizInfo.put(newQuiz.getQuizName(), newQuiz.getID());

		return newQuiz;
	}

	public Quiz getQuiz(String quizName) {
		Quiz result = null;

		if (quizInfo.containsKey(quizName)) {

			int quizID = quizInfo.get(quizName);

			result = qDao.getQuizByID(quizID);
		}

		return result;
	}

	/**
	 * 
	 * Creates a new Question without any answers and adds it to the specified
	 * quiz
	 * 
	 * @param quizName
	 * @param questionStr
	 * @param type
	 * @param maxPoints
	 * @return newly created Question object
	 */
	public Question addQuestionToQuiz(String quizName, String questionStr, QuestionType type, double maxPoints) {
		int quizID = quizInfo.get(quizName);

		Question newQuestion = new Question(questionStr, type, maxPoints);

		int newQuestionID = qDao.insertQuestion(quizID, newQuestion);

		newQuestion.setID(newQuestionID);

		return newQuestion;
	}

	/**
	 * adds an answer to specified question.
	 * 
	 * @param questionID
	 *            ID of the question we wish to add answer to
	 * @param answer
	 *            String representing one of answers to the specified question
	 * @param isCorrect
	 *            whether or not this is correct answer to the specified
	 *            question
	 * @param NO
	 *            integer less than 1 if answers to this question have no order.
	 *            Integer more than 0 if answers to this question have order
	 * @return Answer object constructed with given parameters
	 */
	public Answer addAnswerToQuestion(int questionID, String answer, boolean isCorrect, int NO) {
		Answer newAnswer = new Answer(answer, isCorrect);
		
		if (NO > 0)
			newAnswer.setNO(NO);
		
		int answerID = qDao.insertAnswer(questionID, newAnswer);
		
		newAnswer.setID(answerID);
		
		return newAnswer;
	}

	
	public String getUserAnswerToQuestion(int userID, int questionID) {
		return qDao.getUserAnswerToCheck(questionID, userID);
	}
	
	public void updateQuestion(Question question, String updatedQuestionStr) {
		question.setQuestion(updatedQuestionStr);
		qDao.updateQuestion(question.getID(), updatedQuestionStr, question.getMaxPoints());
	}
	
	public void updateQuestionMaxPoints(Question question, double newMaxPoints) {
		question.setMaxPoints(newMaxPoints);
		qDao.updateQuestion(question.getID(), question.getQuestionStr(), newMaxPoints);
	}
}

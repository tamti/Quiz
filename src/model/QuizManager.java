package model;

import java.util.List;
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
	 * @param allowedTimeInMinutes
	 * @param maxPoints
	 * @return newly created Quiz object
	 */
	public Quiz createQuiz(int ownerID, String quizName, String description, boolean answersImmediately,
			boolean isOnePage, int allowedTimeInMinutes) {
		
		Quiz newQuiz = new Quiz(ownerID, quizName, description, answersImmediately, isOnePage,
				allowedTimeInMinutes);

		int newQuizID = qDao.insertQuiz(newQuiz);
		newQuiz.setID(newQuizID);

		quizInfo.put(newQuiz.getQuizName(), newQuiz.getID());

		return newQuiz;
	}

	/**
	 * 
	 * @param quizName
	 * @return Quiz object corresponding to the given name
	 */
	public Quiz getQuiz(String quizName) {
		Quiz result = null;

		if (quizInfo.containsKey(quizName)) {

			int quizID = quizInfo.get(quizName);

			result = qDao.getQuizByID(quizID);
		}

		return result;
	}

	
	public void setQuiz(Quiz quiz) {
		int quizID = qDao.insertQuiz(quiz);
		quiz.setID(quizID);
		quizInfo.put(quiz.getQuizName(), quizID);
		List<Question> questions = quiz.getQuestions();
		for (int j = 0; j < questions.size(); j++) {
			Question q = questions.get(j);
			int questionID = qDao.insertQuestion(quizID, q);
			q.setID(questionID);
			
			List<Answer> answers = q.getAnswers();
			
			for (int i = 0; i < answers.size(); i++) {
				Answer a = answers.get(i);
				System.out.println("QUIZ MANAGER " + a.getAnswerStr());
				
				int answerID = qDao.insertAnswer(questionID, a);
				a.setID(answerID);
			}
		}
		
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
	/*public Answer addAnswerToQuestion(int questionID, String answer, boolean isCorrect, int NO) {
		Answer newAnswer = new Answer(answer, isCorrect);

		if (NO > 0)
			newAnswer.setNO(NO);

		int answerID = qDao.insertAnswer(questionID, newAnswer);

		newAnswer.setID(answerID);

		return newAnswer;
	}*/

	public String getUserAnswerToQuestion(int userID, int questionID) {
		return qDao.getUserAnswerToCheck(questionID, userID);
	}

	public void updateQuestionTxt(Question question, String updatedQuestionStr) {
		question.setQuestion(updatedQuestionStr);
		qDao.updateQuestion(question.getID(), updatedQuestionStr, question.getMaxPoints());
	}

	public void updateQuestionMaxPoints(Question question, double newMaxPoints) {
		question.setMaxPoints(newMaxPoints);
		qDao.updateQuestion(question.getID(), question.getQuestionStr(), newMaxPoints);
	}

	public void removeQuestion(String quizName, int questionID) {
		Quiz q = getQuiz(quizName);
		q.removeQuestion(questionID);
		qDao.removeQuestion(questionID);
	}

	public void removeAnswer(int answerID) {
		//Quiz quiz = getQuiz(quizName);
		//Question question = quiz.getQuestions().get(questionID);
		//question.removeAnswer(answerID);
		qDao.removeAnswer(answerID);
	}
	
	public void removeQuiz(int quizID){
		qDao.removeQuiz(quizID);
	}
}

package databaseManagement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import model.Answer;
import model.Question;
import model.Quiz;
import others.QuestionType;

public class QuizDAO extends BasicQuizWebSiteDAO {

	/**
	 * Selects from the database all the necessary parameters filtered with the
	 * given ID and returns a Quiz object constructed with those parameters.
	 * 
	 * @param ID
	 * @return Quiz object
	 */
	public Quiz getQuizByID(int ID) {
		Quiz result = null;

		String condition = DbContract.COL_QUIZ_ID + " = ?";

		String query = prepareSelectStatementWith("*", DbContract.TABLE_QUIZZES, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, ID);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					int quizID = rs.getInt(DbContract.COL_QUIZ_ID);
					int ownerID = rs.getInt(DbContract.COL_AUTHOR_ID);
					String quizName = rs.getString(DbContract.COL_QUIZ_NAME);
					String description = rs.getString(DbContract.COL_QUIZ_DESCRIPTION);
					Date dateCreated = rs.getDate(DbContract.COL_DATE_CREATED);
					boolean answersImmediately = rs.getBoolean(DbContract.COL_SHOW_ANSWERS_IMMEDIATELY);
					boolean isOnePage = rs.getBoolean(DbContract.COL_QUESTIONS_ON_SAME_PAGE);
					int allowedTime = rs.getInt(DbContract.COL_MAX_ALLOWED_TIME);

					result = new Quiz(quizID, ownerID, quizName, description, dateCreated, answersImmediately,
							isOnePage, allowedTime);

					ArrayList<Question> questions = getAllQuestionsFor(quizID);

					result.setQuestions(questions);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	public ArrayList<String> getQuizByDate() {
		ArrayList<String> result = new ArrayList<>();
		String col = "q." + DbContract.COL_QUIZ_NAME;
		String tables = DbContract.TABLE_QUIZZES + " q";
		String condition = "1=1 order by " + DbContract.COL_DATE_CREATED + " desc limit 10";

		String query = prepareSelectStatementWith(col, tables, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			try (ResultSet rs = ps.executeQuery()) {

				while(rs.next()) {
					String quizName = rs.getString(DbContract.COL_QUIZ_NAME);
					System.out.println("quizName " + quizName);
					result.add(quizName);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/*
	 * Selects all necessary info from the database and returns SortedSet of all
	 * Questions that belong to the specified quiz
	 */
	private ArrayList<Question> getAllQuestionsFor(int quizID) {
		ArrayList<Question> res = new ArrayList<Question>();

		String tables = DbContract.TABLE_QUESTIONS + " q, " + DbContract.TABLE_QUIZ_QUESTIONS + " qq, "
				+ DbContract.TABLE_QUESTION_TYPES + " qt";

		String condition = "qq." + DbContract.COL_QUIZ_ID + " = ? and qq." + DbContract.COL_QUESTION_ID + " = q."
				+ DbContract.COL_QUESTION_ID + " AND q." + DbContract.COL_QUESTION_TYPE_ID + " = qt."
				+ DbContract.COL_QUESTION_TYPE_ID;

		String query = prepareSelectStatementWith("*", tables, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, quizID);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					int questionID = rs.getInt(DbContract.COL_QUESTION_ID);
					String questionStr = rs.getString(DbContract.COL_QUESTION);

					String qTypeName = rs.getString(DbContract.COL_QUESTION_TYPE);
					QuestionType qType = QuestionType.valueOf(qTypeName);

					double maxPoints = rs.getDouble(DbContract.COL_MAX_POINTS);
					int photoID = rs.getInt(DbContract.COL_PHOTO_ID);

					Question current = new Question(questionID, questionStr, qType, maxPoints);

					if (photoID > 0) {
						// String photoFileName =
						// rs.getString(DbContract.COL_PHOTO_FILE);
						current.setPhotoID(photoID);
					}

					ArrayList<Answer> answers = getAllAnswersFor(questionID);

					current.setAnswers(answers);

					res.add(current);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return res;
	}

	/*
	 * Selects all necessary info from the database and returns SortedSet of all
	 * Answers that belong to the specified question
	 */
	private ArrayList<Answer> getAllAnswersFor(int questionID) {
		ArrayList<Answer> res = new ArrayList<Answer>();

		String tables = DbContract.TABLE_ANSWERS + " a, " + DbContract.TABLE_ANSWERS_TO_QUESTIONS + " aq";
		String condition = "aq." + DbContract.COL_QUESTION_ID + " = ? and aq." + DbContract.COL_ANSWER_ID + " = a."
				+ DbContract.COL_ANSWER_ID;

		String query = prepareSelectStatementWith("*", tables, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, questionID);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					int answerID = rs.getInt(DbContract.COL_ANSWER_ID);
					String answerStr = rs.getString(DbContract.COL_ANSWER);
					boolean isCorrect = rs.getBoolean(DbContract.COL_ANSWER_IS_CORRECT);

					Answer current = new Answer(answerID, answerStr, isCorrect);

					int answerNO = rs.getInt(DbContract.COL_ANSWER_NO);

					if (answerNO > 0) {
						current.setNO(answerNO);
					}

					res.add(current);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return res;
	}

	/**
	 * Returns user's answer to the question of the type "graded" that needs to
	 * be checked by the author of the quiz
	 * 
	 * @param questionID
	 * @param userID
	 * @return String representig user's answer to the specified question
	 */
	public String getUserAnswerToCheck(int questionID, int userID) {
		String result = "";

		String table = DbContract.TABLE_USER_ANSWERS;
		String condition = DbContract.COL_QUESTION_ID + " = ? and " + DbContract.COL_USER_ID + " = ?";

		String query = prepareSelectStatementWith(DbContract.COL_USER_ANSWER, table, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, questionID);
			ps.setInt(2, userID);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					result = rs.getString(DbContract.COL_USER_ANSWER);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * @return SortedMap containing names of quizzes as keys and ID's as values
	 */
	public SortedMap<String, Integer> getAllQuizInfo() {
		SortedMap<String, Integer> result = new TreeMap<String, Integer>();

		String columns = DbContract.COL_QUIZ_ID + ", " + DbContract.COL_QUIZ_NAME;

		String query = prepareSelectStatementWith(columns, DbContract.TABLE_QUIZZES, "");

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int ID = rs.getInt(DbContract.COL_QUIZ_ID);
				String name = rs.getString(DbContract.COL_QUIZ_NAME);

				result.put(name, ID);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Inserts all properties of the given Quiz to the database
	 * 
	 * @param Quiz
	 * @return ID of the new Quiz in the database
	 */
	public int insertQuiz(Quiz newQuiz) {
		String[] cols = { DbContract.COL_QUIZ_NAME, DbContract.COL_QUIZ_DESCRIPTION, DbContract.COL_AUTHOR_ID,
				DbContract.COL_DATE_CREATED, DbContract.COL_SHOW_ANSWERS_IMMEDIATELY,
				DbContract.COL_QUESTIONS_ON_SAME_PAGE, DbContract.COL_MAX_ALLOWED_TIME };

		String query = prepareInsertStatementWith(DbContract.TABLE_QUIZZES, cols);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, newQuiz.getQuizName());
			ps.setString(2, newQuiz.getDescription());
			ps.setInt(3, newQuiz.getOwnerID());
			ps.setDate(4, newQuiz.getDate());
			ps.setBoolean(5, newQuiz.showAnswersImmediately());
			ps.setBoolean(6, newQuiz.isOnePage());
			ps.setInt(7, newQuiz.getAllowedTime());

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return getLastIdOf(DbContract.TABLE_QUIZZES, DbContract.COL_QUIZ_ID);
	}

	/**
	 * 
	 * Inserts all properties of the given Question to the database
	 * 
	 * @param quizID
	 * @param newQuestion
	 * @return ID of the new Question in the database
	 */
	public int insertQuestion(int quizID, Question newQuestion) {
		String[] questionCols = { DbContract.COL_QUESTION, DbContract.COL_QUESTION_TYPE_ID, DbContract.COL_PHOTO_ID,
				DbContract.COL_MAX_POINTS };

		String query1 = prepareInsertStatementWith(DbContract.TABLE_QUESTIONS, questionCols);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query1)) {

			ps.setString(1, newQuestion.getQuestionStr());
			ps.setInt(2, newQuestion.getType().getID());

			if (newQuestion.hasPhoto()) {
				ps.setInt(3, newQuestion.getPhotoID());
			} else {
				ps.setNull(3, Types.INTEGER);
			}

			ps.setDouble(4, newQuestion.getMaxPoints());

			ps.executeUpdate();

			System.out.println("INSERT QUESTION " + newQuestion.getQuestionStr());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		String[] quizQuestionsCols = { DbContract.COL_QUIZ_ID, DbContract.COL_QUESTION_ID };

		String query2 = prepareInsertStatementWith(DbContract.TABLE_QUIZ_QUESTIONS, quizQuestionsCols);

		int IdOfNewQuestion = getLastIdOf(DbContract.TABLE_QUESTIONS, DbContract.COL_QUESTION_ID);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query2)) {

			ps.setInt(1, quizID);
			ps.setInt(2, IdOfNewQuestion);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return IdOfNewQuestion;
	}

	/**
	 * 
	 * Inserts all properties of the given Answer to the database
	 * 
	 * @param questionID
	 * @param newAnswer
	 * @return ID of the new Answer in the database
	 */
	public int insertAnswer(int questionID, Answer newAnswer) {
		String[] answerCols = { DbContract.COL_ANSWER, DbContract.COL_ANSWER_IS_CORRECT, DbContract.COL_ANSWER_NO };

		String query1 = prepareInsertStatementWith(DbContract.TABLE_ANSWERS, answerCols);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query1)) {

			ps.setString(1, newAnswer.getAnswerStr());
			ps.setBoolean(2, newAnswer.isCorrect());

			if (newAnswer.hasNO()) {
				ps.setInt(3, newAnswer.getNO());
			} else {
				ps.setNull(3, Types.INTEGER);
			}

			ps.executeUpdate();

			System.out.println("INSERT ANSWER " + newAnswer.getAnswerStr());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		String[] questionAnswersCols = { DbContract.COL_QUESTION_ID, DbContract.COL_ANSWER_ID };

		String query2 = prepareInsertStatementWith(DbContract.TABLE_ANSWERS_TO_QUESTIONS, questionAnswersCols);

		int idOfNewAnswer = getLastIdOf(DbContract.TABLE_ANSWERS, DbContract.COL_ANSWER_ID);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query2)) {

			ps.setInt(1, questionID);
			ps.setInt(2, idOfNewAnswer);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return idOfNewAnswer;
	}

	/**
	 * 
	 * Updates question's text and/or its max points
	 * 
	 * @param questionID
	 * @param updatedQuestionStr
	 * @param updatedMaxPoints
	 */
	public void updateQuestion(int questionID, String updatedQuestionStr, double updatedMaxPoints) {
		String setCols = DbContract.COL_QUESTION + " = ?, " + DbContract.COL_MAX_POINTS + " = ?";
		String condition = DbContract.COL_QUESTION_ID + " = ?";

		String query = prepareUpdateStatementWith(DbContract.TABLE_QUESTIONS, setCols, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, updatedQuestionStr);
			ps.setDouble(2, updatedMaxPoints);
			ps.setInt(3, questionID);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates answer's text and/or its NO
	 * 
	 * @param answerID
	 * @param updatedAnswer
	 * @param newNO
	 */
	public void updateAnswer(int answerID, String updatedAnswer, int newNO) {
		String setCols = DbContract.COL_ANSWER + " = ?, " + DbContract.COL_ANSWER_NO + " = ?";
		String condition = DbContract.COL_ANSWER_ID + " = ?";

		String query = prepareUpdateStatementWith(DbContract.TABLE_QUESTIONS, setCols, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, updatedAnswer);
			ps.setInt(2, newNO);
			ps.setInt(3, answerID);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes specified question from the database
	 * 
	 * @param questionID
	 */
	public void removeQuestion(int questionID) {
		removeFromTableWithID(DbContract.TABLE_QUESTIONS, DbContract.COL_QUESTION_ID, questionID);
	}

	/**
	 * 
	 * @param answerID
	 */
	public void removeAnswer(int answerID) {
		removeFromTableWithID(DbContract.TABLE_ANSWERS, DbContract.COL_ANSWER_ID, answerID);
	}

	/*
	 * removes data from the specified table where some specified (ID type)
	 * column's value is "ID"
	 */
	private void removeFromTableWithID(String table, String column, int ID) {
		String condition = column + " = ?";

		String query = prepareDeleteStatementWith(table, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, ID);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeQuiz(int quiz_id) {
		String condition = DbContract.COL_QUIZ_ID + " = ?";
		String query = prepareDeleteStatementWith(DbContract.TABLE_QUIZZES, condition);

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, quiz_id);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
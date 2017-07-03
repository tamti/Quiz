package databaseManagement;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.SortedSet;
import java.util.TreeSet;

import model.Statistics;

public class StatisticsDAO extends BasicQuizWebSiteDAO {

	/**
	 * 
	 * @param userID
	 *            ID of the user whose statistics we are interested in
	 * @return SortedSet of all Statistics of user with ID "userID"
	 */
	public SortedSet<Statistics> getStatisticsByUser(int userID) {
		String[] cols = { DbContract.COL_USER_ID };
		int[] IDs = { userID };

		return getStatsByIdOf(cols, IDs);
	}

	/**
	 * 
	 * @param quizID
	 *            ID of the quiz statistics of which we are interested in
	 * @return SortedSet of all Statistics of quiz with ID "quizID"
	 */
	public SortedSet<Statistics> getStatisticsByQuiz(int quizID) {
		String[] cols = { DbContract.COL_QUIZ_ID };
		int[] IDs = { quizID };

		return getStatsByIdOf(cols, IDs);
	}

	/**
	 * Returns whole information about user's statistics in some specified quiz
	 * 
	 * @param userID
	 *            ID of the user whose statistics we are interested in
	 * @param quizID
	 *            ID of the quiz statistics of which we are interested in
	 * @return SortedSet of all Statistics of user with ID "userID" from the
	 *         quiz with ID "quizID"
	 */
	public SortedSet<Statistics> getStatisticsOfUserForQuiz(int userID, int quizID) {
		String[] cols = { DbContract.COL_USER_ID, DbContract.COL_QUIZ_ID };
		int[] IDs = { userID, quizID };

		return getStatsByIdOf(cols, IDs);
	}

	/*
	 * This is a helper method for public methods of type "getStatistics...".
	 * Returns SortedSet of Statistics objects taken from the database, where
	 * for each idCols[i] = IDs[i] is satisfied. Parameter "idCols" is an array
	 * containing names of some ID columns and parameter "IDs" is an array
	 * containing desired values of the columns from "idCols"
	 */
	private SortedSet<Statistics> getStatsByIdOf(String[] idCols, int[] IDs) {
		String table = DbContract.TABLE_QUIZ_STATS + "s";

		String condition = "1 = 1";

		for (int i = 0; i < idCols.length; i++) {
			condition = condition + " AND s." + idCols[i] + " = ?";
		}

		PreparedStatement ps = prepareSelectStatementWith("*", table, condition);

		for (int i = 0; i < idCols.length; i++) {
			try {
				ps.setInt(i + 1, IDs[i]);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			ResultSet rs = ps.executeQuery();

			return getAllStatsFromResultSet(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new TreeSet<Statistics>();
	}

	/*
	 * Returns a SortedSet of Statistic objects containing all statistical info
	 * taken from the give ResultSet
	 */
	private SortedSet<Statistics> getAllStatsFromResultSet(ResultSet rs) {
		SortedSet<Statistics> result = new TreeSet<Statistics>();

		try {
			while (rs.next()) {
				Statistics curr = getStatFromResultSet(rs);
				result.add(curr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/*
	 * Returns a Statistic objects containing info taken from the give ResultSet
	 */
	private Statistics getStatFromResultSet(ResultSet rs) {
		Statistics result = null;

		try {
			int quizID = rs.getInt(DbContract.COL_QUIZ_ID);
			int userID = rs.getInt(DbContract.COL_USER_ID);
			Date takenOn = rs.getDate(DbContract.COL_TAKE_ON);
			Time usedTime = rs.getTime(DbContract.COL_USED_TIME);
			int numCorrectAnswers = rs.getInt(DbContract.COL_NUM_CORRECT_ANSWERS);
			double numEarnedPoints = rs.getDouble(DbContract.COL_NUM_RECIEVED_POINTS);
			boolean notYetFullyGraded = rs.getBoolean(DbContract.COL_HAS_ANSWERS_TO_CHECK);

			result = new Statistics(quizID, userID, takenOn, usedTime, numCorrectAnswers, numEarnedPoints,
					notYetFullyGraded);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * inserts all information from Statistics object into the database
	 * 
	 * @param stat
	 *            Statistics object containing all necessary statistical
	 *            information
	 */
	public void insertStatistics(Statistics stat) {
		String table = DbContract.TABLE_QUIZ_STATS;
		String cols = DbContract.COL_QUIZ_ID + ", " + DbContract.COL_USER_ID + ", " + DbContract.COL_TAKE_ON + ", "
				+ DbContract.COL_USED_TIME + ", " + DbContract.COL_NUM_CORRECT_ANSWERS + ", "
				+ DbContract.COL_NUM_RECIEVED_POINTS + ", " + DbContract.COL_HAS_ANSWERS_TO_CHECK;

		PreparedStatement ps = getPreparedStatementForInsertionWith(table, cols, 7);

		try {
			ps.setInt(1, stat.getQuizID());
			ps.setInt(2, stat.getUserID());
			ps.setDate(3, stat.getDate());
			ps.setTime(4, stat.getUsedTime());
			ps.setInt(5, stat.getNumCorrectAnswers());
			ps.setDouble(6, stat.getpoints());
			ps.setBoolean(7, stat.needsGrading());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * updates score of the specified user in the statistics contained in the
	 * database. (This method could be used to completely change user's score,
	 * nonetheless it is mainly meant to update user's score if the specified
	 * quiz had questions of type "graded" and author has graded user's answers)
	 * 
	 * @param quizID
	 *            ID of quiz in which we are going to update score of the
	 *            specified user
	 * @param userID
	 *            ID of the user whose score in specified quiz we are going to
	 *            update
	 * @param newScore
	 *            real number representing new score we wish to give to the
	 *            specified user in the specified quiz
	 * @param stillNeedsChecking
	 *            False if there are no more questions of type "graded" which
	 *            still need to be checked by the author of the quiz. True
	 *            otherwise
	 */
	public void updatePoints(int quizID, int userID, double newScore, boolean stillNeedsChecking) {
		String setCols = DbContract.COL_NUM_RECIEVED_POINTS + " = ? " + DbContract.COL_HAS_ANSWERS_TO_CHECK + " = ?";
		String condition = DbContract.COL_QUIZ_ID + " = ? AND " + DbContract.COL_USER_ID + " = ?";

		PreparedStatement ps = prepareUpdateStatementWith(DbContract.TABLE_QUIZ_STATS, setCols, condition);

		try {
			ps.setDouble(1, newScore);
			ps.setBoolean(2, stillNeedsChecking);
			ps.setInt(3, quizID);
			ps.setInt(4, userID);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
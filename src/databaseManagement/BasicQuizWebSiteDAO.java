package databaseManagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BasicQuizWebSiteDAO {

	/*
	 * Argument "query" is a String representing MySQL "Select" query. The
	 * function Returns a ResultSet returned by the
	 * statement.executeQuery(query).
	 */
	protected ResultSet getResultSetWithQuery(String query) {
		ResultSet res = null;

		try {
			Connection connection = DataSource.getDataSource().getConnection();
			Statement statement = connection.createStatement();
			res = statement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	protected void updateWithQuery(String tableName, String setCol, String where) {
		try {
			Connection connection = DataSource.getDataSource().getConnection();
			Statement statement = connection.createStatement();
			statement.executeQuery("update " + tableName + " set " + setCol + " " + where + ";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

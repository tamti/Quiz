package databaseManagement;

import java.sql.*;

public class BasicQuizWebSiteDAO {

	/**
	 * 
	 * @param query
	 * @return PreparedStatement prepared with given [query]
	 */
	protected PreparedStatement getPreparedStatementWith(String query) {
		try {
			Connection connection = DataSource.getDataSource().getConnection();

			return connection.prepareStatement(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns a PreparedStatement with select query constructed with the given
	 * parameters. "condition" is optional, "where" condition will be omitted,
	 * if "condition" is set to empty string.
	 * 
	 * Client should set all parameters (if any) for the execution of the query
	 * by himself and than use executeQuery() or executeUpdate()
	 * 
	 * @param columns
	 *            String representing all columns to be selected from the table
	 *            (separated by commas)
	 * @param tables
	 *            String representing all tables selection is made from
	 * @param condition
	 *            String representing condition of selection given columns from
	 *            the given table
	 * 
	 * @Return a PreparedStatement returned by the query: "select [columns] from
	 *         [table] where [condition];"
	 */
	protected PreparedStatement prepareSelectStatementWith(String columns, String tables, String condition) {
		String query = "select " + columns + " from " + tables;

		if (!condition.isEmpty())
			query = query + " where " + condition;

		query += ";";

		return getPreparedStatementWith(query);
	}

	/**
	 * Returns a PreparedStatement with update query constructed with the given
	 * parameters. "condition" is optional, "where" condition will be omitted,
	 * if "condition" is set to empty string.
	 * 
	 * Client should set all parameters (if any) for the execution of the query
	 * by himself and than use executeQuery() or executeUpdate()
	 * 
	 * @param table
	 *            table to be updated
	 * @param setCols
	 *            String representing all "set" instructions on the desired
	 *            columns in the table
	 * @param condition
	 *            String representing condition of update
	 * @return a PreparedStatement for update made with the query: "update
	 *         [table] set [setCols] (where [condition])"
	 */
	protected PreparedStatement prepareUpdateStatementWith(String table, String setCols, String condition) {
		String query = "update " + table + " set " + setCols;

		if (!condition.isEmpty())
			query = query + " where " + condition;

		query += ";";

		return getPreparedStatementWith(query);
	}

	/**
	 * Returns PreparedStatement with the insert query constructed with given
	 * parameters.
	 * 
	 * Client should set all parameters (if any) for the execution of the query
	 * by himself and than use executeQuery()
	 * 
	 * @param table
	 *            a table into which we wish to insert some values
	 * @param cols
	 *            array of string representing all columns into which we wish to
	 *            insert some values
	 * 
	 * @Return a PreparedStatement for insertion into database with the query:
	 *         "insert into [table] ( [cols[i],..] ) values ( [?,..] )"
	 */
	protected PreparedStatement prepareInsertStatementWith(String table, String[] cols) {
		String query = "insert into " + table + " (" + String.join(",", cols) + ") values (?";

		for (int i = 0; i < cols.length - 1; i++) {
			query += ", ?";
		}

		query += ");";

		return getPreparedStatementWith(query);
	}

	/**
	 * Returns PreparedStatement with the delete query constructed with given
	 * parameters.
	 * 
	 * Parameter "condition" cannot be omitted!
	 * 
	 * Client should set all parameters (if any) for the execution of the query
	 * by himself and than use executeQuery()
	 * 
	 * @param table
	 *            a table from which we wish to delete some data
	 * @param condition
	 *            a condition with which deletion will be made
	 * 
	 * @Return a PreparedStatement for deletion from table with the query:
	 *         "delete from [table] where [condition]
	 */
	protected PreparedStatement prepareDeleteStatementWith(String table, String condition) {
		String query = "delete from " + table + " where " + condition + ";";

		return getPreparedStatementWith(query);
	}

	/**
	 * 
	 * Should be used to find primary key (ID) of last insert into specified
	 * table
	 * 
	 * @param table
	 * @param idColumnName
	 *            name of the primary key (ID) column
	 * @return "select Max([idColumnName]) from [table]"
	 */
	protected int getLastIdOf(String table, String idColumnName) {
		String col = "max(" + idColumnName + ") as lastID";
		PreparedStatement ps = prepareSelectStatementWith(col, table, "");

		int result = 0;

		try {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt("lastID");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

}

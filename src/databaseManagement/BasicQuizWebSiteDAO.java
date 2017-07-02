package databaseManagement;

import java.sql.*;

public class BasicQuizWebSiteDAO {

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
		PreparedStatement res = null;

		String query = "select " + columns + " from " + tables;

		if (!condition.isEmpty())
			query = query + " where " + condition;

		query += ";";

		try {
			Connection connection = DataSource.getDataSource().getConnection();
			res = connection.prepareStatement(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
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
	 *         [table] set [setCols] (where [condition])
	 */
	protected PreparedStatement prepareUpdateStatementWith(String table, String setCols, String condition) {
		PreparedStatement res = null;

		String query = "update " + table + " set " + setCols;

		if (!condition.isEmpty())
			query = query + " where " + condition;

		query += ";";

		try {
			Connection connection = DataSource.getDataSource().getConnection();
			res = connection.prepareStatement(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Returns PreparedStatement with the insert query constructed with given
	 * parameters.
	 * 
	 * Client should set all parameters (if any) for the execution of the query
	 * by himself and than use executeQuery()
	 * 
	 * @param cols
	 *            string representing all columns into which we wish to insert
	 *            some values (separated by commas)
	 * @param table
	 *            a table into which we wish to insert some values
	 * @param numValues
	 *            number of columns we wish to insert into (used for determining
	 *            number of needed question marks)
	 * 
	 * @Return a PreparedStatement for insertion into database with the query:
	 *         "insert into [table] (cols) values (?*numValues)
	 */
	protected PreparedStatement getPreparedStatementForInsertionWith(String table, String cols, int numValues) {
		PreparedStatement res = null;

		String query = "insert into " + table + " (" + cols + ") values (?";

		for (int i = 0; i < numValues - 1; i++) {
			query += ", ?";
		}

		query += ");";

		try {
			Connection connection = DataSource.getDataSource().getConnection();
			res = connection.prepareStatement(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
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
		PreparedStatement res = null;

		String query = "delete from " + table + " where " + condition + ";";

		try {
			Connection connection = DataSource.getDataSource().getConnection();
			res = connection.prepareStatement(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

}

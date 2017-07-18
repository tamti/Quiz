package databaseManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicQuizWebSiteDAO {

	/**
	 * Returns a select query constructed with the given parameters. "condition"
	 * is optional, "where" condition will be omitted, if "condition" is set to
	 * empty string.
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
	 * @Return String representing select query: "select [columns] from [table]
	 *         where [condition];"
	 */
	protected String prepareSelectStatementWith(String columns, String tables, String condition) {
		String query = "select " + columns + " from " + tables;

		if (!condition.isEmpty())
			query = query + " where " + condition;

		query += ";";

		return query;
	}

	/**
	 * Returns a update statement query constructed with the given parameters.
	 * "condition" is optional, "where" condition will be omitted, if
	 * "condition" is set to empty string.
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
	 * @return String representing update query: "update [table] set [setCols]
	 *         (where [condition])"
	 */
	protected String prepareUpdateStatementWith(String table, String setCols, String condition) {
		String query = "update " + table + " set " + setCols;

		if (!condition.isEmpty())
			query = query + " where " + condition;

		query += ";";

		return query;
	}

	/**
	 * Returns an insert query constructed with given parameters.
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
	 * @Return String representing insertion query: "insert into [table] (
	 *         [cols[i],..] ) values ( [?,..] )"
	 */
	protected String prepareInsertStatementWith(String table, String[] cols) {
		String query = "insert into " + table + " (" + String.join(",", cols) + ") values (?";

		for (int i = 0; i < cols.length - 1; i++) {
			query += ", ?";
		}

		query += ");";

		return query;
	}

	/**
	 * Returns deletion query constructed with given parameters.
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
	 * @Return String representing deletion query: "delete from [table] where
	 *         [condition]
	 */
	protected String prepareDeleteStatementWith(String table, String condition) {
		return "delete from " + table + " where " + condition + ";";
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
		int result = 0;

		String col = "max(" + idColumnName + ") as lastID";

		String query = prepareSelectStatementWith(col, table, "");

		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			
			if (rs.next()) {
				result = rs.getInt("lastID");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	protected int insertPhoto(String photoURL) {
		String[] col = { DbContract.COL_PHOTO_FILE };
		String query = prepareInsertStatementWith(DbContract.TABLE_PHOTOS, col);
				
		try (Connection con = DataSource.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, photoURL);
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return getLastIdOf(DbContract.TABLE_PHOTOS, DbContract.COL_PHOTO_ID);
	}
}

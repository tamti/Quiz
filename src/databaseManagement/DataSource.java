package databaseManagement;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * DataSource class is for connection pooling
 * 
 * @author Vazha
 *
 */
public class DataSource {
	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	private static final int MIN_IDLE = 3;
	private static final int MAX_IDLE = 15;
	private static final int MAX_OPEN_PREP_ST = 100;

	private static DataSource dataSource;
	private BasicDataSource ds;

	private DataSource() throws IOException, SQLException, PropertyVetoException {
		ds = new BasicDataSource();
		ds.setDriverClassName(DRIVER_CLASS_NAME);
		ds.setUrl(MyDbInfo.MYSQL_DATABASE_SERVER + MyDbInfo.MYSQL_DATABASE_NAME);
		ds.setUsername(MyDbInfo.MYSQL_USERNAME);
		ds.setPassword(MyDbInfo.MYSQL_PASSWORD);
		ds.setMinIdle(MIN_IDLE);
		ds.setMaxIdle(MAX_IDLE);
		ds.setMaxOpenPreparedStatements(MAX_OPEN_PREP_ST);
	}

	/**
	 * 
	 * @return DataSource object
	 */
	public static DataSource getDataSource() {
		if (dataSource == null) {
			try {
				dataSource = new DataSource();
			} catch (IOException | SQLException | PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		return dataSource;
	}

	/**
	 * Returns one of available connections
	 * to the database
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}

}
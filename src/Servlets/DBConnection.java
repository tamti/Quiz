package Servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	 static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/quiz?useUnicode=true&characterEncoding=utf8";
	   static final String USER = "root";
	   static final String PASS = "anastasia";
	
	public static Connection initConnection() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Connection thisCon = null;
		try {
			 
		    Class.forName("com.mysql.jdbc.Driver").newInstance();

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      thisCon = DriverManager.getConnection(DB_URL,USER,PASS);
			//thisCon =   DriverManager.getConnection("jdbc:mysql://localhost:8080/01quiz?" + "user=root&password=temo39814500");
			 		
			 
		} catch (SQLException e) { 
			e.printStackTrace();
		}  
		return thisCon;
	}
	
	public static void main(String[] args) throws SQLException{
		
		 
	}
}

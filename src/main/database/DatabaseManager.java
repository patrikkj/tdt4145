package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.app.MainApp;

public class DatabaseManager {
	private static final String DB_DRIVER_PATH = "com.mysql.cj.jdbc.Driver";
	private static final String DB_NAME = "database_name";
	private static final String DB_USERNAME = "username";
	private static final String DB_PASSWORD = "password";
	private static final String CONNECTION_STRING = "jdbc:mysql://mysql.stud.ntnu.no/" + DB_NAME + "?serverTimezone=UTC";
	
	private static Connection connection;
	
	
	/*
	 * Setting up a connection to the database
	 */
	static {
		openConnection();
		MainApp.addShutdownHook(DatabaseManager::closeConnection);
	}
	
	
	/**
	 * Returns a new {@link PreparedStatement} representing the {@code statement} specified.
	 */
	protected static PreparedStatement getPreparedStatement(String statement) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
	}
	
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	protected static ResultSet executeQuery(PreparedStatement preparedStatement) {
		ResultSet resultSet = null;
		
		try {
			resultSet = executeTimedQuery(preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		};
		
		return resultSet;
	}

	/*
	 * Sends query and reestablishes connection if lost.
	 */
	protected static ResultSet executeQuery(String query) {
		ResultSet resultSet = null;
		
		try {
			resultSet = executeTimedQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		};
		
		return resultSet;
	}
	
	/*
	 * Sends update and reestablishes connection if lost.
	 */
	protected static int executeUpdate(PreparedStatement preparedStatement) {
		int linesChanged = 0;
		
		try {
			linesChanged = executeTimedUpdate(preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		};
		
		return linesChanged;
	}
	
	/*
	 * Sends update and reestablishes connection if lost.
	 */
	protected static int executeUpdate(String update) {
		int linesChanged = 0;
		
		try {
			linesChanged = executeTimedUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		};
		
		return linesChanged;
	}
	

	/**
	 * Executes query specified by {@code preparedStatement} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static ResultSet executeTimedQuery(PreparedStatement preparedStatement) throws SQLException{
		ResultSet resultSet = null;
		
		try {
			long time1 = System.nanoTime();
			resultSet = preparedStatement.executeQuery();
			long time2 = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (time2 - time1) / Math.pow(10, 9), preparedStatement);
		} catch (SQLException e) {
			openConnection();
			
			long time1 = System.nanoTime();
			resultSet = preparedStatement.executeQuery();
			long time2 = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (time2 - time1) / Math.pow(10, 9), preparedStatement);
		}
		
		return resultSet;
	}

	/**
	 * Executes query specified by {@code query} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static ResultSet executeTimedQuery(String query) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = null;

		try {
			long time1 = System.nanoTime();
			resultSet = statement.executeQuery(query);
			long time2 = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (time2 - time1) / Math.pow(10, 9), query);
		} catch (SQLException e) {
			openConnection();
			
			long time1 = System.nanoTime();
			resultSet = statement.executeQuery(query);
			long time2 = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (time2 - time1) / Math.pow(10, 9), query);
		}
		
		return resultSet;
	}
	
	/**
	 * Executes update specified by {@code preparedStatement} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedUpdate(PreparedStatement preparedStatement) throws SQLException {
		int linesChanged = 0;
		
		try {
			long time1 = System.nanoTime();
			linesChanged = preparedStatement.executeUpdate();
			long time2 = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (time2 - time1) / Math.pow(10, 9), preparedStatement);
		} catch (SQLException e) {
			openConnection();
			
			long time1 = System.nanoTime();
			linesChanged = preparedStatement.executeUpdate();
			long time2 = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (time2 - time1) / Math.pow(10, 9), preparedStatement);
		}
		
		return linesChanged;
	}	
	
	/**
	 * Executes update specified by {@code query} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedUpdate(String update) throws SQLException {
		Statement statement = connection.createStatement();
		int linesChanged = 0;

		try {
			long time1 = System.nanoTime();
			linesChanged = statement.executeUpdate(update);
			long time2 = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (time2 - time1) / Math.pow(10, 9), update);
		} catch (SQLException e) {
			openConnection();
			
			long time1 = System.nanoTime();
			linesChanged = statement.executeUpdate(update);
			long time2 = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (time2 - time1) / Math.pow(10, 9), update);
		}
		
		return linesChanged;
	}
	
	
	/*
	 * Opens SQL connection.
	 * Runs at launch, and if the connection times out etc. 
	 */
	private static void openConnection() {
		try {
			Class.forName(DB_DRIVER_PATH);
			connection = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Closes SQL connection.
	 * Runs from shutdown hook. 
	 */
	private static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}

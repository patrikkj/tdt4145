package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.util.Pair;
import main.app.MainApp;

public class DatabaseManager {
	private static final String DB_DRIVER_PATH = "com.mysql.cj.jdbc.Driver";
	private static final String DB_NAME = "patrikkj_iexercise_db";
	private static final String DB_USERNAME = "patrikkj_iexercise";
	private static final String DB_PASSWORD = "iexercise";
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
	
	
	// Queries
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	@SafeVarargs
	protected static <T> ResultSet executeQuery(String preparedStatement, Pair<Class<T>, T>... pairs) {
		return executeTimedQuery(prepareStatement(preparedStatement, pairs));
	}

	/*
	 * Sends query and reestablishes connection if lost.
	 */
	protected static ResultSet executeQuery(String preparedStatement, Object... args) {
		return executeTimedQuery(prepareStatement(preparedStatement, args));
	}
	
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	protected static ResultSet executeQuery(PreparedStatement preparedStatement) {
		return executeTimedQuery(preparedStatement);
	}

	/*
	 * Sends query and reestablishes connection if lost.
	 */
	protected static ResultSet executeQuery(String query) {
		return executeTimedQuery(query);
	}
	
	
	// Updates
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	@SafeVarargs
	protected static <T> int executeUpdate(String preparedStatement, Pair<Class<T>, T>... pairs) {
		return executeTimedUpdate(prepareStatement(preparedStatement, pairs));
	}

	/*
	 * Sends query and reestablishes connection if lost.
	 */
	protected static int executeUpdate(String preparedStatement, Object... args) {
		return executeTimedUpdate(prepareStatement(preparedStatement, args));
	}
	
	/*
	 * Sends update and reestablishes connection if lost.
	 */
	protected static int executeUpdate(PreparedStatement preparedStatement) {
		return executeTimedUpdate(preparedStatement);
	}
	
	/*
	 * Sends update and reestablishes connection if lost.
	 */
	protected static int executeUpdate(String update) {
		return executeTimedUpdate(update);
	}
	
	
	// Private
	/**
	 * Executes query specified by {@code preparedStatement} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static ResultSet executeTimedQuery(PreparedStatement preparedStatement) {
		ResultSet resultSet = null;
		
		try {
			resultSet = DatabaseUtil.executeWithTimer(() -> preparedStatement.executeQuery(), preparedStatement);
		} catch (RuntimeException e) {
			openConnection();
			resultSet = DatabaseUtil.executeWithTimer(() -> preparedStatement.executeQuery(), preparedStatement);
		}
		
		return resultSet;
	}

	/**
	 * Executes query specified by {@code query} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static ResultSet executeTimedQuery(String query) {
		ResultSet resultSet = null;
		
		try (Statement statement = connection.createStatement()) {
			try {
				resultSet = DatabaseUtil.executeWithTimer(() -> statement.executeQuery(query), statement);
			} catch (RuntimeException e) {
				openConnection();
				resultSet = DatabaseUtil.executeWithTimer(() -> statement.executeQuery(query), statement);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return resultSet;
	}
	
	/**
	 * Executes update specified by {@code preparedStatement} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedUpdate(PreparedStatement preparedStatement) {
		int linesChanged;
		
		try {
			linesChanged = DatabaseUtil.executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement);
		} catch (RuntimeException e) {
			openConnection();
			linesChanged = DatabaseUtil.executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement);
		}
		
		return linesChanged;
	}	
	
	/**
	 * Executes update specified by {@code query} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedUpdate(String update) {
		int linesChanged = 0;

		try (Statement statement = connection.createStatement()) {
			try {
				linesChanged = DatabaseUtil.executeWithTimer(() -> statement.executeUpdate(update), statement);
			} catch (RuntimeException e) {
				openConnection();
				linesChanged = DatabaseUtil.executeWithTimer(() -> statement.executeUpdate(update), statement);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
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
	
	/**
	 * Creates a {@link PreparedStatement} and initializes with the specified {@code args} as wildcard parameters.
	 * Uses {@link DatabaseUtil#setStatementArgument} to determine SQL parameter types.
	 */
	private static PreparedStatement prepareStatement(String statement, Object... args) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(statement);
			for (int argNum = 1; argNum <= args.length; argNum++)
				DatabaseUtil.setStatementArgument(preparedStatement, args[argNum - 1], null, argNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
	}

	/**
	 * Creates a {@link PreparedStatement} and initializes with the specified {@code args} as wildcard parameters.
	 * Uses {@link DatabaseUtil#setStatementArgument} to determine SQL parameter types.
	 */
	@SafeVarargs
	private static <T> PreparedStatement prepareStatement(String statement, Pair<Class<T>, T>... pairs) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(statement);
			for (int argNum = 1; argNum <= pairs.length; argNum++)
				DatabaseUtil.setStatementArgument(preparedStatement, pairs[argNum - 1].getValue(), pairs[argNum - 1].getKey(), argNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
		
		
	}
}



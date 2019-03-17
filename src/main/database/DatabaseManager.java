package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.Callable;

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
	public static PreparedStatement getPreparedStatement(String statement) {
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
	public static List<Record> executeQuery(String query) {
		ResultSet resultSet = executeTimedQuery(query);
		return Record.generateRecords(resultSet);
	}	

	/*
	 * Sends query and reestablishes connection if lost.
	 */
	public static List<Record> executeQuery(PreparedStatement preparedStatement) {
		ResultSet resultSet = executeTimedQuery(preparedStatement);
		return Record.generateRecords(resultSet);
	}

	/*
	 * Sends query and reestablishes connection if lost.
	 */
	public static List<Record> executeQuery(String preparedStatement, Object... args) {
		ResultSet resultSet = executeTimedQuery(prepareStatement(preparedStatement, false, args));
		return Record.generateRecords(resultSet);
	}
	
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	@SafeVarargs
	public static <T> List<Record> executeQuery(String preparedStatement, Pair<Class<T>, T>... pairs) {
		ResultSet resultSet = executeTimedQuery(prepareStatement(preparedStatement, false, pairs));
		return Record.generateRecords(resultSet);
	}

	
	// Updates
	/*
	 * Sends update and reestablishes connection if lost.
	 */
	public static int executeUpdate(String update) {
		return executeTimedUpdate(update);
	}

	/*
	 * Sends update and reestablishes connection if lost.
	 */
	public static int executeUpdate(PreparedStatement preparedStatement) {
		return executeTimedUpdate(preparedStatement);
	}

	/*
	 * Sends query and reestablishes connection if lost.
	 */
	public static int executeUpdate(String preparedStatement, Object... args) {
		return executeTimedUpdate(prepareStatement(preparedStatement, false, args));
	}
	
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	@SafeVarargs
	public static <T> int executeUpdate(String preparedStatement, Pair<Class<T>, T>... pairs) {
		return executeTimedUpdate(prepareStatement(preparedStatement, false, pairs));
	}

	
	// Insertion
	/*
	 * Sends update and reestablishes connection if lost.
	 */
	public static int executeInsertGetID(String insert) {
		return executeTimedInsertGetID(insert);
	}
	
	/*
	 * Sends update and reestablishes connection if lost.
	 */
	public static int executeInsertGetID(PreparedStatement preparedStatement) {
		// TODO: MUST BE KEYGEN-STATEMENT!!

		return executeTimedInsertGetID(preparedStatement);
	}
	
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	public static int executeInsertGetID(String preparedStatement, Object... args) {
		return executeTimedInsertGetID(prepareStatement(preparedStatement, true, args));
	}
	
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	@SafeVarargs
	public static <T> int executeInsertGetID(String preparedStatement, Pair<Class<T>, T>... pairs) {
		return executeTimedInsertGetID(prepareStatement(preparedStatement, true, pairs));
	}
	
	
	// Private
	/**
	 * Executes query specified by {@code query} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static ResultSet executeTimedQuery(String query) {
		ResultSet resultSet = null;
		
		try (Statement statement = connection.createStatement()) {
			try {
				resultSet = executeWithTimer(() -> statement.executeQuery(query), statement);
			} catch (RuntimeException e) {
				openConnection();
				resultSet = executeWithTimer(() -> statement.executeQuery(query), statement);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return resultSet;
	}
	
	/**
	 * Executes query specified by {@code preparedStatement} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static ResultSet executeTimedQuery(PreparedStatement preparedStatement) {
		ResultSet resultSet = null;
		
		try {
			resultSet = executeWithTimer(() -> preparedStatement.executeQuery(), preparedStatement);
		} catch (RuntimeException e) {
			openConnection();
			resultSet = executeWithTimer(() -> preparedStatement.executeQuery(), preparedStatement);
		}
		
		return resultSet;
	}

	/**
	 * Executes update specified by {@code query} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedUpdate(String update) {
		int linesChanged = 0;

		try (Statement statement = connection.createStatement()) {
			try {
				linesChanged = executeWithTimer(() -> statement.executeUpdate(update), statement);
			} catch (RuntimeException e) {
				openConnection();
				linesChanged = executeWithTimer(() -> statement.executeUpdate(update), statement);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return linesChanged;
	}
	
	/**
	 * Executes update specified by {@code preparedStatement} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedUpdate(PreparedStatement preparedStatement) {
		int linesChanged;
		
		try {
			linesChanged = executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement);
		} catch (RuntimeException e) {
			openConnection();
			linesChanged = executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement);
		}
		
		return linesChanged;
	}	
	
	/**
	 * Executes update specified by {@code query} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedInsertGetID(String insert) {
		int id = -1;
		
		try (Statement statement = connection.createStatement()) {
			try {
				executeWithTimer(() -> statement.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS), statement);
				ResultSet generatedKeys = statement.getGeneratedKeys();
				if (generatedKeys.next())
					id = generatedKeys.getInt(1);
			} catch (RuntimeException e) {
				openConnection();
				executeWithTimer(() -> statement.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS), statement);
				ResultSet generatedKeys = statement.getGeneratedKeys();
				if (generatedKeys.next())
					id = generatedKeys.getInt(1);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return id;
	}
	
	/**
	 * Executes update specified by {@code preparedStatement} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedInsertGetID(PreparedStatement preparedStatement) {
		int id = -1;
		
		try {
			try {
				executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement);
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				if (generatedKeys.next())
					id = generatedKeys.getInt(1);
			} catch (RuntimeException e) {
				openConnection();
				executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement);
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				if (generatedKeys.next())
					id = generatedKeys.getInt(1);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return id;
	}	
	
	/**
	 * Wraps {@code Callable} in a timer, used for estimating statement execution time.
	 */
	private static <T> T executeWithTimer(Callable<T> callable, Statement statement) {
		T result = null;
		try {
			long start = System.nanoTime();
			result = callable.call();
			long end = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (end - start) / Math.pow(10, 9), statement);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
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
	private static PreparedStatement prepareStatement(String statement, boolean getID, Object... args) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(statement, getID ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
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
	private static <T> PreparedStatement prepareStatement(String statement, boolean getID, Pair<Class<T>, T>... pairs) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(statement, getID ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
			for (int argNum = 1; argNum <= pairs.length; argNum++)
				DatabaseUtil.setStatementArgument(preparedStatement, pairs[argNum - 1].getValue(), pairs[argNum - 1].getKey(), argNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preparedStatement;
		
		
	}
}



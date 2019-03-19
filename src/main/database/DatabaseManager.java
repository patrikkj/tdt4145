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
	
	
	public static List<Record> executeQuery(String query) {
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return Record.generateRecords(resultSet);
	}	

	public static List<Record> executeQuery(String preparedStatement, Object... args) {
		ResultSet resultSet = null;
		try {
			resultSet = prepareStatement(preparedStatement, false, args).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Record.generateRecords(resultSet);
	}
	
	public static int executeUpdate(String update) {
		int linesChanged;
		try {
			Statement statement = connection.createStatement();
			linesChanged = statement.executeUpdate(update);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return linesChanged;
	}
	
	public static int executeUpdate(String preparedStatement, Object... args) {
		return executeTimedUpdate(prepareStatement(preparedStatement, false, args));
	}
	
	public static int executeInsertGetID(String insert) {
		return executeTimedInsertGetID(insert);
	}

	public static int executeInsertGetID(String preparedStatement, Object... args) {
		return executeTimedInsertGetID(prepareStatement(preparedStatement, true, args));
	}
	
	// Private
	private static ResultSet execute(PreparedStatement preparedStatement) {
		ResultSet resultSet = null;
		
		try {
			resultSet = executeWithTimer(() -> preparedStatement.executeQuery(), preparedStatement.toString());
		} catch (RuntimeException e) {
			openConnection();
			resultSet = executeWithTimer(() -> preparedStatement.executeQuery(), preparedStatement.toString());
		}
		
		return resultSet;
	}

	/**
	 * Executes update specified by {@code query} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedUpdate(String update) {
		int linesChanged = 0;

		try {
			Statement statement = connection.createStatement();
			try {
				linesChanged = executeWithTimer(() -> statement.executeUpdate(update), update);
			} catch (RuntimeException e) {
				openConnection();
				linesChanged = executeWithTimer(() -> statement.executeUpdate(update), update);
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
			linesChanged = executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement.toString());
		} catch (RuntimeException e) {
			openConnection();
			linesChanged = executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement.toString());
		}
		
		return linesChanged;
	}	
	
	/**
	 * Executes insertion specified by {@code query} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedInsertGetID(String insert) {
		int id = -1;
		
		try {
			Statement statement = connection.createStatement();
			try {
				executeWithTimer(() -> statement.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS), insert);
				ResultSet generatedKeys = statement.getGeneratedKeys();
				if (generatedKeys.next())
					id = generatedKeys.getInt(1);
			} catch (RuntimeException e) {
				openConnection();
				executeWithTimer(() -> statement.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS), insert);
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
	 * Executes insertion specified by {@code preparedStatement} and prints execution time to console.
	 * Reattempts once if first execution fails.
	 */
	private static int executeTimedInsertGetID(PreparedStatement preparedStatement) {
		int id = -1;
		
		try {
			try {
				executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement.toString());
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				if (generatedKeys.next())
					id = generatedKeys.getInt(1);
			} catch (RuntimeException e) {
				openConnection();
				executeWithTimer(() -> preparedStatement.executeUpdate(), preparedStatement.toString());
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
	private static <T> T executeWithTimer(Callable<T> callable, String statement) {
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
	
	/**
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
	
	/**
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



package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseManager {
	private static final String DB_DRIVER_PATH = "com.mysql.cj.jdbc.Driver";
	private static final String DB_NAME = "patrikkj_iexercise_db";
	private static final String DB_USERNAME = "patrikkj_iexercise";
	private static final String DB_PASSWORD = "iexercise";
	private static final String CONNECTION_STRING = "jdbc:mysql://mysql.stud.ntnu.no/" + DB_NAME + "?serverTimezone=UTC";
	
	private static Connection connection;
	
	
	// Connection
	/**
	 * Opens SQL connection.
	 */
	public static void openConnection() {
		try {
			Class.forName(DB_DRIVER_PATH);
			connection = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes SQL connection.
	 */
	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	// Queries
	/*
	 * Returns a list of {@} 
	 */
	public static List<Record> executeQuery(String query) {
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Record.generateRecords(resultSet);
	}	

	/*
	 * Sends query and reestablishes connection if lost.
	 */
	public static List<Record> executeQuery(String preparedStatement, Object... args) {
		ResultSet resultSet = null;
		try {
			PreparedStatement statement = prepareStatement(preparedStatement, false, args);
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Record.generateRecords(resultSet);
	}
	
	
	// Updates
	/*
	 * Sends update and reestablishes connection if lost.
	 */
	public static int executeUpdate(String update) {
		int linesChanged = 0;
		try {
			Statement statement = connection.createStatement();
			linesChanged = statement.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return linesChanged;
	}

	/*
	 * Sends update and reestablishes connection if lost.
	 */
	public static int executeUpdate(String preparedStatement, Object... args) {
		int linesChanged = 0;
		try {
			PreparedStatement statement = prepareStatement(preparedStatement, false, args);
			linesChanged = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return linesChanged;
	}
	
	
	// Insertion
	/*
	 * Sends update and reestablishes connection if lost.
	 */
	public static int executeInsertGetID(String insert) {
		int id = -1;
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next())
				id = generatedKeys.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	public static int executeInsertGetID(String preparedStatement, Object... args) {
		int id = -1;
		try {
			PreparedStatement statement = prepareStatement(preparedStatement, true, args);
			statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next())
				id = generatedKeys.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	// Private 
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
	
}



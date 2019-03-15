package main.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class DatabaseUtil {
	private static final Map<Class<?>, Integer> mapToSQL = new HashMap<>();
	static {
		mapToSQL.put(String.class, Types.VARCHAR);
		mapToSQL.put(Double.class, Types.DECIMAL);
		mapToSQL.put(Integer.class, Types.INTEGER);
		mapToSQL.put(Boolean.class, Types.BOOLEAN);
		mapToSQL.put(Date.class, Types.DATE);
		mapToSQL.put(LocalDate.class, Types.DATE);
		mapToSQL.put(Time.class, Types.TIME);
		mapToSQL.put(LocalTime.class, Types.TIME);
	}
	
	protected static void setStatementArgument(PreparedStatement preparedStatement, Object arg, Class<?> type, int paramIndex) throws SQLException {
		if (arg instanceof String)
			preparedStatement.setString(paramIndex, (String) arg);
		else if (arg instanceof Double)
			preparedStatement.setDouble(paramIndex, (Double) arg);
		else if (arg instanceof Integer)
			preparedStatement.setInt(paramIndex, (Integer) arg);
		else if (arg instanceof Boolean)
			preparedStatement.setBoolean(paramIndex, (Boolean) arg);
		else if (arg instanceof Date)
			preparedStatement.setDate(paramIndex, (Date) arg);
		else if (arg instanceof LocalDate)
			preparedStatement.setDate(paramIndex, Date.valueOf((LocalDate) arg));
		else if (arg instanceof Time)
			preparedStatement.setTime(paramIndex, (Time) arg);
		else if (arg instanceof LocalTime)
			preparedStatement.setTime(paramIndex, Time.valueOf((LocalTime) arg));
		else
			preparedStatement.setNull(paramIndex, DatabaseUtil.classToSQLType(type));
	}
	
	protected static Integer classToSQLType(Class<?> classType) {
		return mapToSQL.getOrDefault(classType, Types.VARCHAR);
	}
	
	protected static <T> T executeWithTimer(Callable<T> callable, Statement statement) {
		T result = null;
		try {
			long start = System.nanoTime();
			result = callable.call();
			long end = System.nanoTime();
			System.out.format("\tTime: %9.9s seconds   Query: %s%n", (end - start) / Math.pow(10, 9), statement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}

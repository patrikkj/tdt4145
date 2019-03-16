package main.database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DatabaseUtil {
	protected static final Map<Class<?>, Integer> mapToSQL = new HashMap<>();
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
	
	protected static final Map<String, Class<?>> mapToClass = new HashMap<>();
	static {
		mapToClass.put("CHAR", String.class);
		mapToClass.put("VARCHAR", String.class);
		mapToClass.put("LONGVARCHAR", String.class);
		mapToClass.put("NUMERIC", BigDecimal.class);
		mapToClass.put("DECIMAL", BigDecimal.class);
		mapToClass.put("BIT", Boolean.class);
		mapToClass.put("TINYINT", Integer.class);
		mapToClass.put("SMALLINT", Integer.class);
		mapToClass.put("INTEGER", Integer.class);
		mapToClass.put("BIGINT", Long.class);
		mapToClass.put("REAL", Float.class);
		mapToClass.put("FLOAT", Double.class);
		mapToClass.put("DOUBLE", Double.class);
		mapToClass.put("BINARY", byte[].class);
		mapToClass.put("VARBINARY", byte[].class);
		mapToClass.put("LONGVARBINARY", byte[].class);
		mapToClass.put("DATE", Date.class);
		mapToClass.put("TIME", Time.class);
		mapToClass.put("TIMESTAMP", Timestamp.class);
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
	
}

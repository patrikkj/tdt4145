package main.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Record {
	private final Map<String, Object> map;
	
	/**
	 * Private constructor to prevent instantiation. 
	 * {@code Record} instances should be created via the {@link #generateRecords} method only.
	 */
	private Record(Map<String, Object> map) {
		this.map = map;
	}
	
	/**
	 * Returns the value to which the specified key is mapped.
	 * Key can be of the form 'columnName' or 'tableName.columnName'.
	 * @param attributeType the type of the returned value
	 * @param key the key whose associated value is to be returned
	 */
	public <T> T get(Class<T> attributeType, String key) {
		Object value = map.get(key);

		if (!map.containsKey(key))
			throw new NoSuchElementException(String.format("There is no key with value '%s'. Accessible keys: [%s]", key, map.keySet()));
		
		if (value != null  &&  !attributeType.isInstance(value))
			throw new ClassCastException(String.format("Record attribute '%s' holds a value of type '%s', which is not a subtype of '%s'." , key, value, attributeType));
		
		return attributeType.cast(value);
	}
	
	/**
	 * Converts a {@code ResultSet} passed by the JDBC to a list of Records.
	 * Each {@code Record} corresponds to a row in the given {@code ResultSet}.
	 */
	public static List<Record> generateRecords(ResultSet resultSet) {
		List<Record> records = new ArrayList<>();
		
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
			while (resultSet.next()) {
				Map<String, Object> resultMap = new HashMap<>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String tableName = rsmd.getTableName(i);
					String columnName = rsmd.getColumnName(i);
					String fullName = String.format("%s.%s", tableName, columnName);
					Object value = resultSet.getObject(i, DatabaseUtil.mapToClass);
					resultMap.put(columnName, value);
					resultMap.put(fullName, value);
				} 
				records.add(new Record(resultMap)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return records;
	}
}

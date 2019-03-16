package main.deprecated;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseQueryHandler<T> {
	
	protected abstract T extractRaw(ResultSet resultSet) throws SQLException;
	
	public T extract(ResultSet resultSet) {
		T t = null;
		try {
			t = extractRaw(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	};
}

package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.models.Notat;

public class NotatQueryHandler {
	public static Notat extractNotatFromResultSet(ResultSet resultSet) throws SQLException {
	    int notatID = resultSet.getInt("notatID");
	    String tekst = resultSet.getString("tekst");
	    
		return new Notat(notatID, tekst);
	}
}

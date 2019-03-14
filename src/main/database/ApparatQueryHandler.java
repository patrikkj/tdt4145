package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.models.Apparat;

public class ApparatQueryHandler {
	
	public static Apparat extractApparatFromResultSet(ResultSet resultSet) throws SQLException {
	    int apparatID = resultSet.getInt("apparatID");
	    String navn = resultSet.getString("navn");
	    String virkemåte = resultSet.getString("virkemåte");
	    
		return new Apparat(apparatID, navn, virkemåte);
	}
}

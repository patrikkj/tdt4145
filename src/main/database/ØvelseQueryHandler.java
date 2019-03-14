package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.models.Apparat;
import main.models.Øvelse;

public class ØvelseQueryHandler {
	public static Øvelse extractØvelseFromResultSet(ResultSet resultSet) throws SQLException {
	    int øvelseID = resultSet.getInt("øvelseID");
	    String navn = resultSet.getString("navn");
	    String beskrivelse = resultSet.getString("beskrivelse");
	    Apparat apparat = ApparatQueryHandler.extractApparatFromResultSet(resultSet);
	    
		return new Øvelse(øvelseID, navn, beskrivelse, apparat);
	}
}

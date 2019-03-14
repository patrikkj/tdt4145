package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.models.Treningsøkt;
import main.models.Øvelse;
import main.relations.UtførtØvelse;

public class UtførtØvelseQueryHandler {
	
	public static UtførtØvelse extractUtførtØvelseFromResultSet(ResultSet resultSet) throws SQLException {
		Treningsøkt treningsøkt = TreningsøktQueryHandler.extractTreningsøktFromResultSet(resultSet);
		Øvelse øvelse = ØvelseQueryHandler.extractØvelseFromResultSet(resultSet);
	    int antallSett = resultSet.getInt("antallSett");
	    int antallKilo = resultSet.getInt("antallKilo");
	    
		return new UtførtØvelse(treningsøkt, øvelse, antallSett, antallKilo);
	}
}

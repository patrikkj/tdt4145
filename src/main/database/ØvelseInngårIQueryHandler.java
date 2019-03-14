package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.models.Øvelse;
import main.models.ØvelseGruppe;
import main.relations.ØvelseInngårI;

public class ØvelseInngårIQueryHandler {
	public static ØvelseInngårI extractØvelseInngårIFromResultSet(ResultSet resultSet) throws SQLException {
		Øvelse øvelse = ØvelseQueryHandler.extractØvelseFromResultSet(resultSet);
		ØvelseGruppe øvelseGruppe = ØvelseGruppeQueryHandler.extractØvelseFromResultSet(resultSet);
	    
		return new ØvelseInngårI(øvelse, øvelseGruppe);
	}
}

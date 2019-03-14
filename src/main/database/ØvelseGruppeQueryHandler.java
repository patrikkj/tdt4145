package main.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.models.ØvelseGruppe;

public class ØvelseGruppeQueryHandler {
	public static ØvelseGruppe extractØvelseFromResultSet(ResultSet resultSet) throws SQLException {
	    int øvelseGruppeID = resultSet.getInt("øvelseGruppeID");
	    String gruppeNavn = resultSet.getString("gruppeNavn");
	    
		return new ØvelseGruppe(øvelseGruppeID, gruppeNavn);
	}
}

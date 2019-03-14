package main.database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import main.models.Notat;
import main.models.Treningsøkt;

public class TreningsøktQueryHandler {
	public static Treningsøkt extractTreningsøktFromResultSet(ResultSet resultSet) throws SQLException {
	    int øktID = resultSet.getInt("øktID");
	    Date dato = resultSet.getDate("dato");
	    Time tidspunkt = resultSet.getTime("tidspunkt");
	    Time varighet = resultSet.getTime("varighet");
	    int form = resultSet.getInt("form");
	    int prestasjon = resultSet.getInt("prestasjon");
	    Notat notat = NotatQueryHandler.extractNotatFromResultSet(resultSet);
	    
		return new Treningsøkt(øktID, dato, tidspunkt, varighet, form, prestasjon, notat);
	}
}

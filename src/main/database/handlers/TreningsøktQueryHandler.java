package main.database.handlers;

import java.sql.Date;
import java.sql.Time;

import main.database.Record;
import main.models.Notat;
import main.models.Treningsøkt;

public class TreningsøktQueryHandler {
	public static Treningsøkt extractTreningsøktFromRecord(Record record) {
	    int øktID = record.get(Integer.class, "øktID");
	    Date dato = record.get(Date.class, "dato");
	    Time tidspunkt = record.get(Time.class, "tidspunkt");
	    Time varighet = record.get(Time.class, "varighet");
	    int form = record.get(Integer.class, "form");
	    int prestasjon = record.get(Integer.class, "prestasjon");
	    Notat notat = NotatQueryHandler.extractNotatFromRecord(record);
	    
		return new Treningsøkt(øktID, dato, tidspunkt, varighet, form, prestasjon, notat);
	}
}

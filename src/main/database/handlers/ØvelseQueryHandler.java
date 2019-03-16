package main.database.handlers;

import main.database.Record;
import main.models.Apparat;
import main.models.Øvelse;

public class ØvelseQueryHandler {
	public static Øvelse extractØvelseFromRecord(Record record) {
	    int øvelseID = record.get(Integer.class, "øvelseID");
	    String navn = record.get(String.class, "navn");
	    String beskrivelse = record.get(String.class, "beskrivelse");
	    Apparat apparat = ApparatQueryHandler.extractApparatFromRecord(record);
	    
		return new Øvelse(øvelseID, navn, beskrivelse, apparat);
	}
}

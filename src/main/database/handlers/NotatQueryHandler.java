package main.database.handlers;

import main.database.Record;
import main.models.Notat;

public class NotatQueryHandler {
	public static Notat extractNotatFromRecord(Record record) {
	    int notatID = record.get(Integer.class, "notatID");
	    String tekst = record.get(String.class, "tekst");
	    
		return new Notat(notatID, tekst);
	}
}

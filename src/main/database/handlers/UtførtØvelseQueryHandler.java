package main.database.handlers;

import main.database.Record;
import main.models.Treningsøkt;
import main.models.Øvelse;
import main.relations.UtførtØvelse;

public class UtførtØvelseQueryHandler {
	
	public static UtførtØvelse extractUtførtØvelseFromRecord(Record record) {
		Treningsøkt treningsøkt = TreningsøktQueryHandler.extractTreningsøktFromRecord(record);
		Øvelse øvelse = ØvelseQueryHandler.extractØvelseFromRecord(record);
	    int antallSett = record.get(Integer.class, "antallSett");
	    int antallKilo = record.get(Integer.class, "antallKilo");
	    
		return new UtførtØvelse(treningsøkt, øvelse, antallSett, antallKilo);
	}
}

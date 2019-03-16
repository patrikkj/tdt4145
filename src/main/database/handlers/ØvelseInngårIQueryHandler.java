package main.database.handlers;

import main.database.Record;
import main.models.Øvelse;
import main.models.ØvelseGruppe;
import main.relations.ØvelseInngårI;

public class ØvelseInngårIQueryHandler {
	public static ØvelseInngårI extractØvelseInngårIFromRecord(Record record) {
		Øvelse øvelse = ØvelseQueryHandler.extractØvelseFromRecord(record);
		ØvelseGruppe øvelseGruppe = ØvelseGruppeQueryHandler.extractØvelseFromRecord(record);
	    
		return new ØvelseInngårI(øvelse, øvelseGruppe);
	}
}

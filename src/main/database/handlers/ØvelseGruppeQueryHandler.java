package main.database.handlers;

import main.database.Record;
import main.models.ØvelseGruppe;

public class ØvelseGruppeQueryHandler {
	public static ØvelseGruppe extractØvelseFromRecord(Record record) {
	    int øvelseGruppeID = record.get(Integer.class, "øvelseGruppeID");
	    String gruppeNavn = record.get(String.class, "gruppeNavn");
	    
		return new ØvelseGruppe(øvelseGruppeID, gruppeNavn);
	}
}

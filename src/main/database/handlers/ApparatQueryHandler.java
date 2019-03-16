package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Apparat;

public class ApparatQueryHandler {
	public static List<Apparat> getApparater() {
		List<Record> resultMaps = DatabaseManager.executeQuery("SELECT * FROM apparat");
		return resultMaps.stream()
				.map(ApparatQueryHandler::extractApparatFromRecord)
				.collect(Collectors.toList());
	}
	
	
	public static Apparat extractApparatFromRecord(Record record) {
	    int apparatID = record.get(Integer.class, "apparatID");
	    String navn = record.get(String.class, "navn");
	    String virkemåte = record.get(String.class, "virkemåte");
	    
		return new Apparat(apparatID, navn, virkemåte);
	}
}

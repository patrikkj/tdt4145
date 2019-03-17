package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Equipment;

public class EquipmentQueryHandler {
	
	public static List<Equipment> getEquipments() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM equipment;");
		return records.stream()
				.map(EquipmentQueryHandler::extractEquipmentFromRecord)
				.collect(Collectors.toList());
	}
	
	public static Equipment getEquipmentByID(int equipmentID) {
		String query = String.format("SELECT * FROM equipment WHERE equipment_id = '%s';", equipmentID);
		List<Record> records = DatabaseManager.executeQuery(query);
		return extractEquipmentFromRecord(records.get(0));
	}
	
	public static Equipment extractEquipmentFromRecord(Record record) {
	    int equipmentID = record.get(Integer.class, "equipment_id");
	    String name = record.get(String.class, "name");
	    String description = record.get(String.class, "description");
	    
		return new Equipment(equipmentID, name, description);
	}
}

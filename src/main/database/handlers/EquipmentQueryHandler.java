package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Equipment;

public class EquipmentQueryHandler {
	
	/**
	 * Returns a list containing every {@code Equipment} entity in the database.
	 */
	public static List<Equipment> getEquipments() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM equipment");
		return records.stream()
				.map(EquipmentQueryHandler::extractEquipmentFromRecord)
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns the {@code Equipment} identified by the given {@code equipmentID}.
	 */
	public static Equipment getEquipmentByID(int equipmentID) {
		String query = String.format("SELECT * FROM equipment WHERE equipment_id = '%s'", equipmentID);
		List<Record> records = DatabaseManager.executeQuery(query);
		return extractEquipmentFromRecord(records.get(0));
	}
	
	/**
	 * Updates the database record for the equipment specified.
	 * @return the number of lines changed.
	 */
	public static int updateEquipment(Equipment equipment) {
		String update = "UPDATE equipment SET name = ?, description = ? WHERE equipment_id = ?"; 
		return DatabaseManager.executeUpdate(update, 
				equipment.getName(),
				equipment.getDescription(),
				equipment.getEquipmentID());
	}
	
	/**
	 * Inserts a new database record corresponding to the given {@code equipment}.
	 * @return the auto-generated identifier.
	 */
	public static int insertEquipmentAssignID(Equipment equipment) {
		String insert = "INSERT INTO equipment (name, description) VALUES (?, ?)";
		int equipmentID = DatabaseManager.executeInsertGetID(insert,
				equipment.getName(),
				equipment.getDescription());
		equipment.setEquipmentID(equipmentID);
		return equipmentID;
	}
	
	/**
	 * Deletes the database record for the equipment specified.
	 * @return the number of lines changed.
	 */
	public static int deleteEquipment(Equipment equipment) {
		String delete = "DELETE FROM equipment WHERE equipment_id = ?";
		return DatabaseManager.executeUpdate(delete, equipment.getEquipmentID());
	}
	
	/**
	 * Deletes the database record for every entity in the list.
	 * @return the number of lines changed.
	 */
	public static int deleteEquipments(List<Equipment> equipments) {
		String parsedIDs = equipments.stream()
				.map(e -> String.format("'%s'", e.getEquipmentID()))
				.collect(Collectors.joining(", ", "(", ")"));
		String delete = String.format("DELETE FROM equipment WHERE equipment_id IN %s", parsedIDs);
		return DatabaseManager.executeUpdate(delete);
	}
	
	/**
	 * Creates a new {@code Equipment} instance from the record specified.
	 */
	public static Equipment extractEquipmentFromRecord(Record record) {
		// Assert that record contains a valid instance of this class
		Integer equipmentID = record.get(Integer.class, "equipment.equipment_id");
		if (equipmentID == null)
			return null;
		
		String name = record.get(String.class, "equipment.name");
	    String description = record.get(String.class, "equipment.description");
		
	    return new Equipment(equipmentID, name, description);
	}
}

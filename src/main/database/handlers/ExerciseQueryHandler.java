package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Equipment;
import main.models.Exercise;

public class ExerciseQueryHandler {

	public static List<Exercise> getExercises() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM exercise;");
		return records.stream()
				.map(ExerciseQueryHandler::extractExerciseFromRecord)
				.collect(Collectors.toList());
	}

	public static Exercise getExerciseByID(int exerciseID) {
		String query = String.format("SELECT * FROM exercise WHERE exercise_id = '%s';", exerciseID);
		List<Record> records = DatabaseManager.executeQuery(query);
		return extractExerciseFromRecord(records.get(0));
	}
	
	public static Exercise extractExerciseFromRecord(Record record) {
	    int exerciseID = record.get(Integer.class, "exercise_id");
	    String name = record.get(String.class, "name");
	    String description = record.get(String.class, "description");
	    Equipment equipment = EquipmentQueryHandler.extractEquipmentFromRecord(record);
	    
		return new Exercise(exerciseID, name, description, equipment);
	}
}

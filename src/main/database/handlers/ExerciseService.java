package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Equipment;
import main.models.Exercise;

public class ExerciseService {
	// TODO: Might have to use Pair<Class, Object> implementation for null handling
	/**
	 * Returns a list containing every {@code Exercise} entity in the database.
	 */
	public static List<Exercise> getExercises() {
		String query = "SELECT * FROM exercise AS a "
				+ "LEFT JOIN equipment AS b ON a.equipment_id = b.equipment_id";
		List<Record> records = DatabaseManager.executeQuery(query);
		return records.stream()
				.map(ExerciseService::extractExerciseFromRecord)
				.collect(Collectors.toList());
	}

	/**
	 * Returns the {@code Exercise} identified by the given {@code exerciseID}.
	 */
	public static Exercise getExerciseByID(int exerciseID) {
		String query = "SELECT * FROM exercise WHERE exercise_id = ?";
		List<Record> records = DatabaseManager.executeQuery(query, exerciseID);
		return extractExerciseFromRecord(records.get(0));
	}
	
	/**
	 * Updates the database record for the exercise specified.
	 * @return the number of lines changed.
	 */
	public static int updateExercise(Exercise exercise) {
		String update = "UPDATE exercise SET name = ?, description = ?, equipment_id = ? WHERE exercise_id = ?"; 
		return DatabaseManager.executeUpdate(update, 
				exercise.getName(),
				exercise.getDescription(),
				exercise.getEquipment() != null ? exercise.getEquipment().getEquipmentID() : null,
				exercise.getExerciseID());
	}
	
	/**
	 * Inserts a new database record corresponding to the given {@code exercise}.
	 * @return the auto-generated identifier.
	 */
	public static int insertExerciseAssignID(Exercise exercise) {
		String insert = "INSERT INTO exercise (name, description, equipment_id) VALUES (?, ?, ?)";
		int exerciseID = DatabaseManager.executeInsertGetID(insert,
				exercise.getName(),
				exercise.getDescription(),
				exercise.getEquipment() != null ? exercise.getEquipment().getEquipmentID() : null);
		exercise.setExerciseID(exerciseID);
		return exerciseID;
	}
	
	/**
	 * Deletes the database record for the exercise specified.
	 * @return the number of lines changed.
	 */
	public static int deleteExercise(Exercise exercise) {
		String delete = "DELETE FROM exercise WHERE exercise_id = ?";
		return DatabaseManager.executeUpdate(delete, exercise.getExerciseID());
	}
	
	/**
	 * Deletes the database record for every entity in the list.
	 * @return the number of lines changed.
	 */
	public static int deleteExercises(List<Exercise> exercises) {
		String parsedIDs = exercises.stream()
				.map(e -> String.format("'%s'", e.getExerciseID()))
				.collect(Collectors.joining(", ", "(", ")"));
		String delete = String.format("DELETE FROM exercise WHERE exercise_id IN %s", parsedIDs);
		return DatabaseManager.executeUpdate(delete);
	}

	/**
	 * Creates a new {@code Exercise} instance from the record specified.
	 */
	public static Exercise extractExerciseFromRecord(Record record) {
		// Assert that record contains a valid instance of this class
		Integer exerciseID = record.get("exercise.exercise_id");
		if (exerciseID == null)
			return null;
	    
	    String name = record.get("exercise.name");
	    String description = record.get("exercise.description");
	    Equipment equipment = EquipmentService.extractEquipmentFromRecord(record);
	    
		return new Exercise(exerciseID, name, description, equipment);
	}

	public static void main(String[] args) {
		insertExerciseAssignID(new Exercise("Squats", "Up and down", null));
		System.out.println(getExercises());
	}
}

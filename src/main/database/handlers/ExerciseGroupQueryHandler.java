package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.ExerciseGroup;

public class ExerciseGroupQueryHandler {
	
	/**
	 * Returns a list containing every {@code ExerciseGroup} entity in the database.
	 */
	public static List<ExerciseGroup> getExerciseGroups() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM exercise_group");
		return records.stream()
				.map(ExerciseGroupQueryHandler::extractExerciseGroupFromRecord)
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns the {@code ExerciseGroup} identified by the given {@code exerciseGroupID}.
	 */
	public static ExerciseGroup getExerciseGroupByID(int exerciseGroupID) {
		String query = "SELECT * FROM exercise_group WHERE exercise_group_id = ?";
		List<Record> records = DatabaseManager.executeQuery(query, exerciseGroupID);
		return extractExerciseGroupFromRecord(records.get(0));
	}
	
	/**
	 * Updates the database record for the exerciseGroup specified.
	 * @return the number of lines changed.
	 */
	public static int updateExerciseGroup(ExerciseGroup exerciseGroup) {
		String update = "UPDATE exercise_group SET name = ? WHERE exercise_group_id = ?"; 
		return DatabaseManager.executeUpdate(update, 
				exerciseGroup.getName(),
				exerciseGroup.getExerciseGroupID());
	}
	
	/**
	 * Inserts a new database record corresponding to the given {@code exerciseGroup}.
	 * @return the auto-generated identifier.
	 */
	public static int insertExerciseGroupAssignID(ExerciseGroup exerciseGroup) {
		String insert = "INSERT INTO exercise_group (name) VALUES (?)";
		int exerciseGroupID = DatabaseManager.executeInsertGetID(insert,
				exerciseGroup.getName());
		exerciseGroup.setExerciseGroupID(exerciseGroupID);
		return exerciseGroupID;
	}
	
	/**
	 * Deletes the database record for the exerciseGroup specified.
	 * @return the number of lines changed.
	 */
	public static int deleteExerciseGroup(ExerciseGroup exerciseGroup) {
		String delete = "DELETE FROM exercise_group WHERE exercise_group_id = ?";
		return DatabaseManager.executeUpdate(delete, exerciseGroup.getExerciseGroupID());
	}
	
	/**
	 * Deletes the database record for every entity in the list.
	 * @return the number of lines changed.
	 */
	public static int deleteExerciseGroups(List<ExerciseGroup> exerciseGroups) {
		String parsedIDs = exerciseGroups.stream()
				.map(e -> String.format("'%s'", e.getExerciseGroupID()))
				.collect(Collectors.joining(", ", "(", ")"));
		String delete = String.format("DELETE FROM exercise_group WHERE exercise_group_id IN %s", parsedIDs);
		return DatabaseManager.executeUpdate(delete);
	}

	/**
	 * Creates a new {@code ExerciseGroup} instance from the record specified.
	 */
	public static ExerciseGroup extractExerciseGroupFromRecord(Record record) {
		// Assert that record contains a valid instance of this class
		Integer exerciseGroupID = record.get(Integer.class, "exercise_group.exercise_group_id");
		if (exerciseGroupID == null)
			return null;
		
	    String name = record.get(String.class, "exercise_group.name");
		
	    return new ExerciseGroup(exerciseGroupID, name);
	}
	public static void main(String[] args) {
		insertExerciseGroupAssignID(new ExerciseGroup("Leg exercises"));
		insertExerciseGroupAssignID(new ExerciseGroup("Abs exercises"));
	}
}

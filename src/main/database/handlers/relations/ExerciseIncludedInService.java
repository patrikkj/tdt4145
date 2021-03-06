package main.database.handlers.relations;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.database.handlers.ExerciseGroupService;
import main.database.handlers.ExerciseService;
import main.models.Exercise;
import main.models.ExerciseGroup;
import main.models.relations.ExerciseIncludedIn;

public class ExerciseIncludedInService {
	
	/**
	 * Returns a list containing every {@code ExerciseIncludedIn} entity in the database.
	 */
	public static List<ExerciseIncludedIn> getExerciseIncludedInRelations() {
		String query = "SELECT * FROM exercise_included_in AS a "
				+ "INNER JOIN exercise AS b ON a.exercise_id = b.exercise_id "
				+ "INNER JOIN exercise_group AS c ON a.exercise_group_id = c.exercise_group_id";
		List<Record> records = DatabaseManager.executeQuery(query);
		return records.stream()
				.map(ExerciseIncludedInService::extractExerciseIncludedInFromRecord)
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns the {@code ExerciseIncludedIn} identified by the given {@code exerciseIncludedInID}.
	 */
	public static ExerciseIncludedIn getExerciseIncludedInByID(int exerciseIncludedInID) {
		String query = "SELECT * FROM exercise_included_in AS a "
				+ "INNER JOIN exercise AS b ON a.exercise_id = b.exercise_id "
				+ "INNER JOIN exercise_group AS c ON a.exercise_group_id = c.exercise_group_id "
				+ "WHERE exercise_included_in_id = ?";
		List<Record> records = DatabaseManager.executeQuery(query, exerciseIncludedInID);
		return extractExerciseIncludedInFromRecord(records.get(0));
	}

	/**
	 * Returns a list of every {@code ExerciseIncludedIn} entity related to the {@code exercise} given.
	 */
	public static List<ExerciseIncludedIn> getExerciseIncludedInRelationsByExercise(Exercise exercise) {
		String query = "SELECT * FROM exercise_included_in AS eii "
				+ "INNER JOIN exercise AS ex ON eii.exercise_id = ex.exercise_id "
				+ "LEFT JOIN equipment AS eq ON ex.equipment_id = eq.equipment_ID "
				+ "INNER JOIN exercise_group AS eg ON eii.exercise_group_id = eg.exercise_group_id "
				+ "WHERE eii.exercise_id = ?";
		List<Record> records = DatabaseManager.executeQuery(query, exercise.getExerciseID());
		return records.stream()
				.map(ExerciseIncludedInService::extractExerciseIncludedInFromRecord)
				.collect(Collectors.toList());
	}
	
	/**
	 * Updates the database record for the exerciseIncludedIn specified.
	 * @return the number of lines changed.
	 */
	public static int updateExerciseIncludedIn(ExerciseIncludedIn exerciseIncludedIn) {
		String update = "UPDATE exercise_included_in SET exercise_id = ?, exercise_group_id = ? WHERE exercise_included_in_id = ?"; 
		return DatabaseManager.executeUpdate(update, 
				exerciseIncludedIn.getExercise().getExerciseID(),				// NOT NULL
				exerciseIncludedIn.getExerciseGroup().getExerciseGroupID(),		// NOT NULL
				exerciseIncludedIn.getExerciseIncludedInID());
	}
	
	/**
	 * Inserts a new database record corresponding to the given {@code exerciseIncludedIn}.
	 * @return the auto-generated identifier.
	 */
	public static int insertExerciseIncludedInAssignID(ExerciseIncludedIn exerciseIncludedIn) {
		String insert = "INSERT INTO exercise_included_in (exercise_id, exercise_group_id) VALUES (?, ?)";
		int exerciseIncludedInID = DatabaseManager.executeInsertGetID(insert,
				exerciseIncludedIn.getExercise().getExerciseID(),				// NOT NULL
				exerciseIncludedIn.getExerciseGroup().getExerciseGroupID());	// NOT NULL
		exerciseIncludedIn.setExerciseIncludedInID(exerciseIncludedInID);
		return exerciseIncludedInID;
	}

	/**
	 * TODO: WRITE DOC
	 * @param exerciseIncludedInRelations
	 */
	public static int insertExerciseIncludedInRelations(List<ExerciseIncludedIn> exerciseIncludedInRelations) {
		if (exerciseIncludedInRelations.isEmpty())
			return -1;
		String values = exerciseIncludedInRelations.stream()
				.map(eii -> String.format("(%s, %s)", 
						eii.getExercise().getExerciseID(),				// NOT NULL
						eii.getExerciseGroup().getExerciseGroupID()))	// NOT NULL
				.collect(Collectors.joining(", "));
		String update = String.format("INSERT INTO exercise_included_in (exercise_id, exercise_group_id) VALUES %s",  values);
		return DatabaseManager.executeUpdate(update);	// Use update for creating normal query (No ID fetch)
	}
	
	/**
	 * Deletes the database record for the exerciseIncludedIn specified.
	 * @return the number of lines changed.
	 */
	public static int deleteExerciseIncludedIn(ExerciseIncludedIn exerciseIncludedIn) {
		String delete = "DELETE FROM exercise_included_in WHERE exercise_included_in_id = ?";
		return DatabaseManager.executeUpdate(delete, exerciseIncludedIn.getExerciseIncludedInID());
	}
	
	/**
	 * Deletes the database record for every entity in the list.
	 * @return the number of lines changed.
	 */
	public static int deleteExerciseIncludedInRelations(List<ExerciseIncludedIn> exerciseIncludedIns) {
		String parsedIDs = exerciseIncludedIns.stream()
				.map(e -> String.valueOf(e.getExerciseIncludedInID()))
				.collect(Collectors.joining(", ", "(", ")"));
		String delete = String.format("DELETE FROM exercise_included_in WHERE exercise_included_in_id IN %s", parsedIDs);
		return DatabaseManager.executeUpdate(delete);
	}

	/**
	 * TODO: WRITE DOCS
	 * @param exercise
	 */
	public static int deleteExerciseIncludedInRelationsByExercise(Exercise exercise) {
		String delete = "DELETE FROM exercise_included_in WHERE exercise_id = ?";
		return DatabaseManager.executeUpdate(delete, exercise.getExerciseID());
	}
	
	/**
	 * Creates a new {@code ExerciseIncludedIn} instance from the record specified.
	 */
	public static ExerciseIncludedIn extractExerciseIncludedInFromRecord(Record record) {
		// Assert that record contains a valid instance of this class
		Integer exerciseIncludedInID = record.get(Integer.class, "exercise_included_in.exercise_included_in_id");
		if (exerciseIncludedInID == null)
			return null;

		Exercise exercise = ExerciseService.extractExerciseFromRecord(record);
		ExerciseGroup exerciseGroup = ExerciseGroupService.extractExerciseGroupFromRecord(record);
		
		return new ExerciseIncludedIn(exerciseIncludedInID, exercise, exerciseGroup);
	}

}

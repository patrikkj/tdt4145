package main.database.handlers.relations;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.database.handlers.ExerciseService;
import main.database.handlers.WorkoutService;
import main.models.Exercise;
import main.models.Workout;
import main.models.relations.ExercisePerformed;

public class ExercisePerformedService {

	/**
	 * Returns a list containing every {@code ExercisePerformed} entity in the database.
	 */
	public static List<ExercisePerformed> getExercisePerformedRelations() {
		String query = "SELECT * FROM exercise_performed AS ep "
				+ "INNER JOIN workout AS wo ON ep.workout_id = wo.workout_id "
				+ "LEFT JOIN note AS no ON wo.note_id = no.note_id "
				+ "INNER JOIN exercise AS ex ON ep.exercise_id = ex.exercise_id "
				+ "LEFT JOIN equipment AS eq ON ex.equipment_id = eq.equipment_id ";
		List<Record> records = DatabaseManager.executeQuery(query);
		return records.stream()
				.map(ExercisePerformedService::extractExercisePerformedFromRecord)
				.collect(Collectors.toList());
	}

	/**
	 * Returns the {@code ExercisePerformed} identified by the given {@code exercisePerformedID}.
	 */
	public static ExercisePerformed getExercisePerformedByID(Integer exercisePerformedID) {
		String query = "SELECT * FROM exercise_performed AS ep "
				+ "INNER JOIN workout AS wo ON ep.workout_id = wo.workout_id "
				+ "LEFT JOIN note AS no ON wo.note_id = no.note_id "
				+ "INNER JOIN exercise AS ex ON ep.exercise_id = ex.exercise_id "
				+ "LEFT JOIN equipment AS eq ON e.equipment_id = eq.equipment_id "
				+ "WHERE exercise_performed_id = ?";
		List<Record> records = DatabaseManager.executeQuery(query, exercisePerformedID);
		return extractExercisePerformedFromRecord(records.get(0));
	}
	
	/**
	 * Returns the {@code ExercisePerformed} identified by the given {@code exercisePerformedID}.
	 */
	public static List<ExercisePerformed> getExercisePerformedRelationsByWorkout(Workout workout) {
		String query = "SELECT * FROM exercise_performed AS ep "
				+ "INNER JOIN workout AS wo ON ep.workout_id = wo.workout_id "
				+ "LEFT JOIN note AS no ON wo.note_id = no.note_id "
				+ "INNER JOIN exercise AS ex ON ep.exercise_id = ex.exercise_id "
				+ "LEFT JOIN equipment AS eq ON ex.equipment_id = eq.equipment_id "
				+ "WHERE ep.workout_id = ?";
		List<Record> records = DatabaseManager.executeQuery(query, workout.getWorkoutID());
		return records.stream()
				.map(ExercisePerformedService::extractExercisePerformedFromRecord)
				.collect(Collectors.toList());
	}
	
	/**
	 * Updates the database record for the exercisePerformed specified.
	 * @return the number of lines changed.
	 */
	public static int updateExercisePerformed(ExercisePerformed exercisePerformed) {
		String update = "UPDATE exercise_performed SET workout_id = ?, exercise_id = ?, number_of_sets = ?, number_of_kilos = ? WHERE exercise_performed_id = ?"; 
		return DatabaseManager.executeUpdate(update, 
				exercisePerformed.getWorkout().getWorkoutID(),		// NOT NULL
				exercisePerformed.getExercise().getExerciseID(),	// NOT NULL
				exercisePerformed.getNumberOfSets(),
				exercisePerformed.getNumberOfKilos(),
				exercisePerformed.getExercisePerformedID());
	}
	
	/**
	 * Inserts a new database record corresponding to the given {@code exercisePerformed}.
	 * @return the auto-generated identifier.
	 */
	public static int insertExercisePerformedAssignID(ExercisePerformed exercisePerformed) {
		String insert = "INSERT INTO exercise_performed (workout_id, exercise_id, number_of_sets, number_of_kilos) VALUES (?, ?, ?, ?)";
		Integer exercisePerformedID = DatabaseManager.executeInsertGetID(insert,
				exercisePerformed.getWorkout().getWorkoutID(),		// NOT NULL
				exercisePerformed.getExercise().getExerciseID(),	// NOT NULL
				exercisePerformed.getNumberOfSets(),
				exercisePerformed.getNumberOfKilos());
		exercisePerformed.setExercisePerformedID(exercisePerformedID);
		return exercisePerformedID;
	}
	
	/**
	 * Inserts a new record for every relation entity in the list.
	 * @return the number of lines changed.
	 */
	public static int insertExercisePerformedRelations(List<ExercisePerformed> exercisePerformedRelations) {
		if (exercisePerformedRelations.isEmpty())
			return -1;
		String values = exercisePerformedRelations.stream()
				.map(ep -> String.format("(%s, %s, %s, %s)", 
						ep.getWorkout().getWorkoutID(),				// NOT NULL
						ep.getExercise().getExerciseID(),			// NOT NULL
						ep.getNumberOfSets(),
						ep.getNumberOfKilos()))
				.collect(Collectors.joining(", "));
		String update = String.format("INSERT INTO exercise_performed (workout_id, exercise_id, number_of_sets, number_of_kilos) VALUES %s",  values);
		return DatabaseManager.executeUpdate(update);	// Use update for creating normal query (No ID fetch)
	}
	
	/**
	 * Deletes all records related to the given workout.
	 * @return the number of lines changed.
	 */
	public static int deleteExercisePerformedRelationsByWorkout(Workout workout) {
		String delete = "DELETE FROM exercise_performed WHERE workout_id = ?";
		return DatabaseManager.executeUpdate(delete, workout.getWorkoutID());
	}
	
	/**
	 * Deletes the database record for the exercisePerformed specified.
	 * @return the number of lines changed.
	 */
	public static int deleteExercisePerformed(ExercisePerformed exercisePerformed) {
		String delete = "DELETE FROM exercise_performed WHERE exercise_performed_id = ?";
		return DatabaseManager.executeUpdate(delete, exercisePerformed.getExercisePerformedID());
	}
	
	/**
	 * Deletes the database record for every entity in the list.
	 * @return the number of lines changed.
	 */
	public static int deleteExercisePerformeds(List<ExercisePerformed> exercisePerformeds) {
		String parsedIDs = exercisePerformeds.stream()
				.map(e -> String.format("%s", e.getExercisePerformedID()))
				.collect(Collectors.joining(", ", "(", ")"));
		String delete = String.format("DELETE FROM exercise_performed WHERE exercise_performed_id IN %s", parsedIDs);
		return DatabaseManager.executeUpdate(delete);
	}
	
	/**
	 * Creates a new {@code ExercisePerformed} instance from the record specified.
	 */
	public static ExercisePerformed extractExercisePerformedFromRecord(Record record) {
		// Assert that record contains a valid instance of this class
		Integer exercisePerformedID = record.get(Integer.class, "exercise_performed.exercise_performed_id");
		if (exercisePerformedID == null)
			return null;

		Workout workout = WorkoutService.extractWorkoutFromRecord(record);
		Exercise exercise = ExerciseService.extractExerciseFromRecord(record);
	    Integer numberOfSets = record.get(Integer.class, "exercise_performed.number_of_sets");
	    Integer numberOfKilos = record.get(Integer.class, "exercise_performed.number_of_kilos");
		
	    return new ExercisePerformed(exercisePerformedID, workout, exercise, numberOfSets, numberOfKilos);
	}
}

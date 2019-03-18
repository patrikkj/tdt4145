package main.database.handlers;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Note;
import main.models.Workout;

public class WorkoutService {
	
	/**
	 * Returns a list containing every {@code Workout} entity in the database.
	 */
	public static List<Workout> getWorkouts() {
		String query = "SELECT * FROM workout AS a "
				+ "LEFT JOIN note AS b ON a.note_id = b.note_id";
		List<Record> records = DatabaseManager.executeQuery(query);
		return records.stream()
				.map(WorkoutService::extractWorkoutFromRecord)
				.collect(Collectors.toList());
	}

	/**
	 * Returns the {@code Workout} identified by the given {@code workoutID}.
	 */
	public static Workout getWorkoutByID(int workoutID) {
		String query = "SELECT * FROM workout WHERE workout_id = ?";
		List<Record> records = DatabaseManager.executeQuery(query, workoutID);
		return extractWorkoutFromRecord(records.get(0));
	}
		
	/**
	 * Updates the database record for the workout specified.
	 * @return the number of lines changed.
	 */
	public static int updateWorkout(Workout workout) {
		String update = "UPDATE workout SET timestamp = ?, duration = ?, shape = ?, performance = ?, note_id = ? WHERE workout_id = ?"; 
		return DatabaseManager.executeUpdate(update, 
				workout.getTimestamp(),
				workout.getDuration(),
				workout.getShape(),
				workout.getPerformance(),
				workout.getNote() != null ? workout.getNote().getNoteID() : null,
				workout.getWorkoutID());
	}
	
	/**
	 * Inserts a new database record corresponding to the given {@code workout}.
	 * @return the auto-generated identifier.
	 */
	public static int insertWorkoutAssignID(Workout workout) {
		String insert = "INSERT INTO workout (timestamp, duration, shape, performance, note_id) VALUES (?, ?, ?, ?, ?, ?)";
		int workoutID = DatabaseManager.executeInsertGetID(insert,
				workout.getTimestamp(),
				workout.getDuration(),
				workout.getShape(),
				workout.getPerformance(),
				workout.getNote() != null ? workout.getNote().getNoteID() : null);
		workout.setWorkoutID(workoutID);
		return workoutID;
	}
	
	/**
	 * Deletes the database record for the workout specified.
	 * @return the number of lines changed.
	 */
	public static int deleteWorkout(Workout workout) {
		String delete = "DELETE FROM workout WHERE workout_id = ?";
		return DatabaseManager.executeUpdate(delete, workout.getWorkoutID());
	}
	
	/**
	 * Deletes the database record for every entity in the list.
	 * @return the number of lines changed.
	 */
	public static int deleteWorkouts(List<Workout> workouts) {
		String parsedIDs = workouts.stream()
				.map(e -> String.format("'%s'", e.getWorkoutID()))
				.collect(Collectors.joining(", ", "(", ")"));
		String delete = String.format("DELETE FROM workout WHERE workout_id IN %s", parsedIDs);
		return DatabaseManager.executeUpdate(delete);
	}

	/**
	 * Creates a new {@code Workout} instance from the record specified.
	 */
	public static Workout extractWorkoutFromRecord(Record record) {
		// Assert that record contains a valid instance of this class
		Integer workoutID = record.get(Integer.class, "workout.workout_id");
		if (workoutID == null)
			return null;
		
	    Timestamp timestamp = record.get(Timestamp.class, "workout.timestamp");
	    Time duration = record.get(Time.class, "workout.duration");
	    int shape = record.get(Integer.class, "workout.shape");
	    int performance = record.get(Integer.class, "workout.performance");
	    Note note = NoteService.extractNoteFromRecord(record);
	    
		return new Workout(workoutID, timestamp, duration, shape, performance, note);
	}
	
	public static void main(String[] args) {
		insertWorkoutAssignID(new Workout(Timestamp.from(Instant.now()), Time.valueOf("02:32:15"), 8, 9, null));
	}
}

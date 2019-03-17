package main.database.handlers;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Note;
import main.models.Workout;

public class WorkoutQueryHandler {
	
	public static List<Workout> getWorkouts() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM workout;");
		return records.stream()
				.map(WorkoutQueryHandler::extractWorkoutFromRecord)
				.collect(Collectors.toList());
	}

	public static Workout getWorkoutByID(int workoutID) {
		String query = String.format("SELECT * FROM workout WHERE workout_id = '%s';", workoutID);
		List<Record> records = DatabaseManager.executeQuery(query);
		return extractWorkoutFromRecord(records.get(0));
	}
	
	public static Workout extractWorkoutFromRecord(Record record) {
	    int workoutID = record.get(Integer.class, "workout_id");
	    Date date = record.get(Date.class, "date");
	    Time time = record.get(Time.class, "time");
	    Time duration = record.get(Time.class, "duration");
	    int shape = record.get(Integer.class, "shape");
	    int performance = record.get(Integer.class, "performance");
	    Note note = NoteQueryHandler.extractNoteFromRecord(record);
	    
		return new Workout(workoutID, date, time, duration, shape, performance, note);
	}
}

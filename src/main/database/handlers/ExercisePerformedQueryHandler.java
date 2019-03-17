package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Exercise;
import main.models.Workout;
import main.relations.ExercisePerformed;

public class ExercisePerformedQueryHandler {

	public static List<ExercisePerformed> getExercisePerformedRelations() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM exercise_performed;");
		return records.stream()
				.map(ExercisePerformedQueryHandler::extractExercisePerformedFromRecord)
				.collect(Collectors.toList());
	}

	public static ExercisePerformed getExercisePerformedByID(int exercisePerformedID) {
		String query = String.format("SELECT * FROM exercise_performed WHERE exercise_performed_id = '%s';", exercisePerformedID);
		List<Record> records = DatabaseManager.executeQuery(query);
		return extractExercisePerformedFromRecord(records.get(0));
	}
	
	public static ExercisePerformed extractExercisePerformedFromRecord(Record record) {
		int exercisePerformedID = record.get(Integer.class, "exercise_performed_id");
		Workout workout = WorkoutQueryHandler.extractWorkoutFromRecord(record);
		Exercise exercise = ExerciseQueryHandler.extractExerciseFromRecord(record);
	    int numberOfSets = record.get(Integer.class, "number_of_sets");
	    int numberOfKilos = record.get(Integer.class, "number_of_kilos");
	    
		return new ExercisePerformed(exercisePerformedID, workout, exercise, numberOfSets, numberOfKilos);
	}
}

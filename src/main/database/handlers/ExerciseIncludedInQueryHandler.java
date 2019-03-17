package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Exercise;
import main.models.ExerciseGroup;
import main.relations.ExerciseIncludedIn;

public class ExerciseIncludedInQueryHandler {

	public static List<ExerciseIncludedIn> getExerciseIncludedInRelations() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM exercise_included_in;");
		return records.stream()
				.map(ExerciseIncludedInQueryHandler::extractExerciseIncludedInFromRecord)
				.collect(Collectors.toList());
	}

	public static ExerciseIncludedIn getExerciseIncludedInByID(int exerciseIncludedInID) {
		String query = String.format("SELECT * FROM exercise_included_in WHERE exercise_included_in_id = '%s';", exerciseIncludedInID);
		List<Record> records = DatabaseManager.executeQuery(query);
		return extractExerciseIncludedInFromRecord(records.get(0));
	}
	
	public static ExerciseIncludedIn extractExerciseIncludedInFromRecord(Record record) {
		int exerciseIncludedInID = record.get(Integer.class,  "exercise_included_in_id");
		Exercise exercise = ExerciseQueryHandler.extractExerciseFromRecord(record);
		ExerciseGroup exerciseGroup = ExerciseGroupQueryHandler.extractExerciseGroupFromRecord(record);
	    
		return new ExerciseIncludedIn(exerciseIncludedInID, exercise, exerciseGroup);
	}
}

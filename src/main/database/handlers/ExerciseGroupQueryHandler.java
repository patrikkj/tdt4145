package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.ExerciseGroup;

public class ExerciseGroupQueryHandler {

	public static List<ExerciseGroup> getExerciseGroups() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM exercise_group;");
		return records.stream()
				.map(ExerciseGroupQueryHandler::extractExerciseGroupFromRecord)
				.collect(Collectors.toList());
	}

	public static ExerciseGroup getExerciseGroupByID(int exerciseGroupID) {
		String query = String.format("SELECT * FROM exercise_group WHERE exercise_group_id = '%s';", exerciseGroupID);
		List<Record> records = DatabaseManager.executeQuery(query);
		return extractExerciseGroupFromRecord(records.get(0));
	}
	
	public static ExerciseGroup extractExerciseGroupFromRecord(Record record) {
	    int exerciseGroupID = record.get(Integer.class, "exercise_group_id");
	    String name = record.get(String.class, "name");
	    
		return new ExerciseGroup(exerciseGroupID, name);
	}
}

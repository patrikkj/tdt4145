package main.models.relations;

import main.models.Exercise;
import main.models.ExerciseGroup;

public class ExerciseIncludedIn {
	private int exerciseIncludedInID;
	private Exercise exercise;
	private ExerciseGroup exerciseGroup;
	
	
	public ExerciseIncludedIn() {
		this.exerciseIncludedInID = -1;
	}
	
	public ExerciseIncludedIn(Exercise exercise, ExerciseGroup exerciseGroup) {
		this(-1, exercise, exerciseGroup);
	}
	
	public ExerciseIncludedIn(int exerciseIncludedInID, Exercise exercise, ExerciseGroup exerciseGroup) {
		this.exerciseIncludedInID = exerciseIncludedInID;
		this.exercise = exercise;
		this.exerciseGroup = exerciseGroup;
	}


	
	public int getExerciseIncludedInID() {
		return exerciseIncludedInID;
	}

	public void setExerciseIncludedInID(int exerciseIncludedInID) {
		this.exerciseIncludedInID = exerciseIncludedInID;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public ExerciseGroup getExerciseGroup() {
		return exerciseGroup;
	}

	public void setExerciseGroup(ExerciseGroup exerciseGroup) {
		this.exerciseGroup = exerciseGroup;
	}
}

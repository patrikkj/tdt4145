package main.models.relations;

import main.models.Exercise;
import main.models.Workout;

public class ExercisePerformed {
	private int exercisePerformedID;
	private Workout workout;
	private Exercise exercise;
	private int numberOfSets;
	private int numberOfKilos;
	
	
	public ExercisePerformed() {
		this.exercisePerformedID = -1;
	}
	
	public ExercisePerformed(Workout workout, Exercise exercise, int numberOfSets, int numberOfKilos) {
		this(-1, workout, exercise, numberOfSets, numberOfKilos);
	}
	
	public ExercisePerformed(int exercisePerformedID, Workout workout, Exercise exercise, int numberOfSets, int numberOfKilos) {
		this.exercisePerformedID = exercisePerformedID;
		this.workout = workout;
		this.exercise = exercise;
		this.numberOfSets = numberOfSets;
		this.numberOfKilos = numberOfKilos;
	}


	public int getExercisePerformedID() {
		return exercisePerformedID;
	}

	public void setExercisePerformedID(int exercisePerformedID) {
		this.exercisePerformedID = exercisePerformedID;
	}

	public Workout getWorkout() {
		return workout;
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public int getNumberOfSets() {
		return numberOfSets;
	}

	public void setNumberOfSets(int numberOfSets) {
		this.numberOfSets = numberOfSets;
	}

	public int getNumberOfKilos() {
		return numberOfKilos;
	}

	public void setNumberOfKilos(int numberOfKilos) {
		this.numberOfKilos = numberOfKilos;
	}
}

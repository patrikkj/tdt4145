package main.models.relations;

import main.models.Exercise;
import main.models.Workout;

public class ExercisePerformed {
	private int exercisePerformedID;
	private Workout workout;
	private Exercise exercise;
	private Integer numberOfSets;
	private Integer numberOfKilos;
	
	
	public ExercisePerformed() {
		this.exercisePerformedID = -1;
	}
	
	public ExercisePerformed(Workout workout, Exercise exercise, Integer numberOfSets, Integer numberOfKilos) {
		this(-1, workout, exercise, numberOfSets, numberOfKilos);
	}
	
	public ExercisePerformed(int exercisePerformedID, Workout workout, Exercise exercise, Integer numberOfSets, Integer numberOfKilos) {
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

	public Integer getNumberOfSets() {
		return numberOfSets;
	}

	public void setNumberOfSets(Integer numberOfSets) {
		this.numberOfSets = numberOfSets;
	}

	public Integer getNumberOfKilos() {
		return numberOfKilos;
	}

	public void setNumberOfKilos(Integer numberOfKilos) {
		this.numberOfKilos = numberOfKilos;
	}
	
	@Override
	public String toString() {
		if (exercise.getEquipment() != null)
			return String.format("%s - [Sett: %s, Kilo: %s]", exercise.getName(), numberOfSets, numberOfKilos);
		return exercise.getName();
	}
}

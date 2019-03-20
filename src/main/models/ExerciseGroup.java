package main.models;

public class ExerciseGroup {
	private int exerciseGroupID;
	private String name;
	
	
	public ExerciseGroup() {
		this.exerciseGroupID = -1;
	}
	
	public ExerciseGroup(String name) {
		this(-1, name);
	}
	
	public ExerciseGroup(int exerciseGroupID, String name) {
		this.exerciseGroupID = exerciseGroupID;
		this.name = name;
	}

	
	public int getExerciseGroupID() {
		return exerciseGroupID;
	}

	public void setExerciseGroupID(int exerciseGroupID) {
		this.exerciseGroupID = exerciseGroupID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}

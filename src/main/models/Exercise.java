package main.models;

public class Exercise {
	private int exerciseID;
	private String name;
	private String description;
	private Equipment equipment;
	
	
	public Exercise() {
		this.exerciseID = -1;
	}
	
	public Exercise(String name, String description, Equipment equipment) {
		this(-1, name, description, equipment);
	}
	
	public Exercise(int exerciseID, String name, String description, Equipment equipment) {
		this.exerciseID = exerciseID;
		this.name = name;
		this.description = description;
		this.equipment = equipment;
	}

	
	public int getExerciseID() {
		return exerciseID;
	}

	public void setExerciseID(int exerciseID) {
		this.exerciseID = exerciseID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	@Override
	public String toString() {
		return String.format("Exercise [exerciseID=%s, name=%s, description=%s, equipment=%s]", exerciseID, name,
				description, equipment);
	}
}

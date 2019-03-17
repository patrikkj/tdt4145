package main.models;

public class Equipment {
	private int equipmentID;
	private String name;
	private String description;
	
	
	public Equipment() {
		this.equipmentID = -1;
	}
	
	public Equipment(String name, String description) {
		this(-1, name, description);
	}
	
	public Equipment(int equipmentID, String name, String description) {
		this.equipmentID = equipmentID;
		this.name = name;
		this.description = description;
	}

	
	public int getEquipmentID() {
		return equipmentID;
	}

	public void setEquipmentID(int equipmentID) {
		this.equipmentID = equipmentID;
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

	@Override
	public String toString() {
		return String.format("Equipment [equipmentID=%s, name=%s, description=%s]", equipmentID, name, description);
	}
}

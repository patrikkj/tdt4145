package main.models;

import java.sql.Time;
import java.sql.Timestamp;

public class Workout {
	private int workoutID;
	private Timestamp timestamp;
	private Time duration;
	private int shape;
	private int performance;
	private Note note;
	
	
	public Workout() {
		this.workoutID = -1;
	}

	public Workout(Timestamp timestamp, Time duration, int shape, int performance, Note note) {
		this(-1, timestamp, duration, shape, performance, note);
	}
	
	public Workout(int øktID, Timestamp timestamp, Time duration, int shape, int performance, Note note) {
		this.workoutID = øktID;
		this.timestamp = timestamp;
		this.duration = duration;
		this.shape = shape;
		this.performance = performance;
		this.note = note;
	}


	public int getWorkoutID() {
		return workoutID;
	}

	public void setWorkoutID(int workoutID) {
		this.workoutID = workoutID;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Time getDuration() {
		return duration;
	}

	public void setDuration(Time duration) {
		this.duration = duration;
	}

	public int getShape() {
		return shape;
	}

	public void setShape(int shape) {
		this.shape = shape;
	}

	public int getPerformance() {
		return performance;
	}

	public void setPerformance(int performance) {
		this.performance = performance;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return String.format("Workout [workoutID=%s, timestamp=%s, duration=%s, shape=%s, performance=%s, note=%s]",
				workoutID, timestamp, duration, shape, performance, note);
	}
}

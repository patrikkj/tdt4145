package main.models;

import java.sql.Date;
import java.sql.Time;

public class Workout {
	private int workoutID;
	private Date date;
	private Time time;
	private Time duration;
	private int shape;
	private int performance;
	private Note note;
	
	
	public Workout() {
		this.workoutID = -1;
	}

	public Workout(Date date, Time time, Time duration, int shape, int performance, Note note) {
		this(-1, date, time, duration, shape, performance, note);
	}
	
	public Workout(int øktID, Date date, Time time, Time duration, int shape, int performance, Note note) {
		this.workoutID = øktID;
		this.date = date;
		this.time = time;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
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
}

package main.models;

public class Note {
	private int noteID;
	private String text;
	
	
	public Note() {
		this.noteID = -1;
	}
	
	public Note(String text) {
		this(-1, text);
	}
	
	public Note(int noteID, String text) {
		this.noteID = noteID;
		this.text = text;
	}


	public int getNoteID() {
		return noteID;
	}

	public void setNoteID(int noteID) {
		this.noteID = noteID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

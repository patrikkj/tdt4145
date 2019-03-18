package main.models;

public class Note {
	private int noteID;
	private String title;
	private String text;
	
	
	public Note() {
		this.noteID = -1;
	}
	
	public Note(String title, String text) {
		this(-1, title, text);
	}
	
	public Note(int noteID, String title, String text) {
		this.noteID = noteID;
		this.title = title;
		this.text = text;
	}


	public int getNoteID() {
		return noteID;
	}

	public void setNoteID(int noteID) {
		this.noteID = noteID;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format("Note [noteID=%s, text=%s]", noteID, text);
	}
}

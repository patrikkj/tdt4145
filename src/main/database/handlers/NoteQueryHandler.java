package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Note;

public class NoteQueryHandler {
	
	/**
	 * Returns a list containing every {@code Note} entity in the database.
	 */
	public static List<Note> getNotes() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM note");
		return records.stream()
				.map(NoteQueryHandler::extractNoteFromRecord)
				.collect(Collectors.toList());
	}

	/**
	 * Returns the {@code Note} identified by the given {@code noteID}.
	 */
	public static Note getNoteByID(int noteID) {
		String query = "SELECT * FROM note WHERE note_id = ?";
		List<Record> records = DatabaseManager.executeQuery(query, noteID);
		return extractNoteFromRecord(records.get(0));
	}
		
	/**
	 * Updates the database record for the note specified.
	 * @return the number of lines changed.
	 */
	public static int updateNote(Note note) {
		String update = "UPDATE note SET text = ? WHERE note_id = ?"; 
		return DatabaseManager.executeUpdate(update, 
				note.getText(),
				note.getNoteID());
	}
	
	/**
	 * Inserts a new database record corresponding to the given {@code note}.
	 * @return the auto-generated identifier.
	 */
	public static int insertNoteAssignID(Note note) {
		String insert = "INSERT INTO note (text) VALUES (?)";
		int noteID = DatabaseManager.executeInsertGetID(insert,
				note.getText());
		note.setNoteID(noteID);
		return noteID;
	}
	
	/**
	 * Deletes the database record for the note specified.
	 * @return the number of lines changed.
	 */
	public static int deleteNote(Note note) {
		String delete = "DELETE FROM note WHERE note_id = ?";
		return DatabaseManager.executeUpdate(delete, note.getNoteID());
	}
	
	/**
	 * Deletes the database record for every entity in the list.
	 * @return the number of lines changed.
	 */
	public static int deleteNotes(List<Note> notes) {
		String parsedIDs = notes.stream()
				.map(e -> String.format("'%s'", e.getNoteID()))
				.collect(Collectors.joining(", ", "(", ")"));
		String delete = String.format("DELETE FROM note WHERE note_id IN %s", parsedIDs);
		return DatabaseManager.executeUpdate(delete);
	}
	
	/**
	 * Creates a new {@code Note} instance from the record specified.
	 */
	public static Note extractNoteFromRecord(Record record) {
		// Assert that record contains a valid instance of this class
		Integer noteID = record.get(Integer.class, "note.note_id");
		if (noteID == null)
			return null;
		
	    String text = record.get(String.class, "note.text");
	    
		return new Note(noteID, text);
	}
}

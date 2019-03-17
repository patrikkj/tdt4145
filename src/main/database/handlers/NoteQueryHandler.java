package main.database.handlers;

import java.util.List;
import java.util.stream.Collectors;

import main.database.DatabaseManager;
import main.database.Record;
import main.models.Note;

public class NoteQueryHandler {
	
	public static List<Note> getNotes() {
		List<Record> records = DatabaseManager.executeQuery("SELECT * FROM note;");
		return records.stream()
				.map(NoteQueryHandler::extractNoteFromRecord)
				.collect(Collectors.toList());
	}

	public static Note getNoteByID(int noteID) {
		String query = String.format("SELECT * FROM note WHERE note_id = '%s';", noteID);
		List<Record> records = DatabaseManager.executeQuery(query);
		return extractNoteFromRecord(records.get(0));
	}
	
	public static Note extractNoteFromRecord(Record record) {
	    int noteID = record.get(Integer.class, "note_id");
	    String text = record.get(String.class, "text");
	    
		return new Note(noteID, text);
	}
}

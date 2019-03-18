package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.database.handlers.NoteService;
import main.models.Note;

public class NotePopupController extends AbstractPopupController<Note> {
	@FXML private Label headerLabel;
    @FXML private JFXTextField titleTextField;
    @FXML private JFXTextArea textTextArea;
    @FXML private JFXButton actionButton;
    private Note note;

	@Override
	public void loadCreateMode() {
		setMode(Mode.CREATE);
		headerLabel.setText("Nytt notat");
		actionButton.setText("Registrer");
		
		// Create empty model
    	this.note = new Note();
	}
	
	@Override
	public void loadEditMode(Note note) {
    	setMode(Mode.EDIT);
    	headerLabel.setText("Endre notat");
    	actionButton.setText("Rediger");
    	
    	// Save model reference
    	this.note = note;
    	
    	// Load data
    	titleTextField.setText(note.getTitle() != null ? note.getTitle() : "");
    	textTextArea.setText(note.getText() != null ? note.getText() : "");
	}

	@Override
	protected void updateModelFromInput() {
    	note.setTitle(titleTextField.getText());
		note.setText(textTextArea.getText());
	}

	@Override
	public void clear() {
		titleTextField.clear();		
		textTextArea.clear();
	}

    @FXML
    void handleActionClick(ActionEvent event) {
    	updateModelFromInput();
    	switch (mode) {
		case CREATE:
			NoteService.insertNoteAssignID(note);
			break;
		case EDIT:
			NoteService.updateNote(note);
			break;
		}
    	dialog.close();
    }
}

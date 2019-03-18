package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.database.handlers.ExerciseGroupService;
import main.models.ExerciseGroup;

public class ExerciseGroupPopupController extends AbstractPopupController<ExerciseGroup> {
	@FXML private Label headerLabel;
    @FXML private JFXTextField nameTextField;
    @FXML private JFXButton actionButton;
    private ExerciseGroup exerciseGroup;

	@Override
	public void loadCreateMode() {
		setMode(Mode.CREATE);
		headerLabel.setText("Ny øvelsesgruppe");
		actionButton.setText("Registrer");
		
		// Create empty model
    	this.exerciseGroup = new ExerciseGroup();
	}
	
	@Override
	public void loadEditMode(ExerciseGroup exerciseGroup) {
    	setMode(Mode.EDIT);
    	headerLabel.setText("Endre øvelsesgruppe");
    	actionButton.setText("Rediger");
    	
    	// Save model reference
    	this.exerciseGroup = exerciseGroup;
    	
    	// Load data
    	nameTextField.setText(exerciseGroup.getName() != null ? exerciseGroup.getName() : "");
	}

	@Override
	protected void updateModelFromInput() {
    	exerciseGroup.setName(nameTextField.getText());
	}

	@Override
	public void clear() {
		nameTextField.clear();		
	}

    @FXML
    void handleActionClick(ActionEvent event) {
    	updateModelFromInput();
    	switch (mode) {
		case CREATE:
			ExerciseGroupService.insertExerciseGroupAssignID(exerciseGroup);
			break;
		case EDIT:
			ExerciseGroupService.updateExerciseGroup(exerciseGroup);
			break;
		}
    	dialog.close();
    }
}

package main.core.ui.popups;

import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.database.handlers.EquipmentService;
import main.database.handlers.ExerciseService;
import main.models.Equipment;
import main.models.Exercise;
import main.models.ExerciseGroup;

public class ExercisePopupController extends AbstractPopupController<Exercise> {
    @FXML private Label headerLabel;
    @FXML private JFXTextField nameTextField;
    @FXML private JFXTextArea descriptionTextArea;
    @FXML private JFXComboBox<Equipment> equipmentComboBox;
    @FXML private JFXComboBox<ExerciseGroup> exerciseGroupComboBox;
    @FXML private JFXButton addExerciseGroupButton;
    @FXML private JFXButton deleteExerciseGroupButton;
    @FXML private JFXListView<?> exerciseGroupListView;
    @FXML private JFXButton actionButton;

    private Exercise exercise;

	@Override
	public void loadCreateMode() {
		setMode(Mode.CREATE);
		headerLabel.setText("Ny øvelse");
		actionButton.setText("Registrer");
		
		// Fill combobox
    	List<Equipment> equipments = EquipmentService.getEquipments();
    	equipmentComboBox.getItems().setAll(equipments);
    	
		// Create empty model
    	this.exercise = new Exercise();
	}
	
	@Override
	public void loadEditMode(Exercise exercise) {
    	setMode(Mode.EDIT);
    	headerLabel.setText("Endre øvelse");
    	actionButton.setText("Rediger");
    	
    	// Save model reference
    	this.exercise = exercise;
    	
    	// Fill combobox
    	List<Equipment> equipments = EquipmentService.getEquipments();
    	equipmentComboBox.getItems().setAll(equipments);
    	
    	// Find equipment for selection
    	Equipment equipmentFromExercise = exercise.getEquipment() == null ? null :
    			equipments.stream()
	    			.filter(e -> e.getEquipmentID() == exercise.getEquipment().getEquipmentID())
	    			.findFirst()
	    			.orElse(null);
    	
    	// Load data
    	nameTextField.setText(exercise.getName() != null ? exercise.getName() : "");
    	descriptionTextArea.setText(exercise.getDescription() != null ? exercise.getDescription() : "");
    	equipmentComboBox.getSelectionModel().select(equipmentFromExercise);
	}

	@Override
	protected void updateModelFromInput() {
		exercise.setName(nameTextField.getText());
		exercise.setDescription(descriptionTextArea.getText());
		exercise.setEquipment(equipmentComboBox.getSelectionModel().getSelectedItem());
	}

	@Override
	public void clear() {
		nameTextField.clear();		
		descriptionTextArea.clear();
		equipmentComboBox.getSelectionModel().clearSelection();
		equipmentComboBox.getItems().clear();
	}


    @FXML
    void handleAddExerciseGroupClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteExerciseGroupClick(ActionEvent event) {

    }
	
    @FXML
    void handleActionClick(ActionEvent event) {
    	updateModelFromInput();
    	switch (mode) {
		case CREATE:
			ExerciseService.insertExerciseAssignID(exercise);
			break;
		case EDIT:
			ExerciseService.updateExercise(exercise);
			break;
		}
    	dialog.close();
    }
}

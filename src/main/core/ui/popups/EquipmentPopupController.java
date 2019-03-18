package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.database.handlers.EquipmentService;
import main.models.Equipment;

public class EquipmentPopupController extends AbstractPopupController<Equipment> {
	@FXML private Label headerLabel;
    @FXML private JFXTextField nameTextField;
    @FXML private JFXTextArea descriptionTextArea;
    @FXML private JFXButton actionButton;
    private Equipment equipment;

	@Override
	public void loadCreateMode() {
		setMode(Mode.CREATE);
		headerLabel.setText("Nytt apparat");
		actionButton.setText("Registrer");
		
		// Create empty model
    	this.equipment = new Equipment();
	}
	
	@Override
	public void loadEditMode(Equipment equipment) {
    	setMode(Mode.EDIT);
    	headerLabel.setText("Endre apparat");
    	actionButton.setText("Rediger");
    	
    	// Save model reference
    	this.equipment = equipment;
    	
    	// Load data
    	nameTextField.setText(equipment.getName() != null ? equipment.getName() : "");
    	descriptionTextArea.setText(equipment.getDescription() != null ? equipment.getDescription() : "");
	}

	@Override
	protected void updateModelFromInput() {
    	equipment.setName(nameTextField.getText());
		equipment.setDescription(descriptionTextArea.getText());
	}

	@Override
	public void clear() {
		nameTextField.clear();		
		descriptionTextArea.clear();
	}

    @FXML
    void handleActionClick(ActionEvent event) {
    	updateModelFromInput();
    	switch (mode) {
		case CREATE:
			EquipmentService.insertEquipmentAssignID(equipment);
			break;
		case EDIT:
			EquipmentService.updateEquipment(equipment);
			break;
		}
    	dialog.close();
    }
}

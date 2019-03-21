package main.core.ui.popups;

import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.util.StringConverter;
import main.database.handlers.EquipmentService;
import main.database.handlers.ExerciseGroupService;
import main.database.handlers.ExerciseService;
import main.database.handlers.relations.ExerciseIncludedInService;
import main.models.Equipment;
import main.models.Exercise;
import main.models.ExerciseGroup;
import main.models.relations.ExerciseIncludedIn;

public class ExercisePopupController extends AbstractPopupController<Exercise> {
    @FXML private Label headerLabel;
    @FXML private JFXTextField nameTextField;
    @FXML private JFXTextArea descriptionTextArea;
    @FXML private JFXComboBox<Equipment> equipmentComboBox;
    @FXML private JFXComboBox<ExerciseGroup> exerciseGroupComboBox;
    @FXML private JFXButton addExerciseGroupButton;
    @FXML private JFXButton deleteExerciseGroupButton;
    @FXML private JFXListView<ExerciseIncludedIn> exerciseGroupListView;
    @FXML private JFXButton actionButton;
    
    private IntegerBinding exerciseGroupSelectionSize;

    private Exercise exercise;
    
    @FXML
    private void initialize() {
		// Disable add button binding
		addExerciseGroupButton.disableProperty().bind(exerciseGroupComboBox.valueProperty().isNull());
		
		// Disable delete button binding and permit multi-select
    	exerciseGroupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	exerciseGroupSelectionSize = Bindings.size(exerciseGroupListView.getSelectionModel().getSelectedItems());
    	deleteExerciseGroupButton.disableProperty().bind(exerciseGroupSelectionSize.isEqualTo(0));
    	
    	// Combo box conversion
    	equipmentComboBox.setConverter(new StringConverter<Equipment>() {
    		@Override
    		public String toString(Equipment equipment) {
    			return equipment != null ? equipment.getName() : "";
    		}
    		
    		@Override
    		public Equipment fromString(String string) {
    			return equipmentComboBox.getItems().stream()
    					.filter(e -> e.getName().equalsIgnoreCase(string))
    					.findFirst()
    					.orElse(null);
    		}
    	});
    	
    	// Combo box conversion
    	exerciseGroupComboBox.setConverter(new StringConverter<ExerciseGroup>() {
			@Override
			public String toString(ExerciseGroup exerciseGroup) {
				return exerciseGroup != null ? exerciseGroup.getName() : "";
			}
			
			@Override
			public ExerciseGroup fromString(String string) {
				return exerciseGroupComboBox.getItems().stream()
						.filter(e -> e.getName().equalsIgnoreCase(string))
						.findFirst()
						.orElse(null);
			}
		});
    }
    
	@Override
	public void loadCreateMode() {
		setMode(Mode.CREATE);
		headerLabel.setText("Ny øvelse");
		actionButton.setText("Registrer");
		
		// Fill combobox
    	List<Equipment> equipments = EquipmentService.getEquipments();
    	equipmentComboBox.getItems().setAll(equipments);
    	
    	// Fill ListView
    	exerciseGroupListView.getItems().clear();
    	
    	// Fill Exercise combobox
    	List<ExerciseGroup> exerciseGroups = ExerciseGroupService.getExerciseGroups();
    	exerciseGroupComboBox.getItems().setAll(exerciseGroups);
    	
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
    	
    	// Fill Equipment combobox
    	List<Equipment> equipments = EquipmentService.getEquipments();
    	equipmentComboBox.getItems().setAll(equipments);
    	
    	// Fill ListView
    	List<ExerciseIncludedIn> exerciseGroupRelationsInExercise = ExerciseIncludedInService.getExerciseIncludedInRelationsByExercise(exercise);
    	exerciseGroupListView.getItems().setAll(exerciseGroupRelationsInExercise);
    	
    	// Fill ExerciseGroup combobox
    	List<ExerciseGroup> exerciseGroupsNotInExercise = ExerciseGroupService.getExerciseGroupsNotInExercise(exercise);
    	exerciseGroupComboBox.getItems().setAll(exerciseGroupsNotInExercise);
    	
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
		exercise.setEquipment(equipmentComboBox.getValue());
	}

	@Override
	public void clear() {
		nameTextField.clear();		
		descriptionTextArea.clear();
		equipmentComboBox.getSelectionModel().clearSelection();
		equipmentComboBox.getItems().clear();
		exerciseGroupComboBox.getItems().clear();
		exerciseGroupListView.getItems().clear();
	}


    @FXML
    void handleAddExerciseGroupClick(ActionEvent event) {
    	ExerciseGroup exerciseGroup = exerciseGroupComboBox.getValue();
    	
    	// Add to ListView
    	exerciseGroupListView.getItems().add(new ExerciseIncludedIn(null, exerciseGroup));
    	
    	// Remove from ComboBox
    	exerciseGroupComboBox.getItems().remove(exerciseGroup);
    	exerciseGroupComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void handleDeleteExerciseGroupClick(ActionEvent event) {
    	List<ExerciseIncludedIn> selectedExerciseIncludedInRelations = exerciseGroupListView.getSelectionModel().getSelectedItems();
    	List<ExerciseGroup> exerciseGroupsFromRelation = selectedExerciseIncludedInRelations.stream()
    			.map(ExerciseIncludedIn::getExerciseGroup)
    			.collect(Collectors.toList());
    	exerciseGroupListView.getItems().removeAll(selectedExerciseIncludedInRelations);
    	exerciseGroupComboBox.getItems().addAll(exerciseGroupsFromRelation);
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
			
			// Delete old relations
			ExerciseIncludedInService.deleteExerciseIncludedInRelationsByExercise(exercise);
			break;
		}
    	
    	// Configure and insert new relations
    	List<ExerciseIncludedIn> exerciseIncludedInRelations = exerciseGroupListView.getItems();
    	exerciseIncludedInRelations.forEach(eii -> eii.setExercise(exercise));
    	ExerciseIncludedInService.insertExerciseIncludedInRelations(exerciseIncludedInRelations);

    	dialog.close();
    }
}

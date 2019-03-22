package main.core.ui.popups;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.NumberValidator;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.util.StringConverter;
import main.database.handlers.ExerciseService;
import main.database.handlers.NoteService;
import main.database.handlers.WorkoutService;
import main.database.handlers.relations.ExercisePerformedService;
import main.models.Exercise;
import main.models.Note;
import main.models.Workout;
import main.models.relations.ExercisePerformed;

public class WorkoutPopupController extends AbstractPopupController<Workout> {
    @FXML private Label headerLabel;

    // Left view
    @FXML private JFXDatePicker dateDatePicker;
    @FXML private JFXTimePicker timeTimePicker;
    @FXML private JFXTimePicker durationTimePicker;
    @FXML private JFXSlider shapeSlider;
    @FXML private JFXSlider performanceSlider;
    @FXML private JFXComboBox<Note> noteComboBox;

    // Right view
    @FXML private JFXButton addExercisePerformedButton;
    @FXML private JFXButton deleteExercisePerformedButton;
    @FXML private JFXListView<ExercisePerformed> exerciseListView;
    @FXML private JFXComboBox<Exercise> exerciseComboBox;
    @FXML private JFXTextField numberOfSetsTextField;
    @FXML private JFXTextField numberOfKilosTextField;

    @FXML private JFXButton actionButton;
    private Workout workout;
    
    // Validation
    private NumberValidator numberOfSetsValidator;
    private NumberValidator numberOfKilosValidator;
    
    // Properties
    private BooleanBinding isValidExerciseProperty;
    private ReadOnlyBooleanProperty numberOfSetsErrorProperty;
    private ReadOnlyBooleanProperty numberOfKilosErrorProperty;
    private BooleanBinding numberOfSetsIsEmpty;
    private BooleanBinding numberOfKilosIsEmpty;
    private SimpleBooleanProperty valuesRequired;
    private IntegerBinding exerciseSelectionSize;

    
    @FXML
    private void initialize() {
    	timeTimePicker.set24HourView(true);
    	durationTimePicker.set24HourView(true);
    	valuesRequired = new SimpleBooleanProperty();
    	exerciseComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
    		if (newValue == null  ||  newValue.getEquipment() == null) {
    			numberOfSetsTextField.setDisable(true);
    			numberOfKilosTextField.setDisable(true);
    			numberOfSetsTextField.clear();
    			numberOfKilosTextField.clear();
    			valuesRequired.set(false);
    		} else {
    			numberOfSetsTextField.setDisable(false);
    			numberOfKilosTextField.setDisable(false);
    			valuesRequired.set(true);
    		}
    	});
    	
    	numberOfSetsValidator = new NumberValidator();
    	numberOfSetsValidator.setMessage("Dette feltet må være et tall.");
		numberOfSetsTextField.getValidators().add(numberOfSetsValidator);
		numberOfSetsTextField.textProperty().addListener((obs, oldValue, newValue) -> {
			numberOfSetsTextField.validate();
		});

		numberOfKilosValidator = new NumberValidator();
		numberOfKilosValidator.setMessage("Dette feltet må være et tall.");
		numberOfKilosTextField.getValidators().add(numberOfSetsValidator);
		numberOfKilosTextField.textProperty().addListener((obs, oldValue, newValue) -> {
			numberOfKilosTextField.validate();
		});
		
		isValidExerciseProperty = exerciseComboBox.valueProperty().isNotNull();
		numberOfSetsErrorProperty = numberOfSetsValidator.hasErrorsProperty();
		numberOfKilosErrorProperty = numberOfKilosValidator.hasErrorsProperty();
		
		numberOfSetsIsEmpty = numberOfSetsTextField.textProperty().isEqualTo("");
	    numberOfKilosIsEmpty = numberOfKilosTextField.textProperty().isEqualTo("");
		
		// Disable add button binding
		addExercisePerformedButton.disableProperty().bind(
				isValidExerciseProperty.not()
				.or(numberOfSetsErrorProperty)
				.or(numberOfKilosErrorProperty)
				.or(valuesRequired
						.and(numberOfSetsIsEmpty
								.or(numberOfKilosIsEmpty))));
		

    	// Combo box conversion
    	noteComboBox.setConverter(new StringConverter<Note>() {
			@Override
			public String toString(Note note) {
				return note != null ? note.getTitle() : "";
			}
			
			@Override
			public Note fromString(String string) {
				return noteComboBox.getItems().stream()
						.filter(n -> n.getTitle().equalsIgnoreCase(string))
						.findFirst()
						.orElse(null);
			}
		});
    	
    	// Combo box conversion
    	exerciseComboBox.setConverter(new StringConverter<Exercise>() {
			@Override
			public String toString(Exercise exercise) {
				return exercise != null ? exercise.getName() : "";
			}
			
			@Override
			public Exercise fromString(String string) {
				return exerciseComboBox.getItems().stream()
						.filter(e -> e.getName().equalsIgnoreCase(string))
						.findFirst()
						.orElse(null);
			}
		});
		
		
		// Disable delete button binding and permit multi-select
    	exerciseListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	exerciseSelectionSize = Bindings.size(exerciseListView.getSelectionModel().getSelectedItems());
    	deleteExercisePerformedButton.disableProperty().bind(exerciseSelectionSize.isEqualTo(0));
    }

	@Override
	public void loadCreateMode() {
		setMode(Mode.CREATE);
		headerLabel.setText("Ny treningsøkt");
		actionButton.setText("Registrer");
		
		// Fill Note combobox
    	List<Note> notes = NoteService.getNotes();
    	noteComboBox.getItems().setAll(notes);
    	
    	// Fill ListView
    	exerciseListView.getItems().clear();
    	
    	// Fill Exercise combobox
    	List<Exercise> exercises = ExerciseService.getExercises();
    	exerciseComboBox.getItems().setAll(exercises);
    	
		// Create empty model
    	this.workout = new Workout();
	}
	
	@Override
	public void loadEditMode(Workout workout) {
    	setMode(Mode.EDIT);
    	headerLabel.setText("Endre treningsøkt");
    	actionButton.setText("Rediger");
    	
    	// Save model reference
    	this.workout = workout;
    	
    	// Fill Note combobox
    	List<Note> notes = NoteService.getNotes();
    	noteComboBox.getItems().setAll(notes);
    	
    	// Fill ListView
    	List<ExercisePerformed> exercisesRelationsInWorkout = ExercisePerformedService.getExercisePerformedRelationsByWorkout(workout);
    	exerciseListView.getItems().setAll(exercisesRelationsInWorkout);
    	
    	// Fill Exercise combobox
    	List<Exercise> exercisesNotInWorkout = ExerciseService.getExercisesNotInWorkout(workout);
    	exerciseComboBox.getItems().setAll(exercisesNotInWorkout);
    	
    	// Find note for selection
    	Note noteFromWorkout = workout.getNote() == null ? null :
    			notes.stream()
	    			.filter(n -> n.getNoteID() == workout.getNote().getNoteID())
	    			.findFirst()
	    			.orElse(null);
    	
    	// Load data
    	dateDatePicker.setValue(workout.getTimestamp() != null ? workout.getTimestamp().toLocalDateTime().toLocalDate() : null);
    	timeTimePicker.setValue(workout.getTimestamp() != null ? workout.getTimestamp().toLocalDateTime().toLocalTime() : null);
    	durationTimePicker.setValue(workout.getDuration() != null ? workout.getDuration().toLocalTime() : null);
    	shapeSlider.setValue(workout.getShape() != null ? workout.getShape() : 5);
    	performanceSlider.setValue(workout.getPerformance());
    	noteComboBox.getSelectionModel().select(noteFromWorkout);
	}

	@Override
	protected void updateModelFromInput() {
		LocalDate date = dateDatePicker.getValue();
		LocalTime time = timeTimePicker.getValue();
		LocalDateTime dateTime = date != null && time != null ? LocalDateTime.of(date, time) : null;
		Timestamp timestamp = dateTime != null ? Timestamp.valueOf(dateTime) : null;
		
		LocalTime duration = durationTimePicker.getValue();
		
		workout.setTimestamp(timestamp);
		workout.setDuration(duration != null ? Time.valueOf(duration) : null);
		workout.setShape((int) shapeSlider.getValue());
		workout.setPerformance((int) performanceSlider.getValue());
		workout.setNote(noteComboBox.getValue());
	}

	@Override
	public void clear() {
		dateDatePicker.setValue(null);
    	timeTimePicker.setValue(null);
    	durationTimePicker.setValue(null);
    	shapeSlider.setValue(5);;
    	performanceSlider.setValue(5);
    	noteComboBox.getSelectionModel().clearSelection();
    	noteComboBox.getItems().clear();
    	exerciseComboBox.getItems().clear();
    	exerciseListView.getItems().clear();
    	numberOfSetsTextField.clear();
    	numberOfKilosTextField.clear();
	}

    @FXML
    void handleAddExercisePerformedClick(ActionEvent event) {
    	Exercise exercise = exerciseComboBox.getValue();
    	
    	// Read count value
    	Integer numberOfSets = !numberOfSetsTextField.getText().equals("") ? Integer.parseInt(numberOfSetsTextField.getText()) : null;
    	Integer numberOfKilos = !numberOfKilosTextField.getText().equals("") ? Integer.parseInt(numberOfKilosTextField.getText()) : null;
    	
    	// Add to ListView
    	exerciseListView.getItems().add(new ExercisePerformed(null, exercise, numberOfSets, numberOfKilos));
    	
    	// Remove from ComboBox
    	exerciseComboBox.getItems().remove(exercise);
    	exerciseComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void handleDeleteExercisePerformedClick(ActionEvent event) {
    	List<ExercisePerformed> selectedExercisePerformedRelations = exerciseListView.getSelectionModel().getSelectedItems();
    	List<Exercise> exercisesFromRelation = selectedExercisePerformedRelations.stream()
    			.map(ExercisePerformed::getExercise)
    			.collect(Collectors.toList());
    	exerciseListView.getItems().removeAll(selectedExercisePerformedRelations);
    	exerciseComboBox.getItems().addAll(exercisesFromRelation);
    }

    @FXML
    void handleActionClick(ActionEvent event) {
    	updateModelFromInput();
    	switch (mode) {
		case CREATE:
			WorkoutService.insertWorkoutAssignID(workout);
			break;
		case EDIT:
			WorkoutService.updateWorkout(workout);
			
			// Delete old relations
			ExercisePerformedService.deleteExercisePerformedRelationsByWorkout(workout);
			break;
		}

    	// Configure and insert new relations
    	List<ExercisePerformed> exercisePerformedRelations = exerciseListView.getItems();
    	exercisePerformedRelations.forEach(ep -> ep.setWorkout(workout));
    	ExercisePerformedService.insertExercisePerformedRelations(exercisePerformedRelations);

    	dialog.close();
    }
}

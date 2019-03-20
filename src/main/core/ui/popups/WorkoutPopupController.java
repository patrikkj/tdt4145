package main.core.ui.popups;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.database.handlers.NoteService;
import main.database.handlers.WorkoutService;
import main.models.Exercise;
import main.models.Note;
import main.models.Workout;
import main.models.relations.ExercisePerformed;

public class WorkoutPopupController extends AbstractPopupController<Workout> {
    @FXML private Label headerLabel;
    @FXML private JFXDatePicker dateDatePicker;
    @FXML private JFXTimePicker timeTimePicker;
    @FXML private JFXTimePicker durationTimePicker;
    @FXML private JFXSlider shapeSlider;
    @FXML private JFXSlider performanceSlider;
    @FXML private JFXComboBox<Note> noteComboBox;
    @FXML private JFXButton addExercisePerformedButton;
    @FXML private JFXButton deleteExercisePerformedButton;
    @FXML private JFXComboBox<Exercise> exerciseomboBox;
    @FXML private JFXListView<ExercisePerformed> exercisePerformedListView;
    @FXML private JFXButton actionButton;


    @FXML private JFXTextField numberOfSetsTextField;
    @FXML private JFXTextField numberOfKilosTextField;


    private Workout workout;


	@Override
	public void loadCreateMode() {
		setMode(Mode.CREATE);
		headerLabel.setText("Ny treningsøkt");
		actionButton.setText("Registrer");
		
		// Fill combobox
    	List<Note> notes = NoteService.getNotes();
    	noteComboBox.getItems().setAll(notes);
    	
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
    	
    	// Fill combobox
    	List<Note> notes = NoteService.getNotes();
    	noteComboBox.getItems().setAll(notes);
    	
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
    	shapeSlider.setValue(workout.getShape());
    	performanceSlider.setValue(workout.getPerformance());
    	noteComboBox.getSelectionModel().select(noteFromWorkout);
	}

	@Override
	protected void updateModelFromInput() {
		LocalDate date = dateDatePicker.getValue();
		LocalTime time = timeTimePicker.getValue();
		LocalDateTime dateTime = date != null && time != null ? LocalDateTime.of(date, time) : null;
		Timestamp timestamp = dateTime != null ? Timestamp.valueOf(dateTime) : null;
		
		System.out.println(date);
		System.out.println(time);
		System.out.println(dateTime);
		System.out.println(timestamp);
		
		LocalTime duration = durationTimePicker.getValue();
		
		workout.setTimestamp(timestamp);
		workout.setDuration(duration != null ? Time.valueOf(duration) : null);
		workout.setShape((int) shapeSlider.getValue());
		workout.setPerformance((int) performanceSlider.getValue());
		workout.setNote(noteComboBox.getSelectionModel().getSelectedItem());
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
    	
	}
	

    @FXML
    void handleAddExercisePerformedClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteExercisePerformedClick(ActionEvent event) {

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
			break;
		}
    	dialog.close();
    }
}

package main.core.ui;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import main.database.handlers.EquipmentQueryHandler;
import main.database.handlers.ExerciseGroupQueryHandler;
import main.database.handlers.ExerciseQueryHandler;
import main.database.handlers.NoteQueryHandler;
import main.database.handlers.WorkoutQueryHandler;
import main.models.Equipment;
import main.models.Exercise;
import main.models.ExerciseGroup;
import main.models.Note;
import main.models.Workout;

public class MainController {
	// Equipment
	@FXML private JFXButton addEquipmentButton;
    @FXML private JFXButton editEquipmentButton;
    @FXML private JFXButton deleteEquipmentButton;
    @FXML private JFXListView<Equipment> equipmentListView;
    
    // Exercise
    @FXML private JFXButton addExerciseButton;
    @FXML private JFXButton editExerciseButton;
    @FXML private JFXButton deleteExerciseButton;
    @FXML private JFXListView<Exercise> exerciseListView;
    
    // ExerciseGroup
    @FXML private JFXButton addExerciseGroupButton;
    @FXML private JFXButton editExerciseGroupButton;
    @FXML private JFXButton deleteExerciseGroupButton;
    @FXML private JFXListView<ExerciseGroup> exerciseGroupListView;
    
    // Workout
    @FXML private JFXButton addWorkoutButton;
    @FXML private JFXButton editWorkoutButton;
    @FXML private JFXButton deleteWorkoutButton;
    @FXML private JFXListView<Workout> workoutListView;
    
    // Note
    @FXML private JFXButton addNoteButton;
    @FXML private JFXButton editNoteButton;
    @FXML private JFXButton deleteNoteButton;
    @FXML private JFXListView<Note> noteListView;
    
    // Workout Log
    private ObservableList<WorkoutTreeData> workoutTreeData;
    @FXML private JFXTextField workoutCountTextField;
    @FXML private JFXTreeTableView<WorkoutTreeData> workoutLogTreeTableView;
    @FXML private TreeTableColumn<WorkoutTreeData, String> workoutLogIDColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> workoutLogTimeColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> workoutLogDurationColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> workoutLogShapeColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> workoutLogPerformanceColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> workoutLogNoteColumn;
    
    // Result Log
    private ObservableList<WorkoutTreeData> resultTreeData;
    @FXML private JFXDatePicker startDatePicker;
    @FXML private JFXTimePicker startTimePicker;
    @FXML private JFXDatePicker endDatePicker;
    @FXML private JFXTimePicker endTimePicker;
    @FXML private JFXComboBox<Exercise> exerciseComboBox;
    @FXML private JFXTreeTableView<WorkoutTreeData> resultLogTreeTableView;
    @FXML private TreeTableColumn<WorkoutTreeData, String> resultLogIDColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> resultLogTimeColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> resultLogDurationColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> resultLogShapeColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> resultLogPerformanceColumn;
    @FXML private TreeTableColumn<WorkoutTreeData, String> resultLogNoteColumn;

    
    @FXML
    private void initialize() {
    	initializeListViews();
    	initializeWorkoutLogTable();
    	initializeResultLogTable();
    	update();
    }
    
    private void initializeListViews() {
    	
	}
    
    private void initializeWorkoutLogTable() {
    	// Allow selection of multiple entities.
		workoutLogTreeTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  	
		
		// Create observable list backing TreeTableView
		workoutTreeData = FXCollections.observableArrayList();
		
    	// Set column cell factories
		workoutLogIDColumn			.setCellValueFactory(cell -> cell.getValue().getValue().id);
		workoutLogTimeColumn		.setCellValueFactory(cell -> cell.getValue().getValue().time);
		workoutLogDurationColumn	.setCellValueFactory(cell -> cell.getValue().getValue().duration);
		workoutLogShapeColumn		.setCellValueFactory(cell -> cell.getValue().getValue().shape);
		workoutLogPerformanceColumn	.setCellValueFactory(cell -> cell.getValue().getValue().performance);
		workoutLogNoteColumn		.setCellValueFactory(cell -> cell.getValue().getValue().note);

		// Assign root node to TreeTableView for holding users
    	TreeItem<WorkoutTreeData> root = new RecursiveTreeItem<WorkoutTreeData>(workoutTreeData, RecursiveTreeObject::getChildren);
    	workoutLogTreeTableView.setRoot(root);
    	workoutLogTreeTableView.setShowRoot(false);
    }

    private void initializeResultLogTable() {
    	// Allow selection of multiple entities.
    	resultLogTreeTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);  	
    	
    	// Create observable list backing TreeTableView
    	resultTreeData = FXCollections.observableArrayList();
    	
    	// Set column cell factories
    	resultLogIDColumn			.setCellValueFactory(cell -> cell.getValue().getValue().id);
    	resultLogTimeColumn			.setCellValueFactory(cell -> cell.getValue().getValue().time);
    	resultLogDurationColumn		.setCellValueFactory(cell -> cell.getValue().getValue().duration);
    	resultLogShapeColumn		.setCellValueFactory(cell -> cell.getValue().getValue().shape);
    	resultLogPerformanceColumn	.setCellValueFactory(cell -> cell.getValue().getValue().performance);
    	resultLogNoteColumn		.setCellValueFactory(cell -> cell.getValue().getValue().note);
    	
    	// Assign root node to TreeTableView for holding users
    	TreeItem<WorkoutTreeData> root = new RecursiveTreeItem<WorkoutTreeData>(resultTreeData, RecursiveTreeObject::getChildren);
    	resultLogTreeTableView.setRoot(root);
    	resultLogTreeTableView.setShowRoot(false);
    }

    
    public void update() {
    	updateListViews();
    	updateWorkoutLogTable();
    	updateResultLogTable();
    }

	private void updateListViews() {
		equipmentListView.getItems().setAll(EquipmentQueryHandler.getEquipments());
		exerciseListView.getItems().setAll(ExerciseQueryHandler.getExercises());
		exerciseGroupListView.getItems().setAll(ExerciseGroupQueryHandler.getExerciseGroups());
		workoutListView.getItems().setAll(WorkoutQueryHandler.getWorkouts());
		noteListView.getItems().setAll(NoteQueryHandler.getNotes());
	}
	
	private void updateWorkoutLogTable() {
		// Fetch list of workouts from database
		List<Workout> workouts = WorkoutQueryHandler.getWorkouts();
		
		// Convert workouts to internal format
		List<WorkoutTreeData> formattedWorkouts = workouts.stream()
				.map(WorkoutTreeData::new)
				.collect(Collectors.toList());
		
		workoutTreeData.setAll(formattedWorkouts);
		workoutLogTreeTableView.getSelectionModel().clearSelection();
		workoutLogTreeTableView.getSortOrder().clear();
	}

	private void updateResultLogTable() {
		// Fetch list of workouts from database
		List<Workout> results = WorkoutQueryHandler.getWorkouts();
		
		// Convert workouts to internal format
		List<WorkoutTreeData> formattedResults = results.stream()
				.map(WorkoutTreeData::new)
				.collect(Collectors.toList());
		
		resultTreeData.setAll(formattedResults);
		resultLogTreeTableView.getSelectionModel().clearSelection();
		resultLogTreeTableView.getSortOrder().clear();		
	}


	

	/*
     * Add
     */
    
    @FXML
    void handleAddEquipmentClick(ActionEvent event) {

    }

    @FXML
    void handleAddExerciseClick(ActionEvent event) {

    }

    @FXML
    void handleAddExerciseGroupClick(ActionEvent event) {

    }

    @FXML
    void handleAddNoteClick(ActionEvent event) {

    }

    @FXML
    void handleAddWorkoutClick(ActionEvent event) {

    }
    
    /*
     * Edit
     */
    
    @FXML
    void handleEditEquipmentClick(ActionEvent event) {
    	
    }
    
    @FXML
    void handleEditExerciseClick(ActionEvent event) {
    	
    }
    
    @FXML
    void handleEditExerciseGroupClick(ActionEvent event) {
    	
    }
    
    @FXML
    void handleEditNoteClick(ActionEvent event) {
    	
    }
    
    @FXML
    void handleEditWorkoutClick(ActionEvent event) {

    }
    
    /*
     * Delete
     */
    
    @FXML
    void handleDeleteEquipmentClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteExerciseClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteExerciseGroupClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteNoteClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteWorkoutClick(ActionEvent event) {

    }

    /**
     * Class used internally by TreeTableView for representing workout data.
     */
    public class WorkoutTreeData extends RecursiveTreeObject<WorkoutTreeData> {
    	private final StringProperty id, time, duration, shape, performance, note;
    	
    	public WorkoutTreeData(Workout workout) {
			this.id = new SimpleStringProperty(String.valueOf(workout.getWorkoutID()));
			this.time = new SimpleStringProperty(new SimpleDateFormat("dd. MMM HH:mm").format(Timestamp.valueOf(LocalDateTime.of(workout.getDate().toLocalDate(), workout.getTime().toLocalTime()))));
			this.duration = new SimpleStringProperty(workout.getDuration().toString());
			this.shape = new SimpleStringProperty(String.valueOf(workout.getShape()));
			this.performance = new SimpleStringProperty(String.valueOf(workout.getPerformance()));
			this.note = new SimpleStringProperty(workout.getNote() != null ? workout.getNote().getText() : "-");
		}
    }

}

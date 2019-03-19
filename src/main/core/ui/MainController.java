package main.core.ui;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.base.ValidatorBase;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import main.app.Loader;
import main.app.StageManager;
import main.core.ui.popups.EquipmentPopupController;
import main.core.ui.popups.ExerciseGroupPopupController;
import main.core.ui.popups.NotePopupController;
import main.database.handlers.EquipmentService;
import main.database.handlers.ExerciseGroupService;
import main.database.handlers.ExerciseService;
import main.database.handlers.NoteService;
import main.database.handlers.WorkoutService;
import main.models.Equipment;
import main.models.Exercise;
import main.models.ExerciseGroup;
import main.models.Note;
import main.models.Workout;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.View;

public class MainController implements Refreshable {
	@FXML private StackPane rootPane;
	
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
    private NumberValidator validator;
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

    // Related exercises
    @FXML private JFXComboBox<ExerciseGroup> relatedExerciseGroupComboBox;
    @FXML private JFXListView<Exercise> relatedExercisesListView;
    
    // Controller references
    private EquipmentPopupController equipmentController;
    private ExerciseGroupPopupController exerciseGroupController;
    private NotePopupController noteController;
    
    // Selection properties
    private IntegerBinding equipmentSelectionSize;
    private IntegerBinding exerciseSelectionSize;
    private IntegerBinding exerciseGroupSelectionSize;
    private IntegerBinding workoutSelectionSize;
    private IntegerBinding noteSelectionSize;
    
    
    
    @FXML
    private void initialize() {
    	initializeListViews();
    	initializeWorkoutLogTable();
    	initializeResultLogTable();
    	initializeRelatedExercises();
    	initializeBindings();
    	initializeChangeListeners();
    	update();
    }
    
    /**
	 * Initialize property bindings, making sure that the interface
	 * is in a valid state by disallowing invalid actions.
	 */
	private void initializeBindings() {
    	equipmentSelectionSize = Bindings.size(equipmentListView.getSelectionModel().getSelectedItems());
    	exerciseSelectionSize = Bindings.size(exerciseListView.getSelectionModel().getSelectedItems());
    	exerciseGroupSelectionSize = Bindings.size(exerciseGroupListView.getSelectionModel().getSelectedItems());
    	workoutSelectionSize = Bindings.size(workoutListView.getSelectionModel().getSelectedItems());
    	noteSelectionSize = Bindings.size(noteListView.getSelectionModel().getSelectedItems());
    	
    	// Bind 'add' button to being disabled when no single course is selected.
    	editEquipmentButton.disableProperty().bind(equipmentSelectionSize.isNotEqualTo(1));
    	editExerciseButton.disableProperty().bind(exerciseSelectionSize.isNotEqualTo(1));
    	editExerciseGroupButton.disableProperty().bind(exerciseGroupSelectionSize.isNotEqualTo(1));
    	editWorkoutButton.disableProperty().bind(workoutSelectionSize.isNotEqualTo(1));
    	editNoteButton.disableProperty().bind(noteSelectionSize.isNotEqualTo(1));

    	// Bind 'delete' button to being disabled when no entity is selected.
    	deleteEquipmentButton.disableProperty().bind(equipmentSelectionSize.isEqualTo(0));
    	deleteExerciseButton.disableProperty().bind(exerciseSelectionSize.isEqualTo(0));
    	deleteExerciseGroupButton.disableProperty().bind(exerciseGroupSelectionSize.isEqualTo(0));
    	deleteWorkoutButton.disableProperty().bind(workoutSelectionSize.isEqualTo(0));
    	deleteNoteButton.disableProperty().bind(noteSelectionSize.isEqualTo(0));
	}
    
	/** 
	 * Initialize change listeners, making the interface respond to changes.
	 */
	private void initializeChangeListeners() {
		// Assign change listener to focus property, unselecting entities when focus is lost.
		// Assures that users from multiple roles cannot be selected simultaneously.
    	equipmentListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue) {
    			exerciseListView.getSelectionModel().clearSelection();
    			exerciseGroupListView.getSelectionModel().clearSelection();
    			workoutListView.getSelectionModel().clearSelection();
    			noteListView.getSelectionModel().clearSelection();
    		}
    	});
    	
    	exerciseListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue) {
    			equipmentListView.getSelectionModel().clearSelection();
    			exerciseGroupListView.getSelectionModel().clearSelection();
    			workoutListView.getSelectionModel().clearSelection();
    			noteListView.getSelectionModel().clearSelection();
    		}
    	});
    	
    	exerciseGroupListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue) {
    			equipmentListView.getSelectionModel().clearSelection();
    			exerciseListView.getSelectionModel().clearSelection();
    			workoutListView.getSelectionModel().clearSelection();
    			noteListView.getSelectionModel().clearSelection();
    		}
    	});
    	
    	workoutListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue) {
    			equipmentListView.getSelectionModel().clearSelection();
    			exerciseListView.getSelectionModel().clearSelection();
    			exerciseGroupListView.getSelectionModel().clearSelection();
    			noteListView.getSelectionModel().clearSelection();
    		}
    	});
    	
    	noteListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue) {
    			equipmentListView.getSelectionModel().clearSelection();
    			exerciseListView.getSelectionModel().clearSelection();
    			exerciseGroupListView.getSelectionModel().clearSelection();
    			workoutListView.getSelectionModel().clearSelection();
    		}
    	});
	}
	
    private void initializeListViews() {
    	equipmentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	exerciseListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	exerciseGroupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	workoutListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	noteListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
		equipmentListView.setCellFactory(customCellFactory(Equipment::getName));
		exerciseListView.setCellFactory(customCellFactory(Exercise::getName));
		exerciseGroupListView.setCellFactory(customCellFactory(ExerciseGroup::getName));
		workoutListView.setCellFactory(customCellFactory(w -> String.format("#%s - %s", w.getWorkoutID(), w.getTimestamp())));
		noteListView.setCellFactory(customCellFactory(Note::getTitle));
	}
    
    private void initializeWorkoutLogTable() {
    	// Input validation
    	validator = new NumberValidator();
    	validator.setMessage("Dette feltet må være et heltall.");
		workoutCountTextField.getValidators().add(validator);
		workoutCountTextField.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				workoutCountTextField.validate();
				updateWorkoutLogTable();
			}
		});
		
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
    	// Add listener
    	BooleanBinding isValidStartDate = startDatePicker.valueProperty().isNotNull();
    	BooleanBinding isValidStartTime = startTimePicker.valueProperty().isNotNull();
    	BooleanBinding isValidEndDate = endDatePicker.valueProperty().isNotNull();
    	BooleanBinding isValidEndTime = endTimePicker.valueProperty().isNotNull();
    	BooleanBinding isValidExercise = exerciseComboBox.getSelectionModel().selectedItemProperty().isNotNull();
    	BooleanBinding isValidResultLog = isValidStartDate.and(isValidStartTime)
    			.and(isValidEndDate)
    			.and(isValidEndTime)
    			.and(isValidExercise);
    	
    	isValidResultLog.addListener((obs, oldValue, newValue) -> {
    		if (newValue)
    			updateResultLogTable();
    	});
    	
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
    	resultLogNoteColumn			.setCellValueFactory(cell -> cell.getValue().getValue().note);
    	
    	// Assign root node to TreeTableView for holding users
    	TreeItem<WorkoutTreeData> root = new RecursiveTreeItem<WorkoutTreeData>(resultTreeData, RecursiveTreeObject::getChildren);
    	resultLogTreeTableView.setRoot(root);
    	resultLogTreeTableView.setShowRoot(false);
    }

    private void initializeRelatedExercises() {
    	relatedExerciseGroupComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
    		updateRelatedExercises();
    	});
    }
    
    /**
	 * Runs any methods that require every controller to be initialized. This method
	 * should only be invoked by the FXML Loader class.
	 */
	@PostInitialize
	private void postInitialize() {
		equipmentController = Loader.getController(View.POPUP_EQUIPMENT);
		exerciseGroupController = Loader.getController(View.POPUP_EXERCISE_GROUP);
		noteController = Loader.getController(View.POPUP_NOTE);
	}
    
    @Override
	public void update() {
    	updateListViews();
    	updateWorkoutLogTable();
    	updateResultLogTable();
    }

	private void updateListViews() {
		equipmentListView.getItems().setAll(EquipmentService.getEquipments());
		exerciseListView.getItems().setAll(ExerciseService.getExercises());
		exerciseGroupListView.getItems().setAll(ExerciseGroupService.getExerciseGroups());
		workoutListView.getItems().setAll(WorkoutService.getWorkouts());
		noteListView.getItems().setAll(NoteService.getNotes());
	}
	
	private void updateWorkoutLogTable() {
		if (validator.getHasErrors()  
				||  workoutCountTextField.getText() == null  
				||  workoutCountTextField.getText().equals(""))
			return;
		
		// Read count value
		int n = Integer.parseInt(workoutCountTextField.getText());
		
		// Fetch list of workouts from database
		List<Workout> workouts = WorkoutService.getRecentWorkouts(n);
		
		// Convert workouts to internal format
		List<WorkoutTreeData> formattedWorkouts = workouts.stream()
				.map(WorkoutTreeData::new)
				.collect(Collectors.toList());
		
		workoutTreeData.setAll(formattedWorkouts);
		workoutLogTreeTableView.getSelectionModel().clearSelection();
		workoutLogTreeTableView.getSortOrder().clear();
	}

	private void updateResultLogTable() {
		if (startDatePicker.getValue() == null
				|| startTimePicker.getValue() == null
				|| endDatePicker.getValue() == null
				|| endTimePicker.getValue() == null)
			return;
		
		Exercise exercise = exerciseComboBox.getSelectionModel().getSelectedItem();
		Timestamp from = Timestamp.valueOf(LocalDateTime.of(startDatePicker.getValue(), startTimePicker.getValue()));
		Timestamp to = Timestamp.valueOf(LocalDateTime.of(endDatePicker.getValue(), endTimePicker.getValue()));
		
		// Fetch list of workouts from database
		List<Workout> results = WorkoutService.getWorkoutByExerciseAndInterval(exercise, from, to);
		
		// Convert workouts to internal format
		List<WorkoutTreeData> formattedResults = results.stream()
				.map(WorkoutTreeData::new)
				.collect(Collectors.toList());
		
		resultTreeData.setAll(formattedResults);
		resultLogTreeTableView.getSelectionModel().clearSelection();
		resultLogTreeTableView.getSortOrder().clear();		
	}

	private void updateRelatedExercises() {
		
	}
	
	private <T> Callback<ListView<T>, ListCell<T>> customCellFactory(Function<T, String> nameFunction) {
    	return listView -> {
			return new JFXListCell<T>() {
				@Override
				protected void updateItem(T item, boolean empty) {
					super.updateItem(item, empty);
					setText(item != null ? nameFunction.apply(item) : null);
				}
			};
    	};
    }
	

	/*
     * Add
     */
    
    @FXML
    void handleAddEquipmentClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EQUIPMENT);
    	equipmentController.loadCreateMode();
    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.show();
    }

    @FXML
    void handleAddExerciseClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EQUIPMENT);
    	equipmentController.loadCreateMode();
    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.show();
    }

    @FXML
    void handleAddExerciseGroupClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EXERCISE_GROUP);
    	exerciseGroupController.loadCreateMode();
    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.show();
    }

    @FXML
    void handleAddWorkoutClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EQUIPMENT);
    	equipmentController.loadCreateMode();
    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.show();
    }

    @FXML
    void handleAddNoteClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_NOTE);
    	noteController.loadCreateMode();
    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.show();
    }
    
    
    /*
     * Edit
     */
    
    @FXML
    void handleEditEquipmentClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EQUIPMENT);
    	equipmentController.loadEditMode(equipmentListView.getSelectionModel().getSelectedItem());
    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.show();
    }
    
    @FXML
    void handleEditExerciseClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EQUIPMENT);
    	equipmentController.loadEditMode(equipmentListView.getSelectionModel().getSelectedItem());
    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.show();
    }
    
    @FXML
    void handleEditExerciseGroupClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EXERCISE_GROUP);
    	exerciseGroupController.loadEditMode(exerciseGroupListView.getSelectionModel().getSelectedItem());
//    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.setOnDialogClosed(e -> update());
    	dialog.show();
    }
  
    @FXML
    void handleEditWorkoutClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EQUIPMENT);
    	equipmentController.loadEditMode(equipmentListView.getSelectionModel().getSelectedItem());
    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.show();
    }
    
    @FXML
    void handleEditNoteClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_NOTE);
    	noteController.loadEditMode(noteListView.getSelectionModel().getSelectedItem());
    	dialog.setOnDialogClosed(e -> updateListViews());
    	dialog.show();
    }
    
    
    /*
     * Delete
     */
    
    @FXML
    void handleDeleteEquipmentClick(ActionEvent event) {
    	EquipmentService.deleteEquipments(equipmentListView.getSelectionModel().getSelectedItems());
    	updateListViews();
    }

    @FXML
    void handleDeleteExerciseClick(ActionEvent event) {
    	ExerciseService.deleteExercises(exerciseListView.getSelectionModel().getSelectedItems());
    	updateListViews();
    }

    @FXML
    void handleDeleteExerciseGroupClick(ActionEvent event) {
    	ExerciseGroupService.deleteExerciseGroups(exerciseGroupListView.getSelectionModel().getSelectedItems());
    	updateListViews();
    }

    @FXML
    void handleDeleteNoteClick(ActionEvent event) {
    	NoteService.deleteNotes(noteListView.getSelectionModel().getSelectedItems());
    	updateListViews();
    }

    @FXML
    void handleDeleteWorkoutClick(ActionEvent event) {
    	WorkoutService.deleteWorkouts(workoutListView.getSelectionModel().getSelectedItems());
    	updateListViews();
    }

    /**
     * Class used internally by TreeTableView for representing workout data.
     */
    public class WorkoutTreeData extends RecursiveTreeObject<WorkoutTreeData> {
    	private final StringProperty id, time, duration, shape, performance, note;
    	
    	public WorkoutTreeData(Workout workout) {
			this.id = new SimpleStringProperty(String.valueOf(workout.getWorkoutID()));
			this.time = new SimpleStringProperty(new SimpleDateFormat("dd. MMM HH:mm").format(workout.getTimestamp()));
			this.duration = new SimpleStringProperty(workout.getDuration().toString());
			this.shape = new SimpleStringProperty(String.valueOf(workout.getShape()));
			this.performance = new SimpleStringProperty(String.valueOf(workout.getPerformance()));
			this.note = new SimpleStringProperty(workout.getNote() != null ? workout.getNote().getText() : "-");
		}
    }

    /**
     * Custom implementation of {@link ValidatorBase} where errors can be triggered manually.
     */
    private class FieldValidator extends ValidatorBase {
    	
    	public FieldValidator() {
    		
    	}
    	
    	@Override
    	protected void eval() {
    	}
    	
    	public void setError(boolean error) {
    		hasErrors.set(error);
    	}
    }
}
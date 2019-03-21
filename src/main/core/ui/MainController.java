package main.core.ui;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.util.StringConverter;
import main.app.Loader;
import main.app.StageManager;
import main.core.ui.popups.EquipmentPopupController;
import main.core.ui.popups.ExerciseGroupPopupController;
import main.core.ui.popups.ExercisePopupController;
import main.core.ui.popups.NotePopupController;
import main.core.ui.popups.WorkoutPopupController;
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
    @FXML private JFXListView<Exercise> relatedExerciseGroupListView;
    
    // Controller references
    private EquipmentPopupController equipmentController;
    private ExercisePopupController exerciseController;
    private ExerciseGroupPopupController exerciseGroupController;
    private WorkoutPopupController workoutController;
    private NotePopupController noteController;
    
    // Selection properties
    private ObjectProperty<LocalDate> isValidStartDate;
    private ObjectProperty<LocalTime> isValidStartTime;
    private ObjectProperty<LocalDate> isValidEndDate;
    private ObjectProperty<LocalTime> isValidEndTime;
    private ReadOnlyObjectProperty<Exercise> isValidExercise;
    private BooleanBinding isValidResultLog;
    
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
		workoutListView.setCellFactory(customCellFactory(Workout::toString));
		noteListView.setCellFactory(customCellFactory(Note::getTitle));
	}
    
    private void initializeWorkoutLogTable() {
    	// Input validation
    	validator = new NumberValidator();
    	validator.setMessage("Dette feltet må være et heltall.");
		workoutCountTextField.getValidators().add(validator);
		workoutCountTextField.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null  &&  workoutCountTextField.validate())
				updateWorkoutLogTable();
			else
				workoutTreeData.clear();
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
    	// Change time picker format to 24 hours
    	startTimePicker.set24HourView(true);
    	endTimePicker.set24HourView(true);
    	
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
    	
    	// Create changeListener
    	ChangeListener<Object> changeListener = (obs, oldValue, newValue) -> {
    		System.out.format("Null check: %s, isValidResultLog: %s%n", newValue != null, isValidResultLog.get());
    		if (newValue != null  &&  isValidResultLog.get())
    			updateResultLogTable();
    		else
    			resultTreeData.clear();
    	};
    	
    	// Add listener
    	isValidStartDate = startDatePicker.valueProperty();
    	isValidStartTime = startTimePicker.valueProperty();
    	isValidEndDate = endDatePicker.valueProperty();
    	isValidEndTime = endTimePicker.valueProperty();
    	isValidExercise = exerciseComboBox.valueProperty();
    	isValidResultLog = isValidStartDate.isNotNull().and(isValidStartTime.isNotNull())
    			.and(isValidEndDate.isNotNull())
    			.and(isValidEndTime.isNotNull())
    			.and(isValidExercise.isNotNull());
    	
    	isValidStartDate.addListener(changeListener);
    	isValidStartTime.addListener(changeListener);
    	isValidEndDate.addListener(changeListener);
    	isValidEndTime.addListener(changeListener);
    	isValidExercise.addListener(changeListener);
    	
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
    	relatedExerciseGroupComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
    		updateRelatedExercises();
    	});
    	
    	// Combo box conversion
//    	relatedExerciseGroupComboBox.getEditor().textProperty().addListener((obs, oldValue, newVaule) -> {
//    		relatedExerciseGroupComboBox.commitValue();
//    	});
    	relatedExerciseGroupComboBox.setConverter(new StringConverter<ExerciseGroup>() {
			@Override
			public String toString(ExerciseGroup exerciseGroup) {
				return exerciseGroup != null ? exerciseGroup.getName() : "";
			}
			
			@Override
			public ExerciseGroup fromString(String string) {
				return relatedExerciseGroupComboBox.getItems().stream()
						.filter(e -> e.getName().equalsIgnoreCase(string))
						.findFirst()
						.orElse(null);
			}
		});
    }
    
	@PostInitialize
	private void postInitialize() {
		equipmentController = Loader.getController(View.POPUP_EQUIPMENT);
		exerciseController = Loader.getController(View.POPUP_EXERCISE);
		exerciseGroupController = Loader.getController(View.POPUP_EXERCISE_GROUP);
		workoutController = Loader.getController(View.POPUP_WORKOUT);
		noteController = Loader.getController(View.POPUP_NOTE);
	}
    
	
    @Override
	public void update() {
    	updateData();
    	updateWorkoutLogTable();
    	updateResultLogTable();
    }

    private void updateData() {
    	List<Equipment> equipments = EquipmentService.getEquipments();
    	List<Exercise> exercises = ExerciseService.getExercises();
    	List<ExerciseGroup> exerciseGroups = ExerciseGroupService.getExerciseGroups();
    	List<Workout> workouts = WorkoutService.getWorkouts();
    	List<Note> notes = NoteService.getNotes();
    	
    	equipmentListView.getItems().setAll(equipments);
    	exerciseListView.getItems().setAll(exercises);
    	exerciseGroupListView.getItems().setAll(exerciseGroups);
    	workoutListView.getItems().setAll(workouts);
    	noteListView.getItems().setAll(notes);
    	
    	exerciseListView.getItems().setAll(exercises);
    	exerciseComboBox.getItems().setAll(exercises);
    	relatedExerciseGroupComboBox.getItems().setAll(exerciseGroups);
    }
    
	private void updateEntity(Entity entity) {
		switch (entity) {
		case EQUIPMENT:
			equipmentListView.getItems().setAll(EquipmentService.getEquipments());
			break;
			
		case EXERCISE:
			List<Exercise> exercises = ExerciseService.getExercises();
			exerciseListView.getItems().setAll(exercises);
			exerciseComboBox.getItems().setAll(exercises);
			updateRelatedExercises();
			
//			// If previously selected exercise was removed
//			exerciseComboBox.getSelectionModel().clearSelection();
			
			break;
			
		case EXERCISE_GROUP:
			List<ExerciseGroup> exerciseGroups = ExerciseGroupService.getExerciseGroups();
			exerciseGroupListView.getItems().setAll(exerciseGroups);
	    	relatedExerciseGroupComboBox.getItems().setAll(exerciseGroups);
			updateRelatedExercises();
			break;
			
		case WORKOUT:
			workoutListView.getItems().setAll(WorkoutService.getWorkouts());
			updateWorkoutLogTable();
			updateResultLogTable();
			break;
			
		case NOTE:
			noteListView.getItems().setAll(NoteService.getNotes());
			updateWorkoutLogTable();
			updateResultLogTable();
			break;
			
			
		default:
			break;
		}
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
				|| endTimePicker.getValue() == null
				|| exerciseComboBox.getValue() == null)
			return;
		
		Exercise exercise = exerciseComboBox.getValue();
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
		ExerciseGroup exerciseGroup = relatedExerciseGroupComboBox.getValue();
		
		if (exerciseGroup != null)
			relatedExerciseGroupListView.getItems().setAll(ExerciseService.getExercisesInGroup(exerciseGroup));
		else
			relatedExerciseGroupListView.getItems().clear();
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
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.EQUIPMENT));
    	dialog.show();
    }

    @FXML
    void handleAddExerciseClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EXERCISE);
    	exerciseController.loadCreateMode();
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.EXERCISE));
    	dialog.show();
    }

    @FXML
    void handleAddExerciseGroupClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EXERCISE_GROUP);
    	exerciseGroupController.loadCreateMode();
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.EXERCISE_GROUP));
    	dialog.show();
    }

    @FXML
    void handleAddWorkoutClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_WORKOUT);
    	workoutController.loadCreateMode();
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.WORKOUT));
    	dialog.show();
    }

    @FXML
    void handleAddNoteClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_NOTE);
    	noteController.loadCreateMode();
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.NOTE));
    	dialog.show();
    }
    
    
    /*
     * Edit
     */
    
    @FXML
    void handleEditEquipmentClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EQUIPMENT);
    	equipmentController.loadEditMode(equipmentListView.getSelectionModel().getSelectedItem());
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.EQUIPMENT));
    	dialog.show();
    }
    
    @FXML
    void handleEditExerciseClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EXERCISE);
    	exerciseController.loadEditMode(exerciseListView.getSelectionModel().getSelectedItem());
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.EXERCISE));
    	dialog.show();
    }
    
    @FXML
    void handleEditExerciseGroupClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_EXERCISE_GROUP);
    	exerciseGroupController.loadEditMode(exerciseGroupListView.getSelectionModel().getSelectedItem());
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.EXERCISE_GROUP));
    	dialog.show();
    }
  
    @FXML
    void handleEditWorkoutClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_WORKOUT);
    	workoutController.loadEditMode(workoutListView.getSelectionModel().getSelectedItem());
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.WORKOUT));
    	dialog.show();
    }
    
    @FXML
    void handleEditNoteClick(ActionEvent event) {
    	JFXDialog dialog = StageManager.createPopupDialog(rootPane, View.POPUP_NOTE);
    	noteController.loadEditMode(noteListView.getSelectionModel().getSelectedItem());
    	dialog.setOnDialogClosed(e -> updateEntity(Entity.NOTE));
    	dialog.show();
    }
    
    
    /*
     * Delete
     */
    
    @FXML
    void handleDeleteEquipmentClick(ActionEvent event) {
    	EquipmentService.deleteEquipments(equipmentListView.getSelectionModel().getSelectedItems());
    	updateEntity(Entity.EQUIPMENT);
    }

    @FXML
    void handleDeleteExerciseClick(ActionEvent event) {
    	ExerciseService.deleteExercises(exerciseListView.getSelectionModel().getSelectedItems());
    	updateEntity(Entity.EXERCISE);
    }

    @FXML
    void handleDeleteExerciseGroupClick(ActionEvent event) {
    	ExerciseGroupService.deleteExerciseGroups(exerciseGroupListView.getSelectionModel().getSelectedItems());
    	updateEntity(Entity.EXERCISE_GROUP);
    }
    
    @FXML
    void handleDeleteWorkoutClick(ActionEvent event) {
    	WorkoutService.deleteWorkouts(workoutListView.getSelectionModel().getSelectedItems());
    	updateEntity(Entity.WORKOUT);
    }

    @FXML
    void handleDeleteNoteClick(ActionEvent event) {
    	NoteService.deleteNotes(noteListView.getSelectionModel().getSelectedItems());
    	updateEntity(Entity.NOTE);
    }

    /**
     * Class used internally by TreeTableView for representing workout data.
     */
    public class WorkoutTreeData extends RecursiveTreeObject<WorkoutTreeData> {
    	private final StringProperty id, time, duration, shape, performance, note;
    	
    	public WorkoutTreeData(Workout workout) {
			this.id = new SimpleStringProperty(String.valueOf(workout.getWorkoutID()));
			
			String timestamp = workout.getTimestamp() != null ? new SimpleDateFormat("dd. MMM HH:mm").format(workout.getTimestamp()) : "-";
			String duration = workout.getDuration() != null ? workout.getDuration().toString() : "-";
			String note = workout.getNote() != null ? workout.getNote().getTitle() : "-";
			
			this.time = new SimpleStringProperty(timestamp);
			this.duration = new SimpleStringProperty(duration);
			this.shape = new SimpleStringProperty(String.valueOf(workout.getShape()));
			this.performance = new SimpleStringProperty(String.valueOf(workout.getPerformance()));
			this.note = new SimpleStringProperty(note);
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

    public enum Entity {
    	EQUIPMENT, EXERCISE, EXERCISE_GROUP, WORKOUT, NOTE;
    }
    
}
package main.utils;

public enum View {
	// 							TITLE			Type				FXML path
	MAIN(						"iExercise",	ViewType.SCENE,		"ui/Main.fxml"),
	POPUP_EQUIPMENT(			null, 			ViewType.POPUP,		"ui/popups/EquipmentPopup.fxml"),
	POPUP_EXERCISE(				null,			ViewType.POPUP,		"ui/popups/ExercisePopup.fxml"), 
	POPUP_EXERCISE_GROUP(		null, 			ViewType.POPUP,		"ui/popups/ExerciseGroupPopup.fxml"),
	POPUP_WORKOUT(				null, 			ViewType.POPUP,		"ui/popups/WorkoutPopup.fxml"),
	POPUP_NOTE(					null, 			ViewType.POPUP,		"ui/popups/NotePopup.fxml");
	
	
	private final String title;
	private final ViewType viewType;
	private final String pathToFXML;
	
	private View(String title, ViewType viewType, String pathToFXML) {
		this.title = title;
		this.viewType = viewType;
		this.pathToFXML = pathToFXML;
	}
	
	public String getTitle() {
		return title;
	}
	
	public ViewType getViewType() {
		return viewType;
	}
	
	public String getPathToFXML() {
		return pathToFXML;
	}
	
	
	public enum ViewType {
		SCENE, POPUP, COMPONENT, TAB; 
	}
	
}

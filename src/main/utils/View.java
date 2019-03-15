package main.utils;

public enum View {
	// 							TITLE			Type				FXML path
	HOVEDSIDE(					"iExercise",	ViewType.SCENE,		"ui/Hovedside.fxml");
//	POPUP_APPARAT(				null, 			true,				"ui/popups/ApparatPopup.fxml"), 
//	POPUP_ØVELSE(				null,			true,				"ui/popups/ØvelsePopup.fxml"), 
//	POPUP_TRENINGSØKT(			null, 			true,				"ui/popups/TreningsøktPopup.fxml"),
//	POPUP_ØVELSE_GRUPPE(		null, 			true,				"ui/popups/ØvelseGruppePopup.fxml"),
//	POPUP_NOTAT(				null, 			true,				"ui/popups/NotatPopup.fxml");
	
	
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

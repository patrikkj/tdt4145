package main.utils;

public enum View {
	// 							TITLE			Load on startup		FXML path
	HOVEDSIDE(					"iExercise",	true,				"ui/Hovedside.fxml");
//	POPUP_APPARAT(				null, 			true,				"ui/popups/ApparatPopup.fxml"), 
//	POPUP_ØVELSE(				null,			true,				"ui/popups/ØvelsePopup.fxml"), 
//	POPUP_TRENINGSØKT(			null, 			true,				"ui/popups/TreningsøktPopup.fxml"),
//	POPUP_ØVELSE_GRUPPE(		null, 			true,				"ui/popups/ØvelseGruppePopup.fxml"),
//	POPUP_NOTAT(				null, 			true,				"ui/popups/NotatPopup.fxml");
	
	
	private final String title;
	private final boolean loadOnStartup;
	private final String pathToFXML;
	
	private View(String title, boolean loadOnStartup, String pathToFXML) {
		this.title = title;
		this.loadOnStartup = loadOnStartup;
		this.pathToFXML = pathToFXML;
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean isLoadOnStartup() {
		return loadOnStartup;
	}
	
	public String getPathToFXML() {
		return pathToFXML;
	}
	
}
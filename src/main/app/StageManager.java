package main.app;

import java.io.IOException;
import java.util.EnumMap;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.utils.Refreshable;
import main.utils.View;

/**
 * Class for managing transitions between different scenes in the application. 
 */
public class StageManager {
	private static Stage stage;
	private static Image icon;
	private static EnumMap<View, Scene> viewToScene;
	
	
	/**
	 * This method must be called from FXML Application thread on startup.
	 * @param stage - stage given by the FXML Application thread's {@code run} method.
	 * @see Runnable
	 */
	protected static void initialize(Stage stage) throws IOException {
		StageManager.stage = stage;
		StageManager.icon = new Image(StageManager.class.getClassLoader().getResourceAsStream("icons/logo.png"));
		StageManager.viewToScene = new EnumMap<>(View.class);
		
		// Preloading all FXML files
		new Loader().run();
	}
	
	/**
	 * Sets the FXML node to be displayed on screen, specified by {@code view}. 
	 * Closes the previous stage and creates a new one.
	 */
	public static void loadView(View view) {
		// Close previous stage
		stage.close();
		
		// Load root node in FXML hierarchy
		Scene scene = prepareScene(view);
		
		// Load new stage
		stage = new Stage();
		stage.setScene(scene);
		stage.setTitle(view.getTitle());
		stage.getIcons().add(icon);
		stage.sizeToScene();
		stage.centerOnScreen();
		
		// Clear and display
		Object controller = Loader.getController(view);
		if (controller instanceof Refreshable)
			((Refreshable) controller).refresh();
		else
			System.err.printf("StageManager can only handle controllers of type 'Refreshable', missing in '%s'.", controller.getClass().getSimpleName());
		
		stage.show();
	}
	
	/**
	 * Returns the {@link Scene} object associated with given {@link View}.
	 * If no scene is present, a new one is created.
	 */
	private static Scene prepareScene(View view) {
		return viewToScene.computeIfAbsent(view, v -> new Scene(Loader.getParent(v)));
	}
	
}

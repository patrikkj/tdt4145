package main.app;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import main.utils.PostInitialize;
import main.utils.View;
import main.utils.View.ViewType;

/**
 * Preloader class for FXML files, runnable on a separate thread.
 */
public class Loader implements Runnable {
	private static EnumMap<View, FXMLLoader> viewToLoader = new EnumMap<>(View.class);
	private static EnumMap<View, Parent> viewToParent = new EnumMap<>(View.class);

	
	/**
	 * {@inheritDoc}
	 * Loads FXML files followed by invocations to non-private {@code @PostInitialize} methods in controllers
	 */
	@Override
	public void run() {
		// Load FXML views
		loadViews();
		
		// Invoke methods with @PostInitialize annotation
		invokeMethodsAnnotatedWith(PostInitialize.class);
	}

	
	// Loaders
	/**
	 * Loads FXML files, storing references to FXMLLoaders and Parents in an enumeration mapping.
	 */
	private void loadViews() {
		for (View view : View.values()) {
			// Components should not be loaded on startup
			if (view.getViewType() == ViewType.COMPONENT)
				continue;
			
			loadView(view);
		}
	}
	
	/**
	 * Loads the FXML file represented by the given {@code View}.
	 */
	private void loadView(View view) {
		// Break if view is not implemented
		if (view.getPathToFXML() == null)
			return;

		// Load FXML file and cache parent
		FXMLLoader loader = buildFXMLLoader(view, true);
		
		// Vaildate view
		checkView(view, loader);
	}
	
	private void checkView(View view, FXMLLoader loader) {
		switch (view) {
		case MAIN:
			break;
		default:
			break;
		}
	}


	/**
	 * Loads the FXML file represented by the given {@code View}.
	 * Also invokes all methods with the {@code @PostInitialize} annotation.
	 */
	public FXMLLoader loadComponent(View component) {
		// Break if the view specified is not a component
		if (component.getViewType() != ViewType.COMPONENT)
			throw new IllegalArgumentException(String.format("Failed to load '%s', view is not of type '%s'.", component, ViewType.COMPONENT));
		
		FXMLLoader loader = buildFXMLLoader(component, false);
		
		// Invoke methods with @PostInitialize annotation
		invokeMethodsAnnotatedWith(loader.getController(), PostInitialize.class);
		
		return loader;
	}
	
	
	// Getters
	/**
	 * Returns the image located at the specified file path as an {@code Image}.
	 */
	public static Image getImage(String path) {
		return new Image(Loader.class.getClassLoader().getResourceAsStream(path));
	}
	
	/**
	 * Returns parent node in the FXML hierarchy represented by the view specified.
	 */
	public static Parent getParent(View view) {
		return viewToParent.get(view);
	}

	/**
	 * Returns the controller paired with the FXMLLoader for the specified view.
	 */
	public static <T> T getController(View view) {
		return viewToLoader.get(view).getController();
	}
	
	/**
	 * Returns the FXMLLoader used for loading the FXML file represented by the view specified.
	 */
	public static FXMLLoader getFXMLLoader(View view) {
		return viewToLoader.get(view);
	}

	
	// Annotations
	/**
	 * Returns a list of all methods within the specified class with a given {@code annotation}. 
	 */
	private static List<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation) {
	    return Arrays.stream(type.getDeclaredMethods())
				.filter(m -> m.isAnnotationPresent(annotation))
				.collect(Collectors.toList());
	}
	
	/**
	 * Invoke all methods with the specified annotation, regardless of access modifier.
	 */
	private void invokeMethodsAnnotatedWith(Object target, Class<? extends Annotation> annotation) {
		List<Method> annotatedMethods = getMethodsAnnotatedWith(target.getClass(), annotation);
		
		// Invoke every method with specified annotation.
		for (Method method : annotatedMethods) {
			try {
				method.setAccessible(true);
				method.invoke(target);
				System.out.printf("@PostInitialize invoked: Method: %s%n", method.getName());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Invoke all methods with the specified annotation, regardless of access modifier.
	 */
	private void invokeMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
		// Handle @PostInitialize annotations
		for (View view : View.values()) {
			// Break if view is not implemented
			if (view.getPathToFXML() == null)
				continue;

			// Components should not be loaded on startup
			if (view.getViewType() == ViewType.COMPONENT)
				continue;
			
			Object controller = getController(view);
			List<Method> annotatedMethods = getMethodsAnnotatedWith(controller.getClass(), annotation);
			
			// Invoke every method with specified annotation.
			for (Method method : annotatedMethods) {
				try {
					method.setAccessible(true);
					method.invoke(controller);
					System.out.printf("@PostInitialize invoked: View: %s, Method: %s%n", view.name(), method.getName());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	// Factories
	/**
	 * Returns a newly initialized FXMLLoader represented by the given {@code View}.
	 */
	public static FXMLLoader buildFXMLLoader(View view, boolean createMapping) {
		FXMLLoader loader = new FXMLLoader(Loader.class.getClassLoader().getResource(view.getPathToFXML()));
		Parent parent = null;
		
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (createMapping) {
			viewToLoader.put(view, loader);
			viewToParent.put(view, parent);
		}

		return loader;
	}
}
package main.core.ui.popups;

import com.jfoenix.controls.JFXDialog;

import main.utils.Refreshable;

public abstract class AbstractPopupController<T> implements Refreshable {
	protected JFXDialog dialog;
	protected Mode mode;
	
	
	/**
	 * Prepares popup for creating a new model. This method must be called
	 * prior to displaying the dialog box.
	 */
	public abstract void loadCreateMode();
	
	/**
	 * Prepares popup for editing the model specified. This method must be called
	 * prior to displaying the dialog box.
	 */
	public abstract void loadEditMode(T t);

	/**
	 * Reads input parameters, updating connected model.
	 */
	protected abstract void updateModelFromInput();

	/**
	 * Connects this controller to associated JFXDialog.
	 */
	public void connectDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}
	
	/**
	 * Sets popup mode.
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	/**
	 * Returns popup mode.
	 */
	public Mode getMode() {
		return mode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void clear();
	
	enum Mode {
		CREATE, EDIT;
	}
}

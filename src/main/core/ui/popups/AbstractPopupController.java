package main.core.ui.popups;

import com.jfoenix.controls.JFXDialog;

import main.utils.Refreshable;

public abstract class AbstractPopupController implements Refreshable {
	protected JFXDialog dialog;
	
	/**
	 * Connects this controller to associated JFXDialog.
	 */
	public void connectDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}
}

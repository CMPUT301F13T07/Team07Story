package edu.ualberta.controller;

import edu.ualberta.controller.CallbackIntefaces.*;

/**
 * Listener when starting PageEdit view.
 * 
 * @author Joey Andres
 */
public class OnStartPageEditListener extends
		CommandAbstract<OnStartPageEdit, OnUndo> {

	public OnStartPageEditListener(OnStartPageEdit callback) {
		super(callback, null);
	}

	@Override
	public void execute() {
		mCallbackExecute.onStartPageEdit();
	}

	@Override
	public void unexecute() {
		// Unknown command.
	}

	@Override
	public boolean isReversible() {
		return false;
	}

	public void setOnSaveListener(OnStartPageEdit callback) {
		mCallbackExecute = callback;
	}

	@Override
	public void setExecuteCallback(OnStartPageEdit callback) {
		setOnSaveListener(callback);
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		// TODO Auto-generated method stub

	}
}
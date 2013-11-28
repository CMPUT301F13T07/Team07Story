package edu.ualberta.controller;

import edu.ualberta.controller.CallbackIntefaces.*;

/**
 * Command for saving.
 */
public class OnSaveListener extends CommandAbstract<OnSave, OnUndo> {
	public OnSaveListener(OnSave cb) {
		super(cb, null);
	}

	@Override
	public void execute() {
		mCallbackExecute.onSave();
	}

	@Override
	public void unexecute() {
	}

	@Override
	public boolean isReversible() {
		return false;
	}

	public void setOnSaveListener(OnSave callback) {
		mCallbackExecute = callback;
	}

	@Override
	public void setExecuteCallback(OnSave callback) {
		setOnSaveListener(callback);
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		// TODO Auto-generated method stub

	}
}
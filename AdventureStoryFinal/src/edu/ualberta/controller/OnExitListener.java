package edu.ualberta.controller;

import edu.ualberta.controller.CallbackIntefaces.*;

/**
 * <code>exitActivityCommand</code> contains the call back for exiting
 * current activity.
 * 
 */
public class OnExitListener extends CommandAbstract<OnExit, OnUndo> {
	public OnExitListener(OnExit onExit) {
		super(onExit, null);
	}

	@Override
	public void execute() {
		mCallbackExecute.onExit();
	}

	@Override
	public void unexecute() {
	}

	@Override
	public boolean isReversible() {
		return false;
	}

	public void setOnExitListener(OnExit callback) {
		mCallbackExecute = callback;
	}

	@Override
	public void setExecuteCallback(OnExit callback) {
		setOnExitListener(callback);
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		// TODO Auto-generated method stub

	}
}
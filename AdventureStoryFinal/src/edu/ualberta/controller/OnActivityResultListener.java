package edu.ualberta.controller;

import edu.ualberta.controller.CallbackIntefaces.*;

/**
 * <code>OnActivityResultListener</code> is a listener for when returning result
 * from an activity.
 * 
 * @author jandres
 *
 */
public class OnActivityResultListener extends
		CommandAbstract<OnActivityResult, OnUndo> {

	public OnActivityResultListener(OnActivityResult execute) {
		super(execute, null);
	}

	@Override
	public void execute() {
		this.mCallbackExecute.onActivityResult();
	}

	@Override
	public void unexecute() {

	}

	@Override
	public boolean isReversible() {
		return false;
	}

	@Override
	public void setExecuteCallback(OnActivityResult callback) {
		this.mCallbackExecute = callback;
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {

	}
}
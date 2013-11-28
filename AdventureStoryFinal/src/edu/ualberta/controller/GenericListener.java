package edu.ualberta.controller;

import edu.ualberta.controller.CallbackIntefaces.OnCallbackGeneric;
import edu.ualberta.controller.CallbackIntefaces.OnUndo;

public class GenericListener extends CommandAbstract<OnCallbackGeneric, OnUndo> {
	
	public GenericListener(OnCallbackGeneric execute) {
		super(execute, null);
	}

	@Override
	public void execute() {
		this.mCallbackExecute.callback();
	}

	@Override
	public void unexecute() {		
	}

	@Override
	public boolean isReversible() {
		return false;
	}

	@Override
	public void setExecuteCallback(OnCallbackGeneric callback) {
		// TODO Auto-generated method stub
		mCallbackExecute = callback;
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {		
	}

}

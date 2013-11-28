package edu.ualberta.controller;

import edu.ualberta.controller.CallbackIntefaces.*;

/**
 * XXX DO NOT ADD THIS TO THE <code>CommandManager</code> stack.
 * @author jandres
 *
 */
public class OnUndoListener extends CommandAbstract<OnUndo, OnUndo>{

	public OnUndoListener(OnUndo execute) {
		super(execute, null);				
	}

	@Override
	public void execute() {
		this.mCallbackExecute.undo();
	}

	@Override
	public void unexecute() {
		this.mCallbackExecute.undo();			
	}

	@Override
	public boolean isReversible() {
		
		return true;
	}

	@Override
	public void setExecuteCallback(OnUndo callback) {
		this.mCallbackExecute = callback;
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		this.mCallbackExecute = callback;
	}
	
}
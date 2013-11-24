package edu.ualberta.controller;

import edu.ualberta.controller.CommandCollection.CommandAbstract;
import edu.ualberta.controller.CommandCollection.OnRedo;

/**
 * XXX DO NOT ADD THIS TO THE <code>CommandManager</code> stack.
 * @author jandres
 *
 */
public class OnRedoListener extends CommandAbstract<OnRedo, OnRedo>{

	public OnRedoListener(OnRedo execute) {
		super(execute, null);				
	}

	@Override
	public void execute() {
		this.mCallbackExecute.redo();
	}

	@Override
	public void unexecute() {
		
	}

	@Override
	public boolean isReversible() {
		return true;
	}

	@Override
	public void setExecuteCallback(OnRedo callback) {
		this.mCallbackExecute = callback;
	}

	@Override
	public void setUnExecuteCallback(OnRedo callback) {
	}
	
}
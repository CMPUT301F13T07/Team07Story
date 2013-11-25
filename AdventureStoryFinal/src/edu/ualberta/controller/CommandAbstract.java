package edu.ualberta.controller;

import edu.ualberta.controller.CallbackIntefaces.OnCallback;

/**
 * <code>Command</code> is the abstract classs for the command. This must be
 * overriden.
 * 
 * @version 1.0
 */
public abstract class CommandAbstract<C, U extends OnCallback> {
	protected C mCallbackExecute;
	protected U mCallbackUnExecute;

	public CommandAbstract(C execute, U unExecute) {
		mCallbackExecute = execute;
		mCallbackUnExecute = unExecute;
	}

	abstract public void execute();

	abstract public void unexecute();

	abstract public boolean isReversible();

	abstract public void setExecuteCallback(C callback);

	abstract public void setUnExecuteCallback(U callback);
}

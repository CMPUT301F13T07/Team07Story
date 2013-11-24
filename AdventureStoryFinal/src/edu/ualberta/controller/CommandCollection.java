/**
 * 
 */
package edu.ualberta.controller;

import edu.ualberta.data.DbManager;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Page;
import android.app.Activity;
import android.content.Context;

/**
 * <code>CommandCollection</code> is a container for Command's subclass. These
 * are all implementation of Command Pattern as discussed in Patterns-01.pdf in
 * class.
 * 
 * 
 * @author JoeyAndres
 * @version 1.0
 */
public class CommandCollection {
	/**
	 * Callback interface. They all extends OnCallbackListener just to have
	 * better name for the callback.
	 */
	public static interface OnCallback {
	};

	public static interface OnAddMultimedia extends OnCallback {
		void onAddMultimedia();
	}

	public static interface OnExit extends OnCallback {
		void onExit();
	}

	public static interface OnSave extends OnCallback {
		void onSave();
	}

	public static interface OnStartPageEdit extends OnCallback {
		void onStartPageEdit();
	}

	public static interface OnUndo extends OnCallback {
		void undo();
	}
	
	public static interface OnRedo extends OnCallback {
		void redo();
	}

	/**
	 * asdjnfkassd
	 * 
	 * @author jandres
	 * 
	 */
	public static interface OnActivityResult extends OnCallback {
		void onActivityResult();
	}

	/**
	 * <code>Command</code> is the abstract classs for the command. This must be
	 * overriden.
	 * 
	 * @version 1.0
	 */
	public static abstract class CommandAbstract<C, U extends OnCallback> {
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

	/**
	 * <code>exitActivityCommand</code> contains the call back for exiting
	 * current activity.
	 * 
	 */
	static public class OnExitListener extends CommandAbstract<OnExit, OnUndo> {
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

	/**
	 * Command for saving.
	 */
	static public class OnSaveListener extends CommandAbstract<OnSave, OnUndo> {
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

	/**
	 * Listener when starting PageEdit view.
	 * 
	 * @author Joey Andres
	 */
	static public class OnStartPageEditListener extends
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

	static public class OnActivityResultListener extends
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
}

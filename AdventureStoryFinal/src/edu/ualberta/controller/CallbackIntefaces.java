package edu.ualberta.controller;

import edu.ualberta.utils.Page;

/**
 * Callback interface. They all extends OnCallbackListener just to have
 * better name for the callback.
 * 
 * <p><p>
 * Note:The Command implementing these could
 * just use OnCallbackGeneric if too lazy to give good naming. 
 */
public class CallbackIntefaces {	
	public static interface OnCallback {};

	public static interface OnCallbackGeneric{
		void callback();
	}
	
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
	
	public static interface OnDelete extends OnCallback{
		void delete();
	}
	
	public static interface OnMove extends OnCallback{
		void onMove(int pos);
	}
	
	public static interface OnActivityResult extends OnCallback {
		void onActivityResult();
	}
	
	public static interface OnCache extends OnCallback {
		void onCache(Page page);
	}
}

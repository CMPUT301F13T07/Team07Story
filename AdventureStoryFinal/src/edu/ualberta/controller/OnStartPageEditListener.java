package edu.ualberta.controller;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import edu.ualberta.adventstory.PageEditActivity;
import edu.ualberta.controller.CallbackIntefaces.*;

/**
 * Listener when starting PageEdit view.
 * 
 * @author Joey Andres
 */
public class OnStartPageEditListener extends
		CommandAbstract<OnStartPageEdit, OnUndo> {
	private static final int START_EDITPAGE_RESULTCODE = 1;
			
	Activity mActivity;

	public OnStartPageEditListener(Activity activity) {
		super(null, null);
		mActivity = activity;
	}

	@Override
	public void execute() {
		startPageEditActivity();
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
	}

	@Override
	public void setExecuteCallback(OnStartPageEdit callback) {
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		// TODO Auto-generated method stub
	}

	/**
	 * starts PageEditActivity().
	 */
	private void startPageEditActivity() {
		mActivity.startActivityForResult(new Intent(mActivity,
				PageEditActivity.class), START_EDITPAGE_RESULTCODE);
	}
}
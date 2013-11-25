package edu.ualberta.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import edu.ualberta.adventstory.HelpActivity;
import edu.ualberta.adventstory.SettingsActivity;
import edu.ualberta.controller.CallbackIntefaces.OnStartSettingsActivity;
import edu.ualberta.controller.CallbackIntefaces.OnUndo;

public class OnStartSettingsActivityListener extends
		CommandAbstract<OnStartSettingsActivity, OnUndo> {

	Activity mActivity;
	
	/**
	 * Don't use this construtor as we don't allow costum callback.
	 * @param execute
	 * @param unExecute
	 */
	private OnStartSettingsActivityListener(OnStartSettingsActivity execute,
			OnUndo unExecute) {
		super(null, null);		
	}
	
	public OnStartSettingsActivityListener(Activity activity){
		super(null, null);
		mActivity = activity;
	}

	@Override
	public void execute() {
		Intent intent = new Intent(mActivity, SettingsActivity.class);		
		mActivity.startActivity(intent);
	}

	@Override
	public void unexecute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReversible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setExecuteCallback(OnStartSettingsActivity callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		// TODO Auto-generated method stub
		
	}

}

package edu.ualberta.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import edu.ualberta.adventstory.HelpActivity;
import edu.ualberta.controller.CallbackIntefaces.OnStartHelpActivity;
import edu.ualberta.controller.CallbackIntefaces.OnUndo;

/**
 * OnStartHelpActivityListener that allows an activity to connect
 * to a help page.
 *
 */
public class OnStartHelpActivityListener extends
		CommandAbstract<OnStartHelpActivity, OnUndo> implements
		OnStartHelpActivity {
	Activity mActivity;	// Caller activity.
	String mHtmlPage;		// html Help page.
	/**
	 * Don't use this constructor.
	 * @param execute 
	 * @param unExecute
	 */
	private OnStartHelpActivityListener(OnStartHelpActivity execute,
			OnUndo unExecute) {
		super(null, null);
	}
	
	/**
	 * @param activity The current activity.
	 * @param page	   *.html file in assets/HelpHtml/ 
	 */
	public OnStartHelpActivityListener(Activity activity, String htmlPage){
		super(null, null);
		mActivity = activity;
		mHtmlPage = htmlPage;
	}

	@Override
	public void onStartHelpActivity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute() {
		Intent intent = new Intent(mActivity, HelpActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("htmlPage", mHtmlPage);
		intent.putExtras(bundle);
		mActivity.startActivity(intent);
	}

	@Override
	public void unexecute() {
		// NONE.
	}

	@Override
	public boolean isReversible() {
		return false;
	}

	@Override
	public void setExecuteCallback(OnStartHelpActivity callback) {
		// NONE.		
	}

	@Override
	public void setUnExecuteCallback(OnUndo callback) {
		// NONE.		
	}

}

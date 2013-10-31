/*
 * ActivityExtended.java
 * - Place methods here that you want to call when outside an Activity.
 */
package edu.ualberta.adventstory;

import android.app.Activity;
import android.os.Bundle;
import edu.ualberta.database.Db;

abstract public class ActivityExtended extends Activity{
	protected boolean mOnVideoViewPreview = false;
	protected String mVideoDirectory = null;
	public Db database;
	
	public ActivityExtended() {
		super();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Load savedInstanceState.
		if(savedInstanceState != null){
			mOnVideoViewPreview = savedInstanceState.getBoolean("Mode");
			if(mOnVideoViewPreview){
				if( savedInstanceState.getString("VideoDirectory") != null){
						mOnVideoViewPreview = savedInstanceState.getBoolean("Mode");
						mVideoDirectory = savedInstanceState.getString("VideoDirectory");					
				}
			}
		}
        // Create instance of the database
        database = new Db(this);
        database.open();
	}
	
	// Override these methods.
	
	// Override to display video. The argument is directory of video.	
	public void switchToVideoViewPreview(String directory){
		// Creates and Open new VideoViewPreview.
		VideoViewPreview vvp = new VideoViewPreview(directory, this);
		mOnVideoViewPreview = true;
		mVideoDirectory = directory;
	}
	// Override to revert to original layout of Activity.
	public void switchToOriginalLayout(){
		mOnVideoViewPreview = false;
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle objects){
		objects.putBoolean("Mode", mOnVideoViewPreview);
		objects.putString("VideoDirectory", mVideoDirectory);
	}
}

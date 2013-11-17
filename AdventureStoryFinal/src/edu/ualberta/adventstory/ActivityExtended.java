package edu.ualberta.adventstory;

import edu.ualberta.multimedia.TObservable;
import android.app.Activity;
import android.os.Bundle;

/**
 * <code>ActivityExtended</code> is specialization of Activity class to allow anyone who 
 * inherit from these to Acquire MVC's TObserver interface. This allows the Views (this)
 * whenever critical parts of the model are updated.
 * 
 * @author Joey Andres
 *
 */
public class ActivityExtended extends Activity implements TObserver<TObservable>{
	// Not the base activity. Transfer this to the base activity then.
	protected DataSingleton mDataSingleton;
	
	protected boolean mOnVideoViewPreview = false;
	protected String mVideoDirectory = null;
	//public Db database;
	
	public ActivityExtended() {
		super();
	}
	
	/*
	 * onCreate for this one allows the subclasses to use VideoPreview.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
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
	
	/* 
	 * Stuff to be saved when onDestroy is called. This will
	 * allow ActivityExtended to recall last state such as,
	 * where we watching a video or not the last time we change
	 * orientation.
	 */
	@Override
	public void onSaveInstanceState(Bundle objects){
		objects.putBoolean("Mode", mOnVideoViewPreview);
		objects.putString("VideoDirectory", mVideoDirectory);
	}

	/**
	 * Part of the MVC template.
	 */
	@Override
	public void update(TObservable s) {}
	
	/**
	 * <code>localUpdate</code> can be called by MVC update or other parts of teh code since
	 * doesn't require any arguments.
	 */
	public void localUpdate(){} 
}

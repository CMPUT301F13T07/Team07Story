package edu.ualberta.adventstory;

import java.io.IOException;

import edu.ualberta.multimedia.TObservable;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

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
	
	static final int PLAY_VIDEO_REQUESTCODE = 0;
	
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
		mDataSingleton = (DataSingleton)getApplicationContext();
	}
	
	// Override these methods.
	
	// Override to display video. The argument is directory of video.	
	public void playVideo(String directory){
		Intent i = new Intent(this, VideoPlayerActivity.class);
		
		Bundle b = new Bundle();
		b.putString("VideoDirectory", directory);
		
		i.putExtras(b);
		
		startActivityForResult(i, PLAY_VIDEO_REQUESTCODE);
	}
	
	/* 
	 * Stuff to be saved when onDestroy is called. This will
	 * allow ActivityExtended to recall last state such as,
	 * where we watching a video or not the last time we change
	 * orientation.
	 */
	@Override
	public void onSaveInstanceState(Bundle objects){
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
	
	/**
	 * <code>exit</code> is called to go back to the <code>StartActivity</code>.
	 */
	protected void exit(){
		Intent startActivityIntent = new Intent(this, StartActivity.class);
		startActivity(startActivityIntent);
	}
}

/*
 * ActivityExtended.java
 * - Place methods here that you want to call when outside an Activity.
 */
package com.example.proto02;

import com.example.controller.OnSwipeTouchListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

abstract public class ActivityExtended extends Activity{
	protected boolean mOnVideoViewPreview = false;
	protected String mVideoDirectory = null;
	
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

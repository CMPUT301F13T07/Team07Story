/*
 * ActivityExtended.java
 * - Place methods here that you want to call when outside an Activity.
 */
package com.example.proto02;

import android.app.Activity;

abstract public class ActivityExtended extends Activity{
	
	public ActivityExtended() {
		super();
	}
	
	// Override these methods.
	
	// Override to display video. The argument is directory of video.
	abstract public void switchToVideoViewPreview(String directory); 
	// Override to revert to original layout of Activity.
	abstract public void switchToOriginalLayout();

}

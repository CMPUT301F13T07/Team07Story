package edu.ualberta.controller;

import edu.ualberta.adventstory.ActivityExtended;
import edu.ualberta.adventstory.DataSingleton;
import edu.ualberta.adventstory.R;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.Video;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * <code>VideoController</code> is part of the MVC pattern
 * that act as a bridge between model and the view.
 * @author Joey
 *
 */
public class VideoController extends MultimediaController{
	@Override
	public void play(Context context, MultimediaAbstract ma) {
		DataSingleton ds = (DataSingleton)context.getApplicationContext();
		((ActivityExtended) ds.getCurrentActivity()).playVideo(ma.getFileDir());
	}
	
	public Bitmap loadBitmap(Context context, MultimediaAbstract ma){
		return BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_video);
	}
}

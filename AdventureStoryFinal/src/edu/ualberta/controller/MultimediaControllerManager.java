package edu.ualberta.controller;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import edu.ualberta.adventstory.ActivityExtended;
import edu.ualberta.adventstory.DataSingleton;
import edu.ualberta.controller.CommandCollection.Command;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.SoundClip;
import edu.ualberta.multimedia.Video;

/**
 * <code>MultimediaControllerManager</code> merges all 3 controller into one to abstract
 * developer from choosing which.
 * 
 * <p>
 * Note that this can only be initialize via Factory method to ensure one instance is only 
 * running.
 * 
 * @author Joey
 *
 */
public class MultimediaControllerManager {
	static MultimediaControllerManager mcm = new MultimediaControllerManager();
		
	public static MultimediaControllerManager MultimediaControllerFactory(){		
		return mcm;
	}
	
	public static void play(Context context, MultimediaAbstract ma){
		if(ma.getClass().toString().equals(Picture.class.toString())){
			// Don't do anything.
		}else if(ma.getClass().toString().equals(SoundClip.class.toString())){
			
		}else if(ma.getClass().toString().equals(Video.class.toString())){
			VideoController vc = new VideoController();
			vc.play(context, ma);
		}else{
			// Unknown state.
		}
	}
	
	public static Bitmap loadBitmap(Context context, MultimediaAbstract ma){
		if(ma.getClass().toString().equals(Picture.class.toString())){
			PictureController pc = new PictureController();
			return pc.loadBitmap(context, ma);
		}else if(ma.getClass().toString().equals(SoundClip.class.toString())){
			SoundClipController scc = new SoundClipController();
			return scc.loadBitmap(context, ma);
		}else if(ma.getClass().toString().equals(Video.class.toString())){
			VideoController vc = new VideoController();
			return vc.loadBitmap(context, ma);
		}else{
			// Unknown state.
			return null;
		}
	}
}
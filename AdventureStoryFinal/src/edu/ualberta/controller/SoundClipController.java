package edu.ualberta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import edu.ualberta.adventstory.R;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Utility;

/**
 * <code>SoundClipController</code> helpse abstract the loadBitmap, Play, Pause and Stop method
 *  as this is system dependent therefore not suitable in the model package.
 * @author Joey
 *
 */
public class SoundClipController extends MultimediaController {
	 MediaPlayer mediaPlayer = new MediaPlayer();
	 
	@Override
	public void play(Context context, MultimediaAbstract ma) {
		File file = new File(ma.getFileDir());
		byte[] byteArray;		
	    try {
	    	// Load byte Array.
	    	byteArray = Utility.load(file, context);
	    	
	        // create temp file that will hold byte array
	    	File tempMp3 = File.createTempFile("kurchina", "mp3", 
	    										context.getCacheDir());
	        tempMp3.deleteOnExit();
	        FileOutputStream fos = new FileOutputStream(tempMp3);
	        fos.write(byteArray);
	        fos.close();

	        // Tried passing path directly, but kept getting 
	        // "Prepare failed.: status=0x1"
	        // so using file descriptor instead
	        FileInputStream fis = new FileInputStream(tempMp3);
	        mediaPlayer.setDataSource(fis.getFD());

	        mediaPlayer.prepare();
	        mediaPlayer.start();
	    } catch (IOException ex) {
	        String s = ex.toString();
	        ex.printStackTrace();
	    }
	}

	@Override
	public Bitmap loadBitmap(Context context, MultimediaAbstract ma) {
		return BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_audio);
	}

}

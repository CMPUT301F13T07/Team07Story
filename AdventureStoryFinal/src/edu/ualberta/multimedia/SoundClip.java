package edu.ualberta.multimedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import edu.ualberta.adventstory.R;
import edu.ualberta.utils.Utility;

public class SoundClip extends MultimediaAbstract {
	
	public SoundClip(int id, int index, String file_dir) {
		super(id, index, file_dir);
	}
	public SoundClip(int index, String file_dir) {
		super(index, file_dir);
	}
	
	public void play() {
		File file = new File(MultimediaDB.getSoundDirectory(super.id, super.context));
		byte[] byteArray;
		
	    try {
	    	// Load byte Array.
	    	byteArray = Utility.load(file, super.context);
	    	
	        // create temp file that will hold byte array
	    	File tempMp3 = File.createTempFile("kurchina", "mp3", 
	    										super.context.getCacheDir());
	        tempMp3.deleteOnExit();
	        FileOutputStream fos = new FileOutputStream(tempMp3);
	        fos.write(byteArray);
	        fos.close();

	        // Tried reusing instance of media player
	        // but that resulted in system crashes...  
	        MediaPlayer mediaPlayer = new MediaPlayer();

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
	public Bitmap loadPhoto() {
		if(super.id == -1){
			return BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_audio);
		}else{
			String tempFileName = MultimediaDB.getBitmapDirectory(id, context);
			return BitmapFactory.decodeFile(tempFileName);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		return super.equals(obj);
	}
}

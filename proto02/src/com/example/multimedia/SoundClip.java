package com.example.multimedia;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.utils.*;

public class SoundClip extends MultimediaAbstract{	
	public SoundClip(int id, int index, int pictureId, 
			Context context){
		super(id, index, pictureId, MultimediaAbstract.SOUND, context);		
	}
	
	void playSound() {
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
}

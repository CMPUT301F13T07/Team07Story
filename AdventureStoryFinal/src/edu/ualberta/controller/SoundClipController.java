package edu.ualberta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import edu.ualberta.adventstory.R;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Utility;

/**
 * <code>SoundClipController</code> helpse abstract the loadBitmap, Play, Pause
 * and Stop method as this is system dependent therefore not suitable in the
 * model package.
 * 
 * @author Joey
 * 
 */
public class SoundClipController extends MultimediaController {

	public static MediaPlayer mediaPlayer;
	private static SoundPool soundPool;
	public static boolean isplayingAudio = false;

	public static void playAudio(Context c, String fileName) {
		mediaPlayer = MediaPlayer.create(c, Uri.parse(fileName));
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		if (!mediaPlayer.isPlaying()) {
			isplayingAudio = true;
			mediaPlayer.start();
		}
	}

	public static void stopAudio() {
		isplayingAudio = false;
		mediaPlayer.stop();
	}

	@Override
	public Bitmap loadBitmap(Context context, MultimediaAbstract ma) {
		return BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_audio);
	}

	@Override
	public void play(Context context, MultimediaAbstract ma) {
		if(isplayingAudio == true){
			stopAudio();
		}else{
			playAudio(context, ma.getFileDir());
		}
	}
}

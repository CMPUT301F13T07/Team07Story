package com.example.proto02;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.content.Context;

import org.apache.commons.io.FileUtils;

import com.example.controller.DoubleTapGestureDetector;
import com.example.multimedia.MultimediaDB;

public class VideoViewPreview extends SurfaceView implements SurfaceHolder.Callback,
													MediaPlayer.OnCompletionListener{
	MediaPlayer mMediaPlayer;
	SurfaceHolder sh;
	Context mContext;
	
	public VideoViewPreview(Context context){
		super(context);
		mContext = context;
				
		sh = getHolder();
		sh.addCallback(this);
		sh.setFixedSize(400, 300);
		sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mMediaPlayer = new MediaPlayer();
		File file = new File("/sdcard/Movies/RickRoll D.mp4");
		
		try {
			byte[] byteArray = MultimediaDB.
					load(file, mContext);
			MultimediaDB mdb = new MultimediaDB(mContext);
			mdb.open();
			long index = mdb.insertVideo("rickroll", ".mp4", byteArray);
			mdb.close();
			
			//File temp = new File("temp.mp4");
			//MultimediaDB.save(temp, byteArray, getBaseContext());
			File tempMp4 = File.createTempFile("temp", "mp4", mContext.getCacheDir());
			tempMp4.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(tempMp4);
			mdb.open();
	        fos.write(byteArray);
	        fos.close();
	        mdb.close();
			
			FileInputStream fis = new FileInputStream(tempMp4);
			mMediaPlayer.setDataSource(fis.getFD());	
			mMediaPlayer.prepare();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
		// Layout for mOuterLayout.		
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);				
		this.setLayoutParams(lp);
		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		//lockLandscape();
		mMediaPlayer.setDisplay(sh);
		mMediaPlayer.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mContext.getApplicationContext();
		
	}
	
	public MediaPlayer getMediaPlayer(){
		return mMediaPlayer;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		switcthToOriginalLayout();		
	}	
	
		
	public void switcthToOriginalLayout(){
		PageViewActivity pva = 
				(PageViewActivity)((ChooseYourAdventure07)mContext.
						getApplicationContext()).getCurrentActivity();
		pva.switchOriginalLayout();
	}
	
	public void lockLandscape(){
		PageViewActivity pva = 
				(PageViewActivity)((ChooseYourAdventure07)mContext.
						getApplicationContext()).getCurrentActivity();
		pva.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
}
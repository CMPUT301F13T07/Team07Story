package com.example.proto02;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.pm.ActivityInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.media.MediaPlayer;
import android.content.Context;
import android.os.SystemClock;

import com.example.multimedia.MultimediaDB;
import com.example.utils.Utility;

public class VideoViewPreview extends SurfaceView implements SurfaceHolder.Callback,
													MediaPlayer.OnCompletionListener{
	MediaPlayer mMediaPlayer;
	SurfaceHolder sh;
	Context mContext;	
	ImageView mImageView;
	boolean isPlay;
	
	@SuppressWarnings("deprecation")
	public VideoViewPreview(String directory, Context context){
		super(context);
		mContext = context;
				
		sh = getHolder();
		sh.addCallback(this);
		sh.setFixedSize(400, 300);
		sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mMediaPlayer = new MediaPlayer();
		
		File file = new File(directory);
		try {
			byte[] byteArray = Utility.load(file, mContext);
			MultimediaDB mdb = new MultimediaDB(mContext);
			mdb.open();
			mdb.close();
			
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
		
	long currentTime;
	@Override
	public boolean onTouchEvent(MotionEvent me){
		if(isPlay){
			if((SystemClock.uptimeMillis()-currentTime) > 200){
				mMediaPlayer.pause();
				isPlay = false;
				currentTime = SystemClock.uptimeMillis();	
			}		
		}else{
			if((SystemClock.uptimeMillis()-currentTime) > 200){
				mMediaPlayer.start();
				isPlay = true;
				currentTime = SystemClock.uptimeMillis();
			}
		}
		return true;
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
		isPlay = true;
		currentTime = SystemClock.uptimeMillis();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mContext.getApplicationContext();
		mMediaPlayer.stop();
	}
	
	public MediaPlayer getMediaPlayer(){
		return mMediaPlayer;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		switcthToOriginalLayout();		
	}	
	
		
	public void switcthToOriginalLayout(){
		ActivityExtended ae = 
				(ActivityExtended)((ChooseYourAdventure07)mContext.
						getApplicationContext()).getCurrentActivity();
		ae.switchOriginalLayout();
	}
	
	public void lockLandscape(){
		PageViewActivity pva = 
				(PageViewActivity)((ChooseYourAdventure07)mContext.
						getApplicationContext()).getCurrentActivity();
		pva.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
}
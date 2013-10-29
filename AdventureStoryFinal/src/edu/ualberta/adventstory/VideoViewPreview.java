package edu.ualberta.adventstory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.SystemClock;


import edu.ualberta.multimedia.MultimediaDB;
import edu.ualberta.utils.Utility;

import edu.ualberta.adventstory.R;
import edu.ualberta.controller.OnSwipeTouchListener;

public class VideoViewPreview extends SurfaceView implements SurfaceHolder.Callback,
															 View.OnTouchListener{
	MediaPlayer mMediaPlayer;
	SurfaceHolder mSurfaceHolder;
	Context mContext;	
	ImageView mImageView;
	boolean mIsPlaying;
	String mDirectory;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public VideoViewPreview(String directory, Context context){
		super(context);
		mContext = context;		
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setFixedSize(400, 300);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mDirectory = directory;
		
		// CREATE LAYOUT.
		// Layout for mOuterLayout.		
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);				
		this.setLayoutParams(lp);
		
		ImageView iv = new ImageView(mContext);
		iv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				switchToOriginalLayout();
			}
		});		
		
		RelativeLayout.LayoutParams rLP = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		RelativeLayout rL = new RelativeLayout(mContext);
		rL.addView(this);
		
		ImageView ivExitButton = new ImageView(mContext);
		ivExitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				switchToOriginalLayout();
			}
		});		
		ivExitButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_exit));
		
		// RelativeLayout.LayoutParam for just the button.
		RelativeLayout.LayoutParams exitLP = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		exitLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rL.addView(ivExitButton, exitLP);

		final ImageView ivPlayButton = new ImageView(mContext);
		ivPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
		ivPlayButton.setScaleX(3.0f);
		ivPlayButton.setScaleY(3.0f);
		ivPlayButton.setAlpha(0.0f);
		ivPlayButton.setOnClickListener(new OnClickListener(){
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v){
				if(isPlaying()){
					// Pause.
					onPlayPause();
					ivPlayButton.setAlpha(1.0f);
				}else{
					// Play.
					onPlayPause();
					// Show button.
					ivPlayButton.setAlpha(0.0f);
				}
			}
		});
		
		// RelativeLyout.LayoutParam for the play button.
		RelativeLayout.LayoutParams playLP = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		playLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		playLP.addRule(RelativeLayout.CENTER_VERTICAL);
		// Set Width/Height manually.
		playLP.width = 100;
		playLP.height = 100;		
		rL.addView(ivPlayButton, playLP);
		((ActivityExtended)mContext).addContentView(rL, rLP); 
		((ActivityExtended)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.setOnTouchListener(this);
	}
	
	
		
	void loadVideo(){
		mMediaPlayer = new MediaPlayer();
		
		File file = new File(mDirectory);
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
		
		// Exit when exit.
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			public void onCompletion(MediaPlayer mp){
				switchToOriginalLayout();				
			}
		});
		
		final SurfaceView sv = this;
	}
			
	public void onPlayPause(){
		if(mIsPlaying){
			mMediaPlayer.pause();
			mIsPlaying = false;			
		}else{
			mMediaPlayer.start();
			mIsPlaying = true;
		}	
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
		loadVideo();
		mMediaPlayer.setDisplay(mSurfaceHolder);
		mMediaPlayer.start();
		mIsPlaying = true;
		
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
		fadeIn.setDuration(1000);
		this.startAnimation(fadeIn);
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
	
		
	public void switchToOriginalLayout(){		
		ActivityExtended ae = 
				(ActivityExtended)((ChooseYourAdventure07)mContext.
						getApplicationContext()).getCurrentActivity();
		((ActivityExtended)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		ae.switchToOriginalLayout();
	}
	
	public void lockLandscape(){
		PageViewActivity pva = 
				(PageViewActivity)((ChooseYourAdventure07)mContext.
						getApplicationContext()).getCurrentActivity();
		pva.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	public boolean isPlaying(){
		return mIsPlaying;
	}


	// TODO: Create a super class SurfaceView.
	// TODO: Make the a threshold parameter.
	// TODO: Make a controller for this.
	private int _xOld;
	private int _yOld;
    private float deltaX;
    private float deltaY;
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		WindowManager wm = ((ActivityExtended)mContext).getWindowManager();
		Display d = wm.getDefaultDisplay();
		Point p = new Point();
		d.getSize(p);
	    final int X = (int) event.getRawX();
	    final int Y = (int) event.getRawY();

	    switch (event.getAction() & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN:
	            deltaX = X - _xOld;
	            deltaY = Y - _yOld;
	            break;
	        case MotionEvent.ACTION_UP:
	        	if(this.getY() <= (p.y-150)){
	        		// Place back to original position.
	        		this.setY(0);
	        		this.setX(0);
	        	}
	            break;
	        case MotionEvent.ACTION_POINTER_DOWN:
	            break;
	        case MotionEvent.ACTION_POINTER_UP:
	        	if(this.getY() <= (p.y-150)){
	        		// Place back to original position.
	        		this.setY(0);
	        		this.setX(0);
	        	}
	            break;
	        case MotionEvent.ACTION_MOVE:	            
	            float newX = X - deltaX;
	            float newY = Y - deltaY;
	            _xOld = X;
	            _xOld = Y;
	            if(this.getY() > (p.y-200)) this.switchToOriginalLayout();
	            this.setY(newY);	            
	            break;
	    }
	    this.invalidate();
	    return true;
	}
}
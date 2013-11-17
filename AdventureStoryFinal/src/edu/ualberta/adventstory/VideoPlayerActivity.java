package edu.ualberta.adventstory;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayerActivity extends ActivityExtended {

	String mDirectory;
	MediaController mMediaController;
	VideoView mVideoView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_video_view);
		
		Bundle bundle = getIntent().getExtras();
		mDirectory = bundle.getString("VideoDirectory");
		
		mVideoView = (VideoView) findViewById(R.id.videoView);
		mMediaController = new MediaController(this);
		mMediaController.setAnchorView(mVideoView);
		mVideoView.setMediaController(mMediaController);
		mVideoView.setVideoPath(mDirectory);
		mVideoView.requestFocus();
		mVideoView.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp) {
				finish();
			}			
		});
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		mVideoView.start();
	}
	
	@Override
	protected void exit(){
		super.exit();
	}
}
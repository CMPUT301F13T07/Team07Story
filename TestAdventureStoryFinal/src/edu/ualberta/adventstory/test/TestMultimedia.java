/*
 * TestMultimedia.java
 * - Assuming that you have the following files in sdcard.
 */
package edu.ualberta.adventstory.test;

import edu.ualberta.adventstory.R;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.Picture;
import edu.ualberta.multimedia.SoundClip;
import edu.ualberta.multimedia.Video;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

public class TestMultimedia extends AndroidTestCase {
	Picture mPictureNullDirectory;
	Picture mPictureValidDirectory;
	SoundClip mSound;
	Video mVideo;
	
	static final int ID1 = 0;
	static final int ID2 = 1;
	static final int ID3 = 2;
	static final int ID4 = 3;
	
	static final int INDEX1 = 0;
	static final int INDEX2 = 2;
	static final int INDEX3 = 1;
	static final int INDEX4 = 3;
	
	@Override
	protected void setUp(){
		mPictureNullDirectory = new Picture(ID1, INDEX1, null);	
		mPictureValidDirectory = new Picture(ID2, INDEX2, "/sdcard/Pictures/troll.jpg");
		mSound = new SoundClip(ID3, INDEX3, "/sdcard/Music/pewpew.mp3");
		mVideo = new Video(ID4, INDEX4, "/sdcard/Movies/chtr.mp4");
	}
	
	public void testgetID(){
		assertTrue(mPictureNullDirectory.getID()==ID1);
		assertTrue(mPictureValidDirectory.getID()==ID2);
		assertTrue(mSound.getID()==ID3);
		assertTrue(mVideo.getID()==ID4);
	}
	
	public void testgetIndex(){
		assertTrue(mPictureNullDirectory.getIndex() == INDEX1);
		assertTrue(mPictureValidDirectory.getIndex() == INDEX2);
		assertTrue(mSound.getIndex() == INDEX3);
		assertTrue(mVideo.getIndex() == INDEX4);
	}
	
	public void testgetIsSelected(){
		// All must be not selected by default. This attribute is needed
		// by PageEditActivity.
		assertFalse(mPictureNullDirectory.getIsSelected());
		assertFalse(mPictureValidDirectory.getIsSelected());
		assertFalse(mSound.getIsSelected());
		assertFalse(mVideo.getIsSelected());
	}
	
	public void testsetID(){
		mPictureNullDirectory.setID(ID4);
		mPictureValidDirectory.setID(ID3);
		mSound.setID(ID2);
		mVideo.setID(ID1);
		
		assertFalse(mPictureNullDirectory.getID()==ID1);
		assertFalse(mPictureValidDirectory.getID()==ID2);
		assertFalse(mSound.getID()==ID3);
		assertFalse(mVideo.getID()==ID4);
		
		assertTrue(mPictureNullDirectory.getID()==ID4);
		assertTrue(mPictureValidDirectory.getID()==ID3);
		assertTrue(mSound.getID()==ID2);
		assertTrue(mVideo.getID()==ID1);
	}
	
	public void testsetIndex(){
		
	}
	
	// Compare Bitmaps pixel by pixel.
	private boolean compareBitmap(Bitmap bm1, Bitmap bm2){
		if(bm1.getHeight() != bm2.getHeight() ||
		   bm1.getWidth() != bm2.getWidth()){
			return false;
		}
		
		for(int i = 0; i < bm1.getWidth(); i++){
			for(int j = 0; j < bm1.getHeight(); j++ ){
				if(bm1.getPixel(i, j) != bm2.getPixel(i, j)){
					return false;
				}
			}
		}
		return true;
	}
	
	public void testloadBitmap(){
		Bitmap bmNullFromLoad = mPictureNullDirectory.loadPhoto(getContext());
		Bitmap bmNullFromResource = BitmapFactory.decodeResource(
				getContext().getResources(), R.drawable.ic_picture);
		assertTrue( compareBitmap(bmNullFromLoad, bmNullFromResource));
		
		Bitmap bmValidFromLoad = mPictureValidDirectory.loadPhoto(getContext());
		Bitmap bmValidFromSd = BitmapFactory.decodeFile(mPictureValidDirectory.getFileDir());
		assertTrue( compareBitmap(bmValidFromLoad, bmValidFromSd));
		
		Bitmap bmSound = mSound.loadPhoto(getContext());
		Bitmap bmFromResource = BitmapFactory.decodeResource(
				getContext().getResources(), R.drawable.ic_audio);
		assertTrue( compareBitmap(bmSound, bmFromResource));
		
		// To prove that compareBitmap don't arbitrarily return true.
		assertFalse(compareBitmap(bmNullFromLoad, bmSound));
		
		Bitmap bmVideo = mVideo.loadPhoto(getContext());
		bmFromResource = BitmapFactory.decodeResource(
				getContext().getResources(), R.drawable.ic_video);
		assertTrue( compareBitmap(bmVideo, bmFromResource));
	}
}

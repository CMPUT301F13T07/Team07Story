package edu.ualberta.multimedia;

import edu.ualberta.adventstory.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Display;
import android.view.WindowManager;

public class Picture extends MultimediaAbstract{
	public static enum IMAGESIZE{
		SMALL,
		MEDIUM,
		LARGE
	}
	
	protected IMAGESIZE mImageSize;
	
	public Picture(int id, int index, String file_dir){
		super(id, index, file_dir);	
		mImageSize = IMAGESIZE.MEDIUM;
	}
	
	public Picture(int index, String file_dir) {
		super(index, file_dir);
		mImageSize = IMAGESIZE.MEDIUM;
	}
	
	public Picture(int id, int index, String file_dir, IMAGESIZE imageSize){
		super(id, index, file_dir);
		mImageSize = imageSize;		
	}

	@Override
	public boolean equals( Object obj ){
		if( obj == null ) return false;
		if( obj == this ) return true;
		return super.equals(obj);
	}
}

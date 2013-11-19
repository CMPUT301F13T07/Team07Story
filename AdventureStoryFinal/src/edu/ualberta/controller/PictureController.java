package edu.ualberta.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Display;
import android.view.WindowManager;
import edu.ualberta.adventstory.R;
import edu.ualberta.multimedia.MultimediaAbstract;

/**
 * <code>PictureController</code> helpse abstract the loadBitmap method as
 * this is system dependent therefore not suitable in the model package.
 * @author Joey
 *
 */
public class PictureController extends MultimediaController {

	@Override
	public void play(Context context, MultimediaAbstract ma) {
		// Unknown sate.
	}

	@Override
	public Bitmap loadBitmap(Context context, MultimediaAbstract ma) {
		if( ma.getID() == -1 || ma.getFileDir() == null){
			return BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_picture);
		}else{
			WindowManager wm = ((Activity)context).getWindowManager();
			Display d = wm.getDefaultDisplay();
			Bitmap bm = BitmapFactory.decodeFile(ma.getFileDir());
			
			// Force width and height to be within 1/2 screen's limit.			
			if( bm.getWidth() > d.getWidth()/2 ){
				bm = getResizedBitmap(bm, bm.getHeight(), d.getWidth()/2);
			}
			if( bm.getHeight() > d.getHeight()/2){
				bm=getResizedBitmap(bm, d.getHeight()/2, bm.getWidth());
			}
			return bm;
		}
	}
	
	/**
	 * <code>getReziedBitmap</code> resize Bitmap.
	 * 
	 * @param bm	Bitmap that is being resized.
	 * @param newHeight	 	target new height.
	 * @param newWidth		target new width.
	 * @return
	 */
	private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}

}

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
import edu.ualberta.multimedia.Picture;

/**
 * <code>PictureController</code> helps abstract the loadBitmap method as
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
			
			Picture p = (Picture)ma;
			
			if( p.getPictureSize() == Picture.SMALL){
				int width = (int) (d.getWidth()/7.5);
				int height = (int) (d.getWidth()/7.5);			
				bm = getResizedBitmap(bm, height, width);
			}else if(p.getPictureSize() == Picture.MEDIUM){
				int width = (int) (d.getWidth()/5.0);
				int height = (int) (d.getWidth()/5.0);			
				bm = getResizedBitmap(bm, height, width);
			}else{
				int width = (int) (d.getWidth()/2.5);
				int height = (int) (d.getWidth()/2.5);			
				bm = getResizedBitmap(bm, height, width);
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

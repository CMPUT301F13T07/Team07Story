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
	public Picture(int id, int index, String file_dir){
		super(id, index, file_dir);	
	}
	public Picture(int index, String file_dir) {
		super(index, file_dir);
	}
	
	@Override
	public void play(Context context){
		// Enter photo effects...		
	}
	
	// Override the following so that setting id will also set mPictureId
	// and vice versa.
	@Override
	public void setID(int id){
		super.id = id;
		//super.pictureId = id;
	}
	
	@Override
	public Bitmap loadPhoto(Context context){
		if( super.id == -1 || file_dir == null){
			return BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_picture);
		}else{
			WindowManager wm = ((Activity)context).getWindowManager();
			Display d = wm.getDefaultDisplay();
			Bitmap bm = BitmapFactory.decodeFile(file_dir);
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


	
	@Override
	public boolean equals( Object obj ){
		if( obj == null ) return false;
		if( obj == this ) return true;
		return super.equals(obj);
	}
}

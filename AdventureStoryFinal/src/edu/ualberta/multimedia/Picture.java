package edu.ualberta.multimedia;

import edu.ualberta.adventstory.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Picture extends MultimediaAbstract{	
	public Picture(int id, int index, String file_dir){
		super(id, index, file_dir);	
	}
	public Picture(int index, String file_dir) {
		super(index, file_dir);
	}
	
	@Override
	public void play(){
		// Enter photo effects...		
	}
	
	// Override the following so that setting id will also set mPictureId
	// and vice versa.
	@Override
	public void setID(int id){
		super.id = id;
		//super.pictureId = id;
	}
	
	/*@Override
	public void setPictureId(int pictureId){
		super.pictureId = pictureId;
		super.id = pictureId;
	}*/
	
	@Override
	public Bitmap loadPhoto(){
		if( super.id == -1 ){
			return BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_picture);
		}else{
			String tempFileName = MultimediaDB.getBitmapDirectory(id, context);
			return BitmapFactory.decodeFile(tempFileName);			
		}
	}
	
	@Override
	public boolean equals( Object obj ){
		if( obj == null ) return false;
		if( obj == this ) return true;
		return super.equals(obj);
	}
}

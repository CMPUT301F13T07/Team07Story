package edu.ualberta.multimedia;

import edu.ualberta.adventstory.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Picture extends MultimediaAbstract{	
	public Picture( int id, int index){
		super(id, index);	
	}
	
	// Override the following so that setting id will also set mPictureId
	// and vice versa.
	@Override
	public void setId(int id){
		super.id = id;
	}	
	
	@Override
	public Bitmap loadPhoto(Context context){
		return BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_picture);
	}
	
	@Override
	public boolean equals( Object obj ){
		if( obj == null ) return false;
		if( obj == this ) return true;
		return super.equals(obj);
	}
}

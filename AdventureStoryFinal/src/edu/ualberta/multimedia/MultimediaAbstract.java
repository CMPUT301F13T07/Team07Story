package edu.ualberta.multimedia;

import edu.ualberta.adventstory.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

abstract public class MultimediaAbstract {
	protected int id;
	protected int index;			// Position from first character.
	
	protected int pictureId; 		// Since sounclips are represented by 
									// picture_id represents them.
									// A value of -1 use the default picture.
	
	private boolean mIsSelected;	// DO NOT SERIALIZE THIS. This is here for editing mode.
	
	public MultimediaAbstract(int id, int index, int pictureId){
		this.id = id;
		this.index = index;
		this.pictureId = pictureId;		
		
		this.mIsSelected = false;
	}
	
	public int getId(){return id;}
	public int getIndex(){return index;}
	public int getPictureId(){return pictureId;}	
	
	public void setId(int id){ this.id = id; }
	public void setIndex(int index){this.index = index; }
	public void setPictureId(int pictureId){this.pictureId = pictureId;}
	
	public boolean getIsSelected(){ return mIsSelected; }
	public void setIsSelected(boolean val){ mIsSelected = val; }
	
	public Bitmap loadPhoto(Context context){
		return BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_multimedia);
	}
	
	public void play(Context context){
		// Override.
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(this == obj) return true;
		MultimediaAbstract ma = (MultimediaAbstract)obj;
		if( this.id == ma.id &&
			this.index == ma.index &&
			this.pictureId == ma.pictureId ){			
			return true;
		}
		return false;
	}
}

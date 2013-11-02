package edu.ualberta.multimedia;

import edu.ualberta.adventstory.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

abstract public class MultimediaAbstract{
	protected int id;
	protected int index;			// Position from first character.
		
	private boolean mIsSelected;	// DO NOT SERIALIZE THIS. This is here for editing mode.	
	
	public MultimediaAbstract(int id, int index){
		this.id = id;
		this.index = index;
		
		this.mIsSelected = false;		
	}
	
	public int getId(){return id;}
	public int getIndex(){return index;}
	
	public void setId(int id){ this.id = id; }
	public void setIndex(int index){this.index = index; }	
	
	public boolean getIsSelected(){ return mIsSelected; }
	public void setIsSelected(boolean val){ mIsSelected = val; }
	
	// TODO: Make a controller.
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
			this.index == ma.index ){
			return true;
		}
		return false;
	}
}

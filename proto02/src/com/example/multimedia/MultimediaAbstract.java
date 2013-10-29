package com.example.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.proto02.R;

abstract public class MultimediaAbstract {
	protected int id;
	protected int index;			// Position from first character.
	
	protected int pictureId; 		// Since sounclips are represented by 
									// picture_id represents them.
									// A value of -1 use the default picture.
	protected Context context;
	
	public MultimediaAbstract(int id, int index, int pictureId, Context context){
		this.id = id;
		this.index = index;
		this.pictureId = pictureId;
		this.context = context;
	}
	
	public int getId(){return id;}
	public int getIndex(){return index;}
	public int getPictureId(){return pictureId;}	
	public Context getContext(){return context;}
	
	public void setId(int id){ this.id = id; }
	public void setIndex(int index){this.index = index; }
	public void setPictureId(int pictureId){this.pictureId = pictureId;}
	public void setContext(Context context){this.context = context;}
	
	public Bitmap loadPhoto(){
		return BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_multimedia);
	}
	
	public void play(){
		// Override.
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(this == obj) return true;
		MultimediaAbstract ma = (MultimediaAbstract)obj;
		if( this.id == ma.id &&
			this.index == ma.index &&
			this.pictureId == ma.pictureId &&
			this.context == ma.context ){
			return true;
		}
		return false;
	}
}
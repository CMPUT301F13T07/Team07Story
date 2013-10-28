package com.example.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.proto02.R;

public class MultimediaAbstract {
	// TODO: Use enum class instead of these constants.
	static final int PHOTO = 0;
	static final int SOUND = 1;
	static final int VIDEO = 2;
	
	protected int id;
	protected int index;			// Position from first character.
	
	protected int pictureId; 		// Since sounclips are represented by 
									// picture_id represents them.
									// A value of -1 use the default picture.
	protected final int mediaType;
	protected Context context;
	
	public MultimediaAbstract(int id, int index, int pictureId, int mediaType,
								Context context){
		this.id = id;
		this.index = index;
		this.pictureId = pictureId;
		this.mediaType = mediaType;
		this.context = context;
	}
	
	public int getId(){return id;}
	public int getIndex(){return index;}
	public int getPictureId(){return pictureId;}
	public int getMediaType(){return mediaType;}
	public Context getContext(){return context;}
	
	public void setId(int id){ this.id = id; }
	public void setIndex(int index){this.index = index; }
	public void setPictureId(int pictureId){this.pictureId = pictureId;}
	public void setContext(Context context){this.context = context;}
	
	public Bitmap loadPhoto(){
		if( pictureId == -1){
			// Load appropriate default photo.
			switch(mediaType){
			case PHOTO:
				return BitmapFactory.decodeResource(
						context.getResources(), R.drawable.ic_picture);																
			case SOUND:
				return BitmapFactory.decodeResource(
						context.getResources(), R.drawable.ic_audio);												
			case VIDEO:
				return BitmapFactory.decodeResource(
						context.getResources(), R.drawable.ic_video);				
			default:
				// Not suppose to happen.
				return BitmapFactory.decodeResource(
						context.getResources(), R.drawable.ic_multimedia);
			}				
		}else{
			// Load from database.
			String tempFileName = MultimediaDB.getBitmapDirectory(pictureId, context);
			return BitmapFactory.decodeFile(tempFileName);			
		}
	}
}

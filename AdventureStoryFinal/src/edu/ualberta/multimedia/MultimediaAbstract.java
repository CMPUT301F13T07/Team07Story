package edu.ualberta.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import edu.ualberta.adventstory.R;
import edu.ualberta.utils.Page;

abstract public class MultimediaAbstract {
	protected int id;
	protected String file_dir;
	protected int index;			// Position from first character.
	//protected int pictureId; 		// Since sounclips are represented by 
									// picture_id represents them.
									// A value of -1 use the default picture.
	protected Context context;
	
	public MultimediaAbstract(int id, int index, String file_dir) {
		this.id = id;
		this.file_dir = file_dir;
		this.index = index;
		//this.pictureId = pictureId;
		//this.context = context;
	}
	
	/**
	 * Overloaded constructor for when id is not yet known
	 * @param file_dir
	 * @param index
	 */
	public MultimediaAbstract(int index, String file_dir) {
		this.file_dir = file_dir;
		this.index = index;
	}
	
	public int getID() {return id;}
	public int getIndex() {return index;}
	//public int getPictureId() {return pictureId;}	
	public String getFileDir() {return file_dir;}
	public Context getContext() {return context;}
	
	public void setID(int id){this.id = id;}
	public void setIndex(int index){this.index = index; }
	//public void setPictureId(int pictureId){this.pictureId = pictureId;}
	public void setFileDir(String file_dir) {this.file_dir = file_dir;}
	public void setContext(Context context){this.context = context;}

	
	public Bitmap loadPhoto() {
		return BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_multimedia);
	}
	
	public void play() {
		// Override.
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(this == obj) return true;
		MultimediaAbstract ma = (MultimediaAbstract)obj;
		if(this.id == ma.id &&
			//this.index == ma.index &&
			//this.pictureId == ma.pictureId &&
			this.context == ma.context &&
			this.file_dir == ma.file_dir){
			return true;
		}
		return false;
	}
}

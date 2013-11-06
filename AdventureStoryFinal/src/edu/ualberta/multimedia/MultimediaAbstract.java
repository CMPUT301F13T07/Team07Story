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
	private boolean isSelected;		// DO NOT SERIALIZE THIS.
	
	public MultimediaAbstract(int id, int index, String file_dir) {
		this.id = id;
		this.file_dir = file_dir;
		this.index = index;
		
		this.isSelected = false;
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
	public String getFileDir() {return file_dir;}
	public boolean getIsSelected(){ return isSelected; }
	public void setID(int id){this.id = id;}
	public void setIndex(int index){this.index = index; }
	public void setFileDir(String file_dir) {this.file_dir = file_dir;}
	public void setIsSelected(boolean val){ this.isSelected = val; }
	
	public Bitmap loadPhoto(Context context) {
		return BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_multimedia);
	}
	
	public void play(Context context) {
		// Override.
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(this == obj) return true;
		MultimediaAbstract ma = (MultimediaAbstract)obj;
		if(this.id == ma.id &&
			this.file_dir == ma.file_dir){
			return true;
		}
		return false;
	}
}

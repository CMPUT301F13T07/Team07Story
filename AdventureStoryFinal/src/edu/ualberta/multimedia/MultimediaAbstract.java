package edu.ualberta.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.ualberta.adventstory.R;
import edu.ualberta.adventstory.TObserver;

abstract public class MultimediaAbstract extends TObservable<TObserver>{
	protected int id;
	protected String file_dir;
	protected int index;			// Position from first character.
	private boolean isSelected;		// DO NOT SERIALIZE THIS.
	
	public MultimediaAbstract(int id, int index, String file_dir) {
		super();
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
	
	public void setID(int id){
		this.id = id; 
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setIndex(int index){
		this.index = index;
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setFileDir(String file_dir) {
		this.file_dir = file_dir;
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setIsSelected(boolean val){ 
		boolean oldVal = this.isSelected;
		this.isSelected = val;
		if( oldVal != val){
			this.setChanged();
			this.notifyObservers();
		}
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

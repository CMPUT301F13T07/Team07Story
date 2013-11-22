package edu.ualberta.multimedia;

public class Video extends MultimediaAbstract{		
	public Video(int id, int index, String file_dir){
		super(id, index, file_dir);
	}
	
	@Override
	public boolean equals( Object obj ){
		if( obj == null ) return false;
		if( obj == this ) return true;
		return super.equals(obj);
	}
}

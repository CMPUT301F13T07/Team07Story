package edu.ualberta.multimedia;

public class SoundClip extends MultimediaAbstract {
	
	public SoundClip(int id, int index, String file_dir) {
		super(id, index, file_dir);
	}
	public SoundClip(int index, String file_dir) {
		super(index, file_dir);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		return super.equals(obj);
	}	
}

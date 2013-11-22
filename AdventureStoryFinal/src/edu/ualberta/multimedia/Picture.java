package edu.ualberta.multimedia;

public class Picture extends MultimediaAbstract{
	public static final int SMALL = 0;
	public static final int MEDIUM = 1;
	public static final int LARGE = 2;
	
	protected int mImageSize;
	
	public Picture(int id, int index, String file_dir){
		super(id, index, file_dir);	
		mImageSize = MEDIUM;
	}
	
	public Picture(int index, String file_dir) {
		super(index, file_dir);
		mImageSize = MEDIUM;
	}
	
	public Picture(int id, int index, String file_dir, int imageSize){
		super(id, index, file_dir);
		mImageSize = imageSize;		
	}
	
	public int getPictureSize(){return mImageSize; }
	public void setPictureSize(int is){mImageSize = is;}

	@Override
	public boolean equals( Object obj ){
		if( obj == null ) return false;
		if( obj == this ) return true;
		return super.equals(obj);
	}
}

package edu.ualberta.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.ualberta.adventstory.ActivityExtended;
import edu.ualberta.adventstory.DataSingleton;
//import edu.ualberta.adventstory.ChooseYourAdventure07;
import edu.ualberta.adventstory.R;

public class Video extends MultimediaAbstract{		
	public Video(int id, int index, String file_dir){
		super(id, index, file_dir);
	}
	
	
	@Override
	public void play(Context context){
		DataSingleton ds = (DataSingleton)context.getApplicationContext();
		((ActivityExtended) ds.getCurrentActivity()).switchToVideoViewPreview(file_dir);
	}
	
	public Bitmap loadPhoto(Context context){
		return BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_video);
	}
	
	@Override
	public boolean equals( Object obj ){
		if( obj == null ) return false;
		if( obj == this ) return true;
		return super.equals(obj);
	}
}

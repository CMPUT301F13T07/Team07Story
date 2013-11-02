package edu.ualberta.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.ualberta.adventstory.ChooseYourAdventure;
import edu.ualberta.adventstory.R;

public class Video extends MultimediaAbstract{		
	public Video(int id, int index){
		super(id, index);
	}
	
	@Override
	public void play(Context context){
		ChooseYourAdventure cya = (ChooseYourAdventure)context.
													getApplicationContext();
		cya.getCurrentActivity().switchToVideoViewPreview(
				MultimediaDB.getVideoDirectory(super.id, context));
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

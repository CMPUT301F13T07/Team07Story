package edu.ualberta.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.ualberta.adventstory.ChooseYourAdventure;
import edu.ualberta.adventstory.R;

public class Video extends MultimediaAbstract{		
	public Video(int id, int index, int pictureId){
		super(id, index, pictureId);
	}
	
	@Override
	public void play(Context context){
		ChooseYourAdventure cya = (ChooseYourAdventure)context.
													getApplicationContext();
		cya.getCurrentActivity().switchToVideoViewPreview(
				MultimediaDB.getVideoDirectory(super.id, context));
	}
	
	public Bitmap loadPhoto(Context context){
		if( super.pictureId== -1 ){
			return BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_video);
		}else{
			String tempFileName = MultimediaDB.getBitmapDirectory(pictureId, context);
			return BitmapFactory.decodeFile(tempFileName);			
		}	
	}
	
	@Override
	public boolean equals( Object obj ){
		if( obj == null ) return false;
		if( obj == this ) return true;
		return super.equals(obj);
	}
}

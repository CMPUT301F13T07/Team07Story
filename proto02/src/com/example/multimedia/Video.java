package com.example.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.proto02.*;

public class Video extends MultimediaAbstract{		
	public Video(int id, int index, int pictureId, Context context){
		super(id, index, pictureId, context);
	}
	
	@Override
	public void play(){
		ChooseYourAdventure07 cya = (ChooseYourAdventure07)super.context.
													getApplicationContext();
		((ActivityExtended)cya.getCurrentActivity()).switchToVideoViewPreview(
				MultimediaDB.getVideoDirectory(super.id, super.context));
	}
	
	public Bitmap loadPhoto(){
		if( super.pictureId== -1 ){
			return BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_video);
		}else{
			String tempFileName = MultimediaDB.getBitmapDirectory(pictureId, context);
			return BitmapFactory.decodeFile(tempFileName);			
		}	
	}
}

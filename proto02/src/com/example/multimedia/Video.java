package com.example.multimedia;

import android.content.Context;
import com.example.proto02.*;

public class Video extends MultimediaAbstract{		
	public Video(int id, int index, int pictureId, Context context){
		super(id, index, pictureId, MultimediaAbstract.VIDEO, context);
	}
	
	void playVideo(){
		ChooseYourAdventure07 cya = (ChooseYourAdventure07)super.context.
													getApplicationContext();
		((ActivityExtended)cya.getCurrentActivity()).switchVideoViewPreview(
				MultimediaDB.getVideoDirectory(super.id, super.context));
	}
}

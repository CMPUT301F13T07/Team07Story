package com.example.multimedia;

import com.example.proto02.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Picture extends MultimediaAbstract{	
	public Picture( int id, int index, Context context){
		super(id, index, id, MultimediaAbstract.PHOTO, context);	
	}
	
	// Override the following so that setting id will also set mPictureId
	// and vice versa.
	@Override
	public void setId(int id){
		super.id = id;
		super.pictureId = id;
	}
	
	@Override
	public void setPictureId(int pictureId){
		super.pictureId = pictureId;
		super.id = pictureId;
	}
}

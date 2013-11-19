package edu.ualberta.multimedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import edu.ualberta.adventstory.R;
import edu.ualberta.utils.Utility;

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

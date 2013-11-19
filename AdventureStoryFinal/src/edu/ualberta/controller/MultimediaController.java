package edu.ualberta.controller;

import edu.ualberta.multimedia.MultimediaAbstract;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * <code>MultimediaController</code> will be the template class for the 
 * thre MultimediaAbstract objects, Picture, SoundClip and Video.
 * 
 * @author Joey
 *
 */
abstract public class MultimediaController {
	abstract public void play(Context context, MultimediaAbstract ma);
	abstract public Bitmap loadBitmap(Context context, MultimediaAbstract ma);
}

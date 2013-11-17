package edu.ualberta.extendedViews;

import android.annotation.SuppressLint;
import android.text.style.ClickableSpan;
import android.view.View;
import edu.ualberta.adventstory.CommandCollection.Callback;

// ClickableSpan for multimedia.
public class ClickableMultimediaSpan extends ClickableSpan {
	Callback mOnClick;

	public ClickableMultimediaSpan(){
		super();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(final View widget) {
		mOnClick.callback();
	}
	
	public void setOnClick(Callback cb){
		mOnClick = cb;
	}
}
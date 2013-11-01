/*
 * Content.java
 * - This module is responsible for saving String and Multimedia
 *   that belongs to the Body of page. This is done since Android
 *   have SpannableStringBuilder which allows for integration of
 *   Bitmap to texts. To retrieve/set String use getter/setter function
 *   getParagraph():String/setParagraph(String paragraph).
 */

package edu.ualberta.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnGenericMotionListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import edu.ualberta.adventstory.ActivityExtended;
import edu.ualberta.adventstory.R;
import edu.ualberta.multimedia.MultimediaAbstract;

public class Content {
	String mParagraph;
	ArrayList<MultimediaAbstract> mMultimedia;
	boolean mEditMode;
	int mSelectedMultimedia = -1; // id of selected multimedia.

	public Content(String paragraph, ArrayList<MultimediaAbstract> multimedia) {
		if (multimedia == null) {
			mMultimedia = new ArrayList<MultimediaAbstract>();
		} else {
			mMultimedia = multimedia;
		}

		mParagraph = paragraph;
		mEditMode = false;
	}

	// Hard-Copy Constructor.
	public Content(Content source) {
		this.mParagraph = new String(source.getParagraph());

		ArrayList<MultimediaAbstract> mAA = source.getAllMultimedia();
		for (MultimediaAbstract m : mAA) {
			if (m != null) {
				this.mMultimedia.add(m);
			}
		}
		mEditMode = false;
	}

	public String getParagraph() {
		return mParagraph;
	}

	public int getSelectedMultimedia() {
		return mSelectedMultimedia;
	}

	public ArrayList<MultimediaAbstract> getAllMultimedia() {
		return mMultimedia;
	}

	public void setEditModeTrue(Context context) {
		mEditMode = true;
	}

	public void setEditModeFalse() {
		mEditMode = false;
	}

	public void setParagraph(String paragraph) {
		this.mParagraph = paragraph;
	}

	public boolean addMultimedia(MultimediaAbstract ma) {
		return mMultimedia.add(ma);
	}

	public boolean removeMultimedia(MultimediaAbstract ma) {
		return mMultimedia.remove(ma);
	}

	public boolean removeMultimedia(int id) {
		// Note: This delete the first object with following id.
		for (MultimediaAbstract m : mMultimedia) {
			if (id == m.getId()) {
				return mMultimedia.remove(m);
			}
		}
		return false; // MultimediaAbstract don't exist.
	}
}

/*
 * Paragraph.java
 * - This module is responsible for saving paragraphs that
 * 	 contain multimedia.s
 */

package edu.ualberta.multimedia;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Toast;

public class StringExtended {
	String mParagraph;
	ArrayList<MultimediaAbstract> mMultimedia;

	public StringExtended(String paragraph,
			ArrayList<MultimediaAbstract> multimedia) {
		if (multimedia == null) {
			mMultimedia = new ArrayList<MultimediaAbstract>();
		} else {
			mMultimedia = multimedia;
		}

		mParagraph = paragraph;
	}

	// Hard-Copy Constructor.
	public StringExtended(StringExtended source) {
		this.mParagraph = new String(source.getParagraph());

		ArrayList<MultimediaAbstract> mAA = source.getAllMultimedia();
		for (MultimediaAbstract m : mAA) {
			if (m != null) {
				this.mMultimedia.add(m);
			}
		}
	}

	public String getParagraph() {
		return mParagraph;
	}

	public ArrayList<MultimediaAbstract> getAllMultimedia() {
		return mMultimedia;
	}

	public SpannableStringBuilder getSpannableStringBuilder() {
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(
				mParagraph);

		if (mMultimedia != null) {
			for (MultimediaAbstract multimedia : mMultimedia) {
				Bitmap bitmap = multimedia.loadPhoto();
				ImageSpan is = new ImageSpan(multimedia.getContext(), bitmap);
				stringBuilder.insert(multimedia.getIndex(), " ");
				stringBuilder.setSpan(is, multimedia.getIndex(),
						multimedia.getIndex() + 1,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

				/* 
				 * Make Clickable
				 * InnerClass.
				 *  NOTE: If this is used anywhere else, then moved this outside
				 * 		  to it's own module.
				 */
				class ClickableSpanEx extends ClickableSpan {
					private MultimediaAbstract mMultimedia;
					private Context mContext;

					public ClickableSpanEx(MultimediaAbstract ma,
							Context context) {
						super();
						mMultimedia = ma;
						mContext = context;
					}

					@Override
					public void onClick(View widget) {
						// TODO Auto-generated method stub
						mMultimedia.play();
					}
				}

				ClickableSpanEx cse = new ClickableSpanEx(multimedia,
						multimedia.getContext());

				stringBuilder.setSpan(cse, multimedia.getIndex(),
						multimedia.getIndex() + 1,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}

		}
		return stringBuilder;
	}

	public void setParagraph(String paragraph) {
		this.mParagraph = paragraph;
	}

	public boolean addMultimedia(MultimediaAbstract ma) {return mMultimedia.add(ma);}
	public boolean removeMultimedia(MultimediaAbstract ma){return mMultimedia.remove(ma);}
	
	public boolean removeMultimedia(int id){ 
		// Note: This delete the first object with following id.
		for( MultimediaAbstract m: mMultimedia){
			if(id == m.getId()){ 
				return mMultimedia.remove(m);
			}
		}
		return false; // MultimediaAbstract don't exist.
	}
}

package edu.ualberta.extendedViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

public class PaddingableImageSpan extends ImageSpan {
	private int mExtraPadding = 0;
	private int mHeight = -1;
	private int mWidth = -1;

	boolean mPadding = false;
	Bitmap mBitmap;
	Point mPoint; // Coordinate of drawing.

	public PaddingableImageSpan(Context context, Bitmap bitmap, int extraPadding) {
		this(context, bitmap, extraPadding, -1, -1);
	}

	public PaddingableImageSpan(Context context, Bitmap bitmap,
			int extraPadding, int width, int height) {
		super(context, bitmap);
		mBitmap = bitmap;

		this.mExtraPadding = extraPadding;
		this.mWidth = width;
		this.mHeight = height;
	}

	@Override
	public Drawable getDrawable() {
		Drawable drawable = super.getDrawable();
		if ((mHeight > 0) || (mWidth > 0)) {
			Rect bounds = drawable.getBounds();
			if (mHeight > 0) {
				bounds.bottom = mHeight;
			}
			if (mWidth > 0) {
				bounds.right = mWidth;
			}
			drawable.setBounds(bounds);
		}
		return drawable;
	}

	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end,
			Paint.FontMetricsInt fm) {
		// adding the padding to the original image size
		int size = super.getSize(paint, text, start, end, fm);
		size += (2 * this.mExtraPadding);
		return size;
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end,
			float x, int top, int y, int bottom, Paint paint) {
		// Draw a border.
		if (mPadding) {
			int w = mBitmap.getWidth();
			int h = mBitmap.getHeight();
			paint.setColor(Color.DKGRAY);
			RectF rect = new RectF(x, top, x + w + mExtraPadding + 20, bottom
					+ mExtraPadding);
			canvas.drawRect(rect, paint);
		}

		// Draw someting
		// adding the padding to the transformation so it will
		// be padding on both sides
		super.draw(canvas, text, start, end, x + this.mExtraPadding, top, y,
				bottom, paint);
	}

	public int GetSize(Paint paint, CharSequence text, int start, int end,
			Paint.FontMetricsInt fm) {
		return Math.round(MeasureText(paint, text, start, end));
	}

	private float MeasureText(Paint paint, CharSequence text, int start, int end) {
		return paint.measureText(text, start, end);
	}

	public void enablePadding() {
		mPadding = true;
	}

	public void disablePadding() {
		mPadding = false;
	}
}
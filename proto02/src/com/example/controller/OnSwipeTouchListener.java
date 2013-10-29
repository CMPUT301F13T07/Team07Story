package com.example.controller;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {
	public OnSwipeTouchListener(){
	}
    private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 10;
        private static final int SWIPE_VELOCITY_THRESHOLD = 10;

        private boolean firstTime = true;
        private float oldX;
        private float oldY;
        @Override
        public boolean onDown(MotionEvent e) {
        	onTouch(e);
        	if( firstTime == true){
        		oldX = e.getX();
        		oldY = e.getY();
        	}else{
        		onSwipe(e.getX() - oldX, e.getY() - oldY);
        		oldX = e.getX();
        		oldY = e.getY();
        	}
            return true;
        }
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                onSwipe(diffX, diffY);
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {                    	
                        if (diffX > 0) {
                            onSwipeRight(diffX);
                        } else {
                            onSwipeLeft(diffX);
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom(diffY);
                        } else {
                            onSwipeTop(diffY);
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
    
    public void onTouch(MotionEvent me){
    	
    }
    
    public void onSwipe(float X, float Y){
    	
    }
    public void onSwipeRight(float diff) {
    }

    public void onSwipeLeft(float diff) {
    }

    public void onSwipeTop(float diff) {
    }

    public void onSwipeBottom(float diff) {
    }
}
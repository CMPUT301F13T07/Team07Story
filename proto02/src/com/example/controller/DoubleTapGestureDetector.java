package com.example.controller;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class DoubleTapGestureDetector extends GestureDetector.SimpleOnGestureListener{
	 @Override
     public boolean onDoubleTap(MotionEvent e) {         
         return true;
     }
}

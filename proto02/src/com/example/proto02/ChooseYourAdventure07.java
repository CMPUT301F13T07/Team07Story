package com.example.proto02;

import android.app.Application;
import android.app.Activity;

public class ChooseYourAdventure07 extends Application {
	private Activity mCurrentActivity = null;
	
	public void onCreate(){
		super.onCreate();
	}
	
	public Activity getCurrentActivity(){
		return mCurrentActivity;
	}
	
	public void setCurrentActivity(Activity currentActivity){
		mCurrentActivity = currentActivity;
	}
}

package edu.ualberta.adventstory;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import edu.ualberta.database.DbManager;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

public class DataSingleton extends Application{
	public static DbManager database;
	
	@Override
	public void onCreate() {
		System.out.println("Database created in DataSingleton");
		super.onCreate();
		database = new DbManager(this);
		database.open();
	}

	// Tests- ask Team which to integrate. 
	Activity mCurrentActivity;
	private Story mStory;
	private Page mCurrentPage;
	private Stack<Page> mPageHistory;	// This might be deleted.
	public Activity getCurrentActivity(){return mCurrentActivity;}
	public void setCurrentActivity(ActivityExtended currentActivity){mCurrentActivity = currentActivity;}
	
	public Page getOldPage(){ 
		if(mPageHistory.empty() == false)
			return mPageHistory.peek();
		else 
			return null;
	}
	
	public Story getCurrentStory(){return mStory;}
	public Page getCurrentPage(){return mCurrentPage;}
	
	public void setCurrentStory(Story story){ mStory = story; }
	
	public void revertPage(){
		if(mPageHistory.empty() == false){
			mCurrentPage = mPageHistory.pop();
		}
	}
	
	public void setCurrentPage(Page page){
		mPageHistory.push(mCurrentPage);
		mCurrentPage = page;
	}
}

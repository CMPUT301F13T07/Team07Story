package edu.ualberta.adventstory;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import edu.ualberta.database.DbManager;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

public class DataSingleton extends Application{
	public static DbManager database;
	
	Activity mCurrentActivity;
	private Story mCurrentStory;
	private Page mCurrentPage;
	private Stack<Page> mPageHistory;
	
	@Override
	public void onCreate() {
		System.out.println("Database created in DataSingleton");
		super.onCreate();
		database = new DbManager(this);
		database.open();
		
		mPageHistory = new Stack<Page>();
		mCurrentStory = null;
		mCurrentPage = null;
	}

	public Activity getCurrentActivity(){return mCurrentActivity;}
	public void setCurrentActivity(ActivityExtended currentActivity){mCurrentActivity = currentActivity;}
	public Story getCurrentStory(){ return mCurrentStory; }
	public void setCurrentStory(Story story){ mCurrentStory = story; }
	
	// Returns the last page viewed.
	public Page getOldPage(){ 
		if(mPageHistory.empty() == false)
			return mPageHistory.peek();
		else 
			return null;
	}
	
	// To allow to revert back to past pages when reading a story.
	public Page getCurrentPage(){return mCurrentPage;}	
	
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

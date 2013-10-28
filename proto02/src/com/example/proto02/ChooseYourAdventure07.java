package com.example.proto02;

import java.util.ArrayList;

import android.app.Application;
import android.app.Activity;

import com.example.multimedia.*;
import com.example.multimedia.MultimediaAbstract;
import com.example.multimedia.MultimediaDB;
import com.example.multimedia.StringExtended;
import com.example.utils.Page;
import com.example.utils.Story;

public class ChooseYourAdventure07 extends Application {
	private Activity mCurrentActivity = null;
	
	// Test data.
	private Story mStory;
	private Page mCurrentPage;
	
	public void onCreate(){
		super.onCreate();
		// TEST.
		// Load the database.
		Picture p1 = new Picture(1, 2, this);
		Picture p2 = new Picture(2, 450, this);
		SoundClip sc1 = new SoundClip(1, 40, -1, this);
		Video v1 = new Video(1, 100, -1, this);
		
		// Initialize photo index 1.
		if( MultimediaDB.getBitmapDirectory(1, this) == null){
			MultimediaDB.inserBitmapDirectory(p1.getId(), "/sdcard/Pictures/troll.jpg", this);
			
		}
		// Initialize photo index 2.
		if( MultimediaDB.getBitmapDirectory(2, this) == null){
			MultimediaDB.inserBitmapDirectory(p2.getId(), "/sdcard/Pictures/herp_derp.jpg", this);
			
		}
		if( MultimediaDB.getSoundDirectory(1, this) == null){
			MultimediaDB.insertSoundDirectory(sc1.getId(), "/sdcard/Music/pewpew.mp3", this);
			
		}
		if( MultimediaDB.getVideoDirectory(1, this) == null){
			MultimediaDB.insertVideoDirectory(v1.getId(), "/sdcard/Movies/wc.mp4", this);
			
		}
		
		ArrayList<MultimediaAbstract> medias = new ArrayList<MultimediaAbstract>();
		medias.add(p1);
		medias.add(p2);
		medias.add(sc1);
		medias.add(v1);
		// Create Pages.
		String par = "\tOnce upon a time laser guns create pewpew followed by " +
				"dying, thus the word pewpewdie. Troll cats meme's seemed to emerged " +
				"from this phenomenon, inevitably, troll faces followed." +
				"Mystery still remains wether troll faces came first before trolls or vice versa."+
				" These species of human race are often refered to as HERPDERPS." +
				" HERPDERP is the politcally incorrect alternative to the word stupid, a love child"+
				" of the trolls, troll faces, and troll cats."+
				"\n\n" + "\tHERPDERPS are the world master of puns in youtube. They often "+
				"have nazi germany puns that annefrankly I don't care, I do nazi the purpose "+
				"in using them in sentence. They are beyond mein kampfort zone and also requires" +
				"alot of concentration. Camp these HERPDERP's do something else than offend people " +
				"any fuhrer. This is simply aint Reich. They should burn to heil or something.";
		
		StringExtended se = new StringExtended(par, medias);
		Page test01 = new Page(1, "first page", "someone", se, null);
		StringExtended se2 = new StringExtended("once upon a time writers dont exist.", null);
		Page second = new Page(2, "third page", "someoneElse", se2, null);
		StringExtended se3 = new StringExtended("what plot?", null);
		Page third = new Page(3, "second page", "someoneElse", se3, null);
		ArrayList<Page> p = new ArrayList<Page>();
		p.add(second);
		p.add(third);
		test01.setPages(p);
		mStory = new Story("Test Story", "someone", test01);
		mCurrentPage = test01;
	}
	
	public Activity getCurrentActivity(){
		return mCurrentActivity;
	}
	
	public void setCurrentActivity(Activity currentActivity){
		mCurrentActivity = currentActivity;
	}
	
	public Story getCurrentStory(){return mStory;}
	public Page getCurrentPage(){return mCurrentPage;}
	
	public void setCurrentStory(Story story){ mStory = story; } 
	public void setCurrentPage(Page page){ mCurrentPage = page; } 
}

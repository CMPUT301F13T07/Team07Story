// TODO: Unsure if this should have an mOldPage to be able to go back
//		 or should Page should be able to determine it's parent?
//		 I persnally would recommend the latter since we are always sure
//		 the old page is the parent page.
// SHOULD DO: remember the scrolling index?
package edu.ualberta.adventstory;

import java.util.ArrayList;
import java.util.Stack;

import android.app.Application;

import edu.ualberta.multimedia.*;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;
import edu.ualberta.utils.Content;

public class ChooseYourAdventure extends Application {
	private ActivityExtended mCurrentActivity = null;
	
	// Test data.
	private Story mStory;
	private Page mCurrentPage;
	private Stack<Page> mPageHistory;	// This might be deleted.
	
	public ChooseYourAdventure(){
		super();
	}
	
	public void onCreate(){
		super.onCreate();
		// TEST.
		// Load the database.
		Picture p1 = new Picture(1, 2);
		Picture p2 = new Picture(2, 450);
		SoundClip sc1 = new SoundClip(3, 40);
		Video v1 = new Video(4, 100);
		
		// NOTE: We have different id's independent of the Multimedia Type.
		// Initialize photo index 1.
		if( MultimediaDB.getBitmapDirectory(1, this) == null){
			MultimediaDB.inserBitmapDirectory(p1.getId(), "/sdcard/Pictures/troll.jpg", this);
			
		}
		// Initialize photo index 2.
		if( MultimediaDB.getBitmapDirectory(2, this) == null){
			MultimediaDB.inserBitmapDirectory(p2.getId(), "/sdcard/Pictures/herp_derp.jpg", this);
			
		}
		if( MultimediaDB.getSoundDirectory(3, this) == null){
			MultimediaDB.insertSoundDirectory(sc1.getId(), "/sdcard/Music/pewpew.mp3", this);
			
		}
		if( MultimediaDB.getVideoDirectory(4, this) == null){
			MultimediaDB.insertVideoDirectory(v1.getId(), "/sdcard/Movies/chtr.mp4", this);
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
		
		// TODO: Temporarily Page.text is string.
		Content se = new Content(par, medias);
		Page test01 = new Page(1, "The rise of Trolls", "someone.", se, null);
		test01.setText(par);
		Content se2 = new Content("Told you will be disappointed.", null);
		Page second = new Page(2, "You will be disappointed.", "someoneElse", se2, null);
		second.setText("Told you will be disappointed.");
		Content se3 = new Content("Times like these when end of civilization seems iminent.", null);
		Page third = new Page(3, "Really?", "someoneElse", se3, null);
		third.setText("Times like these when end of civilization seems iminent.");
		ArrayList<Page> p = new ArrayList<Page>();
		p.add(second);
		p.add(third);
		test01.setPages(p);
		mStory = new Story("Test Story", "someone", test01);
		mCurrentPage = test01;
		mPageHistory = new Stack<Page>();
	}
	
	public ActivityExtended getCurrentActivity(){return mCurrentActivity;}
	
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

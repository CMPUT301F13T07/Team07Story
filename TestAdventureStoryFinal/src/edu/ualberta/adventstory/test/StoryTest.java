package edu.ualberta.adventstory.test;

import java.util.ArrayList;

import edu.ualberta.utils.*;
import edu.ualberta.multimedia.*;
import android.test.AndroidTestCase;
import android.util.Log;

public class StoryTest extends AndroidTestCase {
	private Page root;
	private Story newstory;
	String stitle;
	String author;
	Integer tid;
	
	
	public void setUp() {
		stitle = "My Test Story";
		author = "Lyle";
		tid = 1;
		root = new Page(tid, "ptitle", author, "text", null);
		storyConstructorTest();
		newstory = new Story(tid, stitle, author, root);
	}
	
	public void storyConstructorTest() {
		Story nstory = new Story(stitle, author, null);
		assertEquals(nstory.getTitle(), stitle);
		assertEquals(nstory.getAuthor(), author);
		assertNull(nstory.getRoot());
		
		nstory = new Story(tid, stitle, author, null);
		assertEquals(nstory.getID(), tid);
		assertEquals(nstory.getTitle(), stitle);
		assertEquals(nstory.getAuthor(), author);
		assertNull(nstory.getRoot());
		
		nstory = new Story(tid, stitle, author, root);
		assertEquals(nstory.getRoot(), root);
	}
	
	public void cloneTest() {
		assertEquals(newstory, newstory.clone());
	}
	
	public void cloneEntireStoryTest() {
		Log.i("Testing", "cloneEntireStoryTest: StoryTest's cloneTest and PageTest's cloneAllChildrenTest test this function");
	}
	
	public void getAllPagesTest() {
		Log.i("Testing", "getAllPagesTest: Tested in PageTest's getAllChildrenTest");
	}
	
	public void searchTests() {
		Log.i("Testing", "searchTests: Tested in PageTests searchTests");
	}
}

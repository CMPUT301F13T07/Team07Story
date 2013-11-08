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
		newstory = new Story(tid, stitle, author, root);
	}
	
	public void teststoryConstructor() {
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
	
	public void testclone() {
		Story cstory = newstory.clone();
		assertEquals(newstory.getID(), cstory.getID());
		assertEquals(newstory.getTitle(), cstory.getTitle());
		assertEquals(newstory.getAuthor(), cstory.getAuthor());
		assertEquals(newstory.getRoot(), cstory.getRoot());
	}
	
	
}

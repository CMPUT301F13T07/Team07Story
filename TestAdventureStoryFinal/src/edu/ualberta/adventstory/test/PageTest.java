package edu.ualberta.adventstory.test;

import java.util.ArrayList;

import edu.ualberta.utils.*;
import edu.ualberta.multimedia.*;
import android.test.AndroidTestCase;

public class PageTest extends AndroidTestCase {
	private Page root;
	private Story newstory;
	private String ptitle;
	private String author;
	private String text;
	private Integer tid;
	private Picture pic;
	private ArrayList<MultimediaAbstract> ma;
	private ArrayList<Page> pages;
	
	
	public void setUp() {
		ptitle = "My Test Page";
		author = "Lyle";
		text = "This is text for testing";
		tid = 1;
		pic = new Picture(1, 0, "/Pictures");
		ma = new ArrayList<MultimediaAbstract>();
		pages = new ArrayList<Page>();
		ma.add(pic);
		root = new Page(tid, ptitle, author, text, ma, null);
		pages.add(new Page(tid, ptitle, author, text, null));
	}
	
	public void testpageConstructor() {
		Page npage = new Page(ptitle, author, text, null);
		assertEquals(npage.getTitle(), ptitle);
		assertEquals(npage.getAuthor(), author);
		assertEquals(npage.getText(), text);
		assertNotNull(npage.getPages());
		assertTrue(npage.getPages().size() == 0);
		
		pages.add(new Page(ptitle, author, text, null));
		
		npage = new Page(ptitle, author, text, pages);
		assertNotNull(npage.getPages());
		assertEquals(npage.getPages(), pages);
		
		npage = new Page(ptitle, author, text, ma, null);
		assertEquals(npage.getTitle(), ptitle);
		assertEquals(npage.getAuthor(), author);
		assertEquals(npage.getText(), text);
		assertNotNull(npage.getPages());
		assertTrue(npage.getPages().size() == 0);
		assertNotNull(npage.getMultimedia());
		assertEquals(npage.getMultimedia(), ma);
		
		npage = new Page(tid, ptitle, author, text, null);
		assertEquals(npage.getID(), tid);
		assertEquals(npage.getTitle(), ptitle);
		assertEquals(npage.getAuthor(), author);
		assertEquals(npage.getText(), text);
		assertNotNull(npage.getPages());
		assertTrue(npage.getPages().size() == 0);
		
		npage = new Page(tid, ptitle, author, text, pages);
		assertNotNull(npage.getPages());
		assertEquals(npage.getPages(), pages);
		
		npage = new Page(tid, ptitle, author, text, ma, null);
		assertEquals(npage.getID(), tid);
		assertEquals(npage.getTitle(), ptitle);
		assertEquals(npage.getAuthor(), author);
		assertEquals(npage.getText(), text);
		assertNotNull(npage.getPages());
		assertTrue(npage.getPages().size() == 0);
		assertNotNull(npage.getMultimedia());
		assertEquals(npage.getMultimedia(), ma);
	}
	
	public void testclone() {
		Page npage = new Page(tid, ptitle, author, text, pages);
		Page cpage = npage.clone();
		assertEquals(npage.getID(), cpage.getID());
		assertEquals(npage.getTitle(), cpage.getTitle());
		assertEquals(npage.getAuthor(), cpage.getAuthor());
		assertEquals(npage.getText(), cpage.getText());
		assertEquals(npage.getMultimedia(), cpage.getMultimedia());
		assertNotSame(npage.getPages(), cpage.getPages());
		assertNotNull(cpage.getPages());
	}

	public void testgetAllPages() {
		ArrayList<String> spages = new ArrayList<String>();
		root = new Page(0, ptitle, author, text, null);
		spages.add(root.toString());
		pages.clear();
		pages.add(root);
		for (int i=0; i<5; i++) {
			Page npage = new Page(i, ptitle, author, text, null);
			spages.add(npage.toString());
			pages.add(npage);
			root.addPage(npage);
		}
		assertEquals(spages.size(), pages.size());
		for (int i=0; i<spages.size(); i++)
			assertEquals(pages.get(0).toString(), spages.get(0));
	}
	
	
}

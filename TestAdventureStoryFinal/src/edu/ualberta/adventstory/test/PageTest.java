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
	
	public void pageConstructorTest() {
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
	
	public void cloneTest() {
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

	public void getAllPagesTest() {
		Page root = new Page("root", author, "root", null);
		for (Integer i=0; i<3; i++) {
			String out = i.toString() + " 1st level";
			root.addPage(new Page(out, author, out, null));
		}
		pages.clear();
		pages.addAll(root.getPages());
		ArrayList<Page> ops;
		for (Integer i=0; i < (ops = root.getPages()).size(); i++) {
			for (Integer j=0; j<3; j++) {
				String out = j.toString() + " 2nd level";
				ops.get(i).addPage(new Page(out, author, out, null));
			}
			pages.addAll(ops.get(i).getPages());
		}
		ops = root.getAllPages();
		assertEquals(pages.size(), ops.size());
		for (int i=0; i<pages.size(); i++) {
			if (!pages.contains(ops.get(i)));
				fail("getAllPages did not return all the pages");
		}
	}
	
	public void cloneAllChildrenTest() {
		Page root = new Page("root", author, "root", null);
		for (Integer i=0; i<3; i++) {
			String out = i.toString() + " 1st level";
			root.addPage(new Page(out, author, out, null));
		}
		ArrayList<Page> ops;
		for (Integer i=0; i < (ops = root.getPages()).size(); i++) {
			for (Integer j=0; j<3; j++) {
				String out = j.toString() + " 2nd level";
				ops.get(i).addPage(new Page(out, author, out, null));
			}
		}
		
		Page cpage = root.cloneAllChildren();
		assertEquals(root, cpage);
	}
}

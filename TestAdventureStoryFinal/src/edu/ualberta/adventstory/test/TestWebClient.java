package edu.ualberta.adventstory.test;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import edu.ualberta.data.Constant;
import edu.ualberta.data.DbManager;
import edu.ualberta.data.WebClient;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.Picture;
import edu.ualberta.multimedia.SoundClip;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

public class TestWebClient extends AndroidTestCase {
	private WebClient webclient;
	private Page test_page = new Page("test title", "test author", "test text", null);
	private Page page = new Page("page title", "page author", "text", null);
	private Page op_page = new Page("option title", "option", "option text", null);
	private Story story = new Story("test title", "test author", page);
	private Story test_story = new Story("story title", "story author", page);
	private Picture test_pic = new Picture(1, "~/documents");
	private SoundClip soundClip = new SoundClip(1, "~/music");
	private Integer story_id;
	
	public void setUp() {
		webclient = new WebClient();
		webclient.open();
		story.setID((int) webclient.insert_story(story));
		page.setID((int) webclient.insert_page(page));
		op_page.setID((int) webclient.insert_page(page));
		test_pic.setID((int) webclient.insert_multimedia(test_pic, page.getID()));
		System.out.println("test_pic id = " + test_pic.getID());
	}
	
	public void testinsert_page() {
		long page_id;
		page_id = webclient.insert_page(test_page);
		assertNotNull(page_id);
	}
	
	public void testInsertStory() {
		story_id = (int) webclient.insert_story(test_story);
		assertNotNull(story_id);
		test_story.setID((int) story_id);
	}
	
	public void testInsertPageOption() {
		int rows;
		rows = (int) webclient.insert_page_option(page, op_page);
		System.out.println("insertpageoption rows = " + rows);
		assertFalse(rows == -1);
	}
	
	public void testinsert_multimedia() {
		long pic_id;
		pic_id = webclient.insert_multimedia(soundClip, 1);
		assertFalse(pic_id == -1);
		assertNotNull(pic_id);
	}
	
	public void testget_stories_by_author() {
		ArrayList<Story> results = new ArrayList<Story>();
		
		results = webclient.get_stories_by_author("test author");
		assertEquals(results.get(0).getTitle(), "test title");
		
		results = webclient.get_stories_by_author("nobody");
		assertTrue(results.size() == 0);
		
		results = webclient.get_stories_by_author("");
		System.out.println("empty string author: " + results.size());
	}
	
	public void testget_stories_by_title() {
		ArrayList<Story> results = new ArrayList<Story>();
		
		results = webclient.get_stories_by_title("test title");
		assertEquals(results.get(0).getAuthor(), "test author");
		
		results = webclient.get_stories_by_title("nothing");
		assertTrue(results.size() == 0);
	}
	
	public void testget_stories_by_id() {
		Story result;
		result = webclient.get_story_by_id(story.getID());
		assertEquals(result.getAuthor(), "test author");
	}
	
	public void testget_multimedia_by_page_id() {
		ArrayList<MultimediaAbstract> result = 
				new ArrayList<MultimediaAbstract>();
		result = webclient.get_multimedia_by_page_id(1);
		assertEquals(result.size(), 1);
	}
	
	public void testupdate_story() {
		Long result;
		String update = "updated title";
		test_story.setTitle(update);
		result = webclient.update_story(test_story);
		assertTrue(result >= 0);
		assertEquals(test_story.getTitle(), update);
	}
	
	public void test_update_page() {
		Long result;
		String update = "updated author";
		page.setAuthor(update);
		result = webclient.update_page(page);
		assertTrue(result >= 0);
		assertEquals(page.getAuthor(), update);
	}
	
	public void tearDown() {
		webclient.close();
	}
}

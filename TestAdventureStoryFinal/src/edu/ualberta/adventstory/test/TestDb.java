package edu.ualberta.adventstory.test;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import edu.ualberta.database.Db;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

public class TestDb extends AndroidTestCase {
	private Db test_db;
	private Page page = new Page("page title", "page author", "text", null);
	private Story test_story = new Story("story title", "story author", page);
	private Page new_page = new Page("option", "option author", "this is an option's text", null);
	private Integer story_id;
	
	public void setUp() {
		test_db = new Db(mContext);
		test_db.open();
	}
	
	public void testOpen() {
		test_db.open();
		assertTrue(test_db.db.isOpen());
	}
	
	public void testInsertPage() {
		long page_id;
		page_id = test_db.insert_page(page);
		assertNotNull(page_id);
		page.setID((int) page_id);
	}
	
	public void testInsertStory() {
		story_id = (int) test_db.insert_story(test_story);
		assertNotNull(story_id);
		page.setID((int) story_id);
	}
	
	public void testInsertPageOption() {
		long rows;
		rows = test_db.insert_page_option(page, new_page);
		assertNotNull(rows);
	}
	
	public void testget_stories_by_author() {
		ArrayList<Story> results = new ArrayList<Story>();
		results = test_db.get_stories_by_author("story author");
		assertTrue(results.size() == 1);
		assertEquals(results.get(0).getTitle(), "story title");
		
		results = test_db.get_stories_by_author("nobody");
		assertTrue(results.size() == 0);
	}
	
	public void testget_stories_by_title() {
		ArrayList<Story> results = new ArrayList<Story>();
		results = test_db.get_stories_by_title("story title");
		assertTrue(results.size() == 1);
		assertEquals(results.get(0).getAuthor(), "story author");
		
		results = test_db.get_stories_by_title("nothing");
		assertTrue(results.size() == 0);
	}
	
	public void testget_stories_by_id() {
		Story result;
		result = test_db.get_story_by_id(story_id);
		assertEquals(result.getAuthor(), "story author");
	}
	
	public void testupdate_story() {
		Long result;
		String update = "updated title";
		test_story.setTitle(update);
		result = test_db.update_story(test_story);
		assertTrue(result == 0);
		assertEquals(test_story.getTitle(), update);
		
	}
	
	public void test_update_page() {
		Long result;
		String update = "updated author";
		page.setAuthor(update);
		result = test_db.update_page(page);
		assertTrue(result == 0);
		assertEquals(page.getAuthor(), update);
	}
	
	public void testClose() {
		test_db.close();
		assertFalse(test_db.db.isOpen());
	}
	
	public void tearDown() {
		test_db.close();
	}
}

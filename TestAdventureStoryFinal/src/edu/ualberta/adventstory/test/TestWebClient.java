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
	private Page test_page = new Page(1, "test title", "test author", "test text", null);
	private Page page = new Page(3, "Learning Android", "Team07", 
								"We learned how to make an android app.  "
								+ "Look at how great it is!", null);
	private Page op_page = new Page(2, "option title", "option", "option text", null);
	private Story story = new Story(1, "Adventures in c301", "Team07", page);
	private Story test_story = new Story(2, "story title", "story author", page);
	private Picture test_pic = new Picture(1, 3, "~/documents");
	private SoundClip soundClip = new SoundClip(2, 3, "~/music");
	private Integer story_id;
	private Boolean setup_done = true;
	
	public void setUp() {
		webclient = new WebClient();
		if (!setup_done) {
			webclient.insert_story(story);
			webclient.insert_page(page);
			webclient.insert_multimedia(test_pic, page.getID());
		}
	}
	
	public void testget_stories_by_author() {
		ArrayList<Story> results = new ArrayList<Story>();
		
		results = webclient.get_stories_by_author("Team07");
		assertEquals(results.get(0).getTitle(), "Adventures in c301");
		
		results = webclient.get_stories_by_author("nobody");
		assertTrue(results.size() == 0);
		
		results = webclient.get_stories_by_author("");
		assertTrue(results.size() > 1);
	}
	
	public void testget_stories_by_title() {
		ArrayList<Story> results = new ArrayList<Story>();
		
		results = webclient.get_stories_by_title("Advent");
		assertEquals(results.get(0).getAuthor(), "Team07");
		
		results = webclient.get_stories_by_title("nothing");
		assertTrue(results.size() == 0);
		
		results = webclient.get_stories_by_title("");
		assertTrue(results.size() > 1);
	}
	
	public void testget_stories_by_id() {
		Story result;
		result = webclient.get_story_by_id(story.getID());
		assertEquals(result.getAuthor(), "Team07");
	}
	
	public void testget_multimedia_by_page_id() {
		ArrayList<MultimediaAbstract> result = 
				new ArrayList<MultimediaAbstract>();
		result = webclient.get_multimedia_by_page_id(3);
		assertEquals(result.get(0).getID(), 1);
	}
	
	public void tearDown() {
	}
}

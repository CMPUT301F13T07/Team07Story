package edu.ualberta.data;

import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

public class ES_Story {
	private String story_title;
	private String story_author;
	private int story_id;
	private int root;
	
	public ES_Story() {	
	}
	
	public ES_Story(String story_title, String story_author, int story_id, int root) {
		this.story_title = story_title;
		this.story_author = story_author;
		this.story_id = story_id;
		this.root = root;
	}
	
	public String toString() {
		return "Title = " + story_title + ", Author = " + story_author;
	}
	
	public Story toStory(Page root) {
		return new Story(story_id, story_title, story_author, root);
	}

	public String getStory_title() {
		return story_title;
	}

	public void setStory_title(String story_title) {
		this.story_title = story_title;
	}

	public String getStory_author() {
		return story_author;
	}

	public void setStory_author(String story_author) {
		this.story_author = story_author;
	}

	public int getStory_id() {
		return story_id;
	}

	public void setStory_id(int story_id) {
		this.story_id = story_id;
	}

	public int getRoot() {
		return root;
	}

	public void setRoot(int root) {
		this.root = root;
	}
}

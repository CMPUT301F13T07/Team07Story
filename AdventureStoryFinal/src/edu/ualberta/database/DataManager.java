package edu.ualberta.database;

import java.util.ArrayList;

import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

public interface DataManager {
	
	/**
	 * Inserts a story into the Story table
	 * @param story
	 * @return id number of the inserted story
	 */
	public long insert_story(Story story);
	
	/**
	 * Inserts a page into the Page table
	 * @param page
	 * @return id number of inserted page
	 */
	public long insert_page(Page page);
	
	/**
	 * Inserts a new page option into a page, for a given story
	 * @param story
	 * @param page
	 * @param option
	 * @return number of rows inserted
	 */
	public long insert_page_option(Page page, Page option);
	
	/**
	 * Inserts a new multimedia item into the multimedia table
	 * @param mult
	 * @return
	 */
	public long insert_multimedia(MultimediaAbstract mult, int page_id);
	
	// getter methods
	public ArrayList<Story> get_stories_by_title(String search_title);
	public ArrayList<Story> get_stories_by_author(String search_author);
	public Story get_story_by_id(Integer id);
	public ArrayList<Page> get_pages_by_title(String search_title);
	public ArrayList<Page> get_pages_by_author(String search_author);
	public Page get_page_by_id(Integer id);
	public ArrayList<Page> get_page_options(Integer id);
	public ArrayList<MultimediaAbstract> get_multimedia_by_page_id(Integer page_id);

	/**
	 * Updates a story's author, title, and or root page
	 * @param old
	 * @param updated
	 * @return
	 */
	public long update_story(Story story);
	
	/**
	 * Updates a page's author, title, and/or text
	 * Does NOT update the page's children
	 * @see insert_page_option
	 * @see delete_page_option
	 * @param page
	 * @return
	 */
	public long update_page(Page page);
	
	/**
	 * Deletes a story from the story table
	 * @param story
	 * @return 0 if successful, -1 if not successful
	 */
	public long delete_story(Story story);
	
	/**
	 * Deletes a page and its connection to its children from the db
	 * @param page
	 * @return
	 */
	public long delete_page(Page page);
	
	/**
	 * Deletes page, child row from page_children table
	 * @param page
	 * @param child
	 * @return
	 */
	public long delete_page_option(Page page, Page child);
}

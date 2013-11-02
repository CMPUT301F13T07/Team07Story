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
	public abstract long insert_story(Story story);
	
	/**
	 * Inserts a page into the Page table
	 * @param page
	 * @return id number of inserted page
	 */
	public abstract long insert_page(Page page);
	
	/**
	 * Inserts a new page option into a page, for a given story
	 * @param story
	 * @param page
	 * @param option
	 * @return number of rows inserted
	 */
	public abstract long insert_page_option(Page page, Page option);
	
	/**
	 * Inserts a new multimedia item into the multimedia table
	 * @param mult
	 * @return
	 */
	public abstract long insert_multimedia(MultimediaAbstract mult, int page_id);
	
	// getter methods
	public abstract ArrayList<Story> get_stories_by_title(String search_title);
	public abstract ArrayList<Story> get_stories_by_author(String search_author);
	public abstract Story get_story_by_id(Integer id);
	public abstract ArrayList<Page> get_pages_by_title(String search_title);
	public abstract ArrayList<Page> get_pages_by_author(String search_author);
	public abstract Page get_page_by_id(Integer id);
	public abstract ArrayList<Page> get_page_options(Integer id);
	public abstract ArrayList<MultimediaAbstract> get_multimedia_by_page_id(Integer page_id);

	/**
	 * Updates a story's author, title, and or root page
	 * @param old
	 * @param updated
	 * @return
	 */
	public abstract long update_story(Story story);
	
	/**
	 * Updates a page's author, title, and/or text
	 * Does NOT update the page's children
	 * @see insert_page_option
	 * @see delete_page_option
	 * @param page
	 * @return
	 */
	public abstract long update_page(Page page);
	
	/**
	 * Deletes a story from the story table
	 * @param story
	 * @return 0 if successful, -1 if not successful
	 */
	public abstract long delete_story(Story story);
	
	/**
	 * Deletes a page and its connection to its children from the db
	 * @param page
	 * @return
	 */
	public abstract long delete_page(Page page);
	
	/**
	 * Deletes page, child row from page_children table
	 * @param page
	 * @param child
	 * @return
	 */
	public abstract long delete_page_option(Page page, Page child);
}

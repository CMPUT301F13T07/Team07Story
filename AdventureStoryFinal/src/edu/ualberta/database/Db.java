package edu.ualberta.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

// Based on The Android Developer's Cookbook, Addison-Wesley, 2011 - Listing 9.9
// Also based on mnaylor's CMPUT301 Assignment1
/**
 * Performs database tasks including open, close, insert, update, and delete
 *
 */
public class Db {
	private static SQLiteDatabase db;
	private final Context context;
	private final DbHelper dbhelper;
	
	public Db(Context c){
		context = c;
		dbhelper = new DbHelper(context,Constant.DATABASE_NAME, null,
								Constant.DATABASE_VERSION);
	}

	public void close() {
		db.close();
	}

	/**
	 * opens database
	 * @throws SQLiteException
	 */
	public void open() throws SQLiteException {
		try {
			db = dbhelper.getWritableDatabase();
		} 
		catch(SQLiteException ex) {
			Log.v("Open database exception caught", ex.getMessage());
			db = dbhelper.getReadableDatabase();
		}
	}

	/**
	 * Inserts a story into the Story table
	 * @param story
	 * @return id number of the inserted story
	 */
	public long insert_story(Story story) {
		ContentValues values = new ContentValues();
	    values.put(Constant.STORY_TITLE, story.getTitle());
	    values.put(Constant.STORY_AUTHOR, story.getAuthor());
	    values.put(Constant.ROOT, story.getRoot().getID());
		try{
			return db.insert(Constant.TABLE_STORY, null, values);
		} 
		catch(SQLiteException ex) {
			Log.v("Insert into database exception caught", ex.getMessage());
			return -1;
		}
	}
	
	/**
	 * Inserts a page into the Page table
	 * @param page
	 * @return id number of inserted page
	 */
	// TODO: add children pages to Story_Page table
	// necessary? When would a newly inserted/created page ever have a child?
	public long insert_page(Page page) {
		ContentValues values = new ContentValues();
	    values.put(Constant.PAGE_TITLE, page.getTitle());
	    values.put(Constant.PAGE_AUTHOR, page.getAuthor());
		try{
			return db.insert(Constant.TABLE_PAGE, null, values);
		} 
		catch(SQLiteException ex) {
			Log.v("Insert into database exception caught", ex.getMessage());
			return -1;
		}
	}
	
	/**
	 * Inserts a new page option into a page, for a given story
	 * @param story
	 * @param page
	 * @param option
	 * @return number of rows inserted
	 */
	public long insert_page_option(Page page, Page option) {
		ContentValues values = new ContentValues();
	    values.put(Constant.PAGE_ID, page.getID());
	    values.put(Constant.NEXT_PAGE_ID, option.getID());
		try{
			return db.insert(Constant.TABLE_PAGE_CHILDREN, null, values);
		}
		catch(SQLiteException ex) {
			Log.v("Insert into database exception caught", ex.getMessage());
			return -1;
		}
	}
	
	// getter methods
	public ArrayList<Story> get_stories_by_title(String search_title) {
		Cursor c = get_from_db(Constant.TABLE_STORY, Constant.STORY_TITLE, search_title);
		return story_from_cursor(c);
	}
	public ArrayList<Story> get_stories_by_author(String search_author) {
		Cursor c = get_from_db(Constant.TABLE_STORY, Constant.STORY_AUTHOR, search_author);
		return story_from_cursor(c);
	}
	public Story get_story_by_id(Integer id) {
		Cursor c = get_from_db(Constant.TABLE_STORY, Constant.PAGE_ID, id);
		return story_from_cursor(c).get(0);
	}
	public ArrayList<Page> get_pages_by_title(String search_title) {
		Cursor c = get_from_db(Constant.TABLE_PAGE, Constant.PAGE_TITLE, search_title);
		return page_from_cursor(c);
	}
	public ArrayList<Page> get_pages_by_author(String search_author) {
		Cursor c = get_from_db(Constant.TABLE_PAGE, Constant.PAGE_AUTHOR, search_author);
		return page_from_cursor(c);
	}
	public Page get_page_by_id(Integer id) {
		Cursor c = get_from_db(Constant.TABLE_PAGE, Constant.PAGE_ID, id);
		return page_from_cursor(c).get(0);
	}
	public ArrayList<Page> get_page_options(Integer id) {
		Cursor c = get_from_db(Constant.TABLE_PAGE_CHILDREN, Constant.PAGE_ID, id);
		return page_from_cursor(c);
	}
	
	/**
	 * Fetches story(ies) or page(s) from the db
	 * @param table
	 * @param column
	 * @param term
	 * @return Cursor
	 */
	public Cursor get_from_db(String table, String column, String term) {
		String select = "SELECT * FROM " + table
				+ "WHERE " + column + " LIKE %"
				+ term + "%";
		try {
			return db.rawQuery(select, null);
		} catch(SQLiteException ex) {
			Log.v("Select from database exception caught", ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Overloaded. To be used when getting ONE item according to id
	 * @param table
	 * @param column
	 * @param id
	 * @return Cursor
	 */
	public Cursor get_from_db(String table, String column, Integer id) {
		String select = "SELECT * FROM " + table
				+ "WHERE " + column + " = " + id;
		try {
			return db.rawQuery(select, null);
		} catch(SQLiteException ex) {
			Log.v("Select from database exception caught", ex.getMessage());
			return null;
		}
	}
	
	public ArrayList<Page> page_from_cursor(Cursor c) {
		Integer id;
	    String title;
	    String author;
		String text;
		
		ArrayList<Page> pages = new ArrayList<Page>();
		
		while (c != null) {
			c.moveToNext();
			id = c.getInt(c.getColumnIndex(Constant.PAGE_ID));
			title = c.getString(c.getColumnIndex(Constant.PAGE_TITLE));
			author = c.getString(c.getColumnIndex(Constant.PAGE_AUTHOR));
			text = c.getString(c.getColumnIndex(Constant.PAGE_TEXT));
			
			ArrayList<Page> options = new ArrayList<Page>();
			options = get_page_options(id);
			
			Page page = new Page(id, title, author, text, options);
			pages.add(page);
		}
		
		return pages;
	}
	
	public ArrayList<Story> story_from_cursor(Cursor c) {
		Integer id;
		String title;
		String author;
		Integer root_id;
		Page root;
		
		ArrayList<Story> stories = new ArrayList<Story>();
		
		while (c != null) {
			c.moveToNext();
			id = c.getInt(c.getColumnIndex(Constant.STORY_ID));
			title = c.getString(c.getColumnIndex(Constant.STORY_TITLE));
			author = c.getString(c.getColumnIndex(Constant.STORY_AUTHOR));
			root_id = c.getInt(c.getColumnIndex(Constant.ROOT));
			
			root = get_page_by_id(root_id);
			Story story = new Story(id, title, author, root);
			
			stories.add(story);
		}
		return stories;
	}
	
	
	// TODO: Update methods
	// TODO: Delete methods
	/**
	 * 
	 * @param table
	 * @param cv
	 * @param where
	 * @return number of rows updated if successful, -1 otherwise
	 */
	public long update(String table, ContentValues cv, String where) {
		try{
			return db.update(table, cv, where, null);
		}
		catch (SQLiteException ex) {
			Log.v("Insert into database exception caught", ex.getMessage());
			return -1;
		}
	}
	
	/**
	 * 
	 * @param table
	 * @param whereClause
	 * @return 0 if successful, -1 otherwise
	 */
	public long delete(String table, String whereClause) {
		try {
			db.delete(table, whereClause, null);
			return 0;
		}
		catch(SQLiteException ex) {
			Log.v("Delete row exception caught", ex.getMessage());
			return -1;
		}
	}
}
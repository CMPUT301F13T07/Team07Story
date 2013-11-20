package edu.ualberta.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.Picture;
import edu.ualberta.multimedia.SoundClip;
import edu.ualberta.multimedia.Video;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

// Based on The Android Developer's Cookbook, Addison-Wesley, 2011 - Listing 9.9
// Also based on mnaylor's CMPUT301 Assignment1
/**
 * Performs database tasks including open, close, insert, update, and delete
 *
 */
public class DbManager implements DataManager{
	public static SQLiteDatabase db;
	private final Context context;
	private final DbHelper dbhelper;
	
	public DbManager(Context c){
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
	
	public int number_stories() {
		String query = "SELECT COUNT(*) FROM " + Constant.TABLE_STORY;
		Cursor c = null;
		try {
			c = db.rawQuery(query, null);
		} catch(SQLiteException ex) {
			Log.v("Select from database exception caught", ex.getMessage());
			return 0;
		}
		c.moveToFirst();
		return c.getInt(0);
	}

	public long insert_story(Story story) {
		ContentValues values = new ContentValues();
	    values.put(Constant.STORY_TITLE, story.getTitle());
	    values.put(Constant.STORY_AUTHOR, story.getAuthor());
	    if (story.getRoot() != null)
	    	values.put(Constant.ROOT, story.getRoot().getID());
	    return insert(Constant.TABLE_STORY, values);
	}
	
	public long insert_page(Page page) {
		ContentValues values = new ContentValues();
	    values.put(Constant.PAGE_TITLE, page.getTitle());
	    values.put(Constant.PAGE_AUTHOR, page.getAuthor());
	    values.put(Constant.READONLY, page.getReadOnly());
		return insert(Constant.TABLE_PAGE, values);
	}
	
	public long insert_page_option(Page page, Page option) {
		ContentValues values = new ContentValues();
	    values.put(Constant.PAGE_ID, page.getID());
	    values.put(Constant.NEXT_PAGE_ID, option.getID());
		return insert(Constant.TABLE_PAGE_CHILDREN, values);
	}
	
	public long insert_multimedia(MultimediaAbstract mult, int page_id) {
		ContentValues values = new ContentValues();
		values.put(Constant.DIRECTORY, mult.getFileDir());
		values.put(Constant.PAGE_ID, page_id);
		values.put(Constant.INDEX, mult.getIndex());
		values.put(Constant.MULT_TYPE, mult.getClass().getSimpleName());		
		return insert(Constant.TABLE_MULT, values);
	}
	
	public long insert_user(String username) {
		ContentValues values = new ContentValues();
		values.put(Constant.USER, username);
		return insert(Constant.TABLE_LOGIN, values);
	}
	
	public long insert(String table, ContentValues values) {
		try{
			return db.insert(table, null, values);
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
		Cursor c = get_from_db(Constant.TABLE_STORY, Constant.STORY_ID, id);
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
		if (c.getCount() == 0) {
			return null;
		}
		return page_from_cursor(c).get(0);
	}
	public ArrayList<Page> get_page_options(Integer id) {
		Cursor c = get_from_db(Constant.TABLE_PAGE_CHILDREN, Constant.PAGE_ID, id);
		return page_from_cursor_children(c);
	}
	public ArrayList<MultimediaAbstract> get_multimedia_by_page_id(Integer page_id) {
		Cursor c = get_from_db(Constant.TABLE_MULT, Constant.PAGE_ID, page_id);
		return multimedia_from_cursor(c);
	}
	public String get_user(String username) {
		Cursor c = get_from_db(Constant.TABLE_LOGIN, Constant.USER, username);
		return username_from_cursor(c);
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
				+ " WHERE " + column + " LIKE '%"
				+ term + "%'";
		if (table.equals(Constant.TABLE_LOGIN)) {
			select = "SELECT * FROM " + table
					+ " WHERE " + column + " = '"
					+ term + "'";
		}
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
				+ " WHERE " + column + " = " + id;
		try {
			return db.rawQuery(select, null);
		} catch(SQLiteException ex) {
			Log.v("Select from database exception caught", ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Takes cursor filled with a page's children and returns 
	 * an arraylist of pages
	 * @param c
	 * @return
	 */
	public ArrayList<Page> page_from_cursor_children(Cursor c) {
		Integer id;
		ArrayList<Page> pages = new ArrayList<Page>();
		
		while (c.moveToNext()) {
			id = c.getInt(c.getColumnIndex(Constant.NEXT_PAGE_ID));
			
			Page page = get_page_by_id(id);
			pages.add(page);
		}
		return pages;
	}
	
	/**
	 * Takes a cursor full of pages and returns an array of page objects
	 * @param c
	 * @return ArrayList<Page>
	 */
	public ArrayList<Page> page_from_cursor(Cursor c) {
		Integer id;
	    String title;
	    String author;
		String text;
		Boolean readonly;
		
		ArrayList<Page> pages = new ArrayList<Page>();
				
		while (c.moveToNext()) {
			id = c.getInt(c.getColumnIndex(Constant.PAGE_ID));
			title = c.getString(c.getColumnIndex(Constant.PAGE_TITLE));
			author = c.getString(c.getColumnIndex(Constant.PAGE_AUTHOR));
			text = c.getString(c.getColumnIndex(Constant.PAGE_TEXT));
			readonly = c.getInt(c.getColumnIndex(Constant.READONLY)) > 0;
			
			ArrayList<Page> options = new ArrayList<Page>();
			options = get_page_options(id);
			
			Page page = new Page(id, title, author, text, options);
			page.setReadOnly(readonly);
			pages.add(page);
		}
		
		return pages;
	}
	
	/**
	 * Takes a cursor full of stories and returns an array of story objects
	 * @param c
	 * @return ArrayList<Story>
	 */
	public ArrayList<Story> story_from_cursor(Cursor c) {
		Integer id;
		String title;
		String author;
		Integer root_id;
		Page root;
		
		ArrayList<Story> stories = new ArrayList<Story>();
		while (c.moveToNext()) {
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
	
	public ArrayList<MultimediaAbstract> multimedia_from_cursor(Cursor c) {
		Integer mult_id;
		String file_dir;
		String type;
		Integer index;
		
		ArrayList<MultimediaAbstract> multimedia = 
				new ArrayList<MultimediaAbstract>();
		while(c.moveToNext()) {
			mult_id = c.getInt(c.getColumnIndex(Constant.MULT_ID));
			file_dir = c.getString(c.getColumnIndex(Constant.DIRECTORY));
			index = c.getInt(c.getColumnIndex(Constant.INDEX));
			type = c.getString(c.getColumnIndex(Constant.MULT_TYPE));
			
			if( type.compareTo("Picture") == 0 ){
			      multimedia.add( new Picture(mult_id, index, file_dir));
			}else if( type.compareTo("SoundClip") == 0){
			      multimedia.add( new SoundClip(mult_id, index, file_dir));
			}else if( type.compareTo("Video") == 0){
			      multimedia.add( new Video(mult_id, index, file_dir));
			}else{
			      multimedia.add(new MultimediaAbstract(mult_id, index, file_dir){});
			}
		}
		return multimedia;
	}
	
	public String username_from_cursor(Cursor c) {
		String username = "";
		while(c.moveToNext()) {
			username = c.getString(c.getColumnIndex(Constant.USER));
		}
		return username;
	}
	
	public long update_story(Story story) {
		ContentValues values = new ContentValues();
		String where = Constant.STORY_ID + " = " + story.getID();
		
	    values.put(Constant.STORY_TITLE, story.getTitle());
	    values.put(Constant.STORY_AUTHOR, story.getAuthor());
	    values.put(Constant.ROOT, story.getRoot().getID());
	    
	    return update(Constant.TABLE_STORY, values, where);
	}
	
	public long update_page(Page page) {
		ContentValues values = new ContentValues();
		String where = Constant.PAGE_ID + " = " + page.getID();
		
	    values.put(Constant.PAGE_TITLE, page.getTitle());
	    values.put(Constant.PAGE_AUTHOR, page.getAuthor());
	    values.put(Constant.PAGE_TEXT, page.getText());
	    
	    return update(Constant.TABLE_PAGE, values, where);
	}
	
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
	 * Deletes a story from the story table
	 * @param story
	 * @return 0 if successful, -1 if not successful
	 */
	public long delete_story(Story story) {
		String where = Constant.STORY_ID + " = " + story.getID(); 
		return delete(Constant.TABLE_STORY, where);
	}
	
	public long delete_page(Page page) {
		long result;
		String where = Constant.PAGE_ID + " = " + page.getID();
		
		/* Delete children from PAGE_CHILDREN table */
		result = delete(Constant.TABLE_PAGE_CHILDREN, where);
		if (result == -1) {return -1;}
		
		/* Delete page from page table */
		return delete(Constant.TABLE_PAGE, where);
	}
	
	public long delete_page_option(Page page, Page child) {
		String where = Constant.PAGE_ID + " = " + page.getID() + " AND "
				       + Constant.NEXT_PAGE_ID + " = " + child.getID();
		return delete(Constant.TABLE_PAGE_CHILDREN, where);
	}
	
	public long delete_mult(MultimediaAbstract mult, Page page) {
		String where = Constant.PAGE_ID + " = " + page.getID() + " AND "
					   + Constant.MULT_ID + " = " + mult.getID();
		return delete(Constant.TABLE_MULT, where);
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
	
	/*
	 * Work In Progress Section.
	 */
	public long update_multimedia(MultimediaAbstract mult, long page_id) {
		ContentValues values = new ContentValues();
		String where = Constant.MULT_ID + "=" + mult.getID();
		
		values.put(Constant.DIRECTORY, mult.getFileDir());
		values.put(Constant.INDEX, mult.getIndex());
		values.put(Constant.PAGE_ID, page_id);
		values.put(Constant.MULT_TYPE, mult.getClass().getSimpleName());		
		return update(Constant.TABLE_MULT, values, where);
	}
	
}
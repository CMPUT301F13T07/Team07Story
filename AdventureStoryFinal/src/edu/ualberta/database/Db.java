package edu.ualberta.database;

import android.content.ContentValues;
import android.content.Context;
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
 * @author mnaylor
 *
 */
public class Db {
	private static SQLiteDatabase db;
	private final Context context;
	private final DbHelper dbhelper;
	
	public Db(Context c){
		context = c;
		dbhelper = new DbHelper(context,Table.database_name, null,
								Table.database_version);
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
	public long insert_page_option(Story story, Page page, Page option) {
		ContentValues values = new ContentValues();
	    values.put(Constant.STORY_ID, story.getID());
	    values.put(Constant.PAGE_ID, page.getID());
	    values.put(Constant.NEXT_PAGE_ID, option.getID());
		try{
			return db.insert(Constant.TABLE_PAGE, null, values);
		} 
		catch(SQLiteException ex) {
			Log.v("Insert into database exception caught", ex.getMessage());
			return -1;
		}
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
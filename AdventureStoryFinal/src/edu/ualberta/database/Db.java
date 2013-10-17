package edu.ualberta.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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
	public static Table story_table;
	public static Table option_table;
	public static Table story_option_table;
	public final static Table[] table_list = {story_table, option_table, 
		                                      story_option_table};
	
	public Db(Context c){
		story_table = new Story_Table();
		option_table = new Option_Table();
		story_option_table = new Story_Option_Table();
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
	 * Inserts a row into the table
	 * @param table
	 * @param cv
	 * @return number of rows inserted if successful, -1 otherwise
	 */
	public long insert(String table, ContentValues cv) {
		try{
			return db.insert(table, null, cv);
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
	 * @return number of rows inserted if successful, -1 otherwise
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
package edu.ualberta.database;

import java.util.HashMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Superclass for all tables to extend.
 * @author michelle
 *
 */
public class Table {
	public static final String database_name = "story_storage";
	public static final int database_version = 1;
	public static HashMap<String, String> table_create;
	public String table_name = "";
	public String create = "";
	
	
	
	/**
	 * Gets everything from a given table
	 * "SELECT * FROM table_name;
	 * @param db
	 * @return Cursor
	 */
	public Cursor get_object(SQLiteDatabase db) {
		try{
			Cursor c = db.query(this.table_name, null, null,
								null, null, null, null);
			return c;
		}
		catch(SQLiteException ex) {
			Log.v("Database query exception caught", ex.getMessage());
		}
		return null;
	}
}

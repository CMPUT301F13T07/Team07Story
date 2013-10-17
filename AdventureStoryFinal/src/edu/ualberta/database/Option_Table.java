package edu.ualberta.database;

import android.content.ContentValues;
import edu.ualberta.utils.*;

/**
 * 
 * @author mnaylor
 *
 */
public class Option_Table extends Table{
	public String table_name = "Option";
	// Define columns
	public String option_id = "option_id";
	public String option_name = "option_name";
	public String option_author = "option_author";
	public String create = 
			"CREATE TABLE IF NOT EXISTS Option (" +
                    option_id + " INTEGER, " +
                    option_name + " TEXT, " +
                    option_author + " TEXT, " +
                    "PRIMARY KEY (" + option_id + "));";
	
	/**
	 * @author mnaylor
	 * @param db
	 * @param option
	 */
	public void insert_object(Db db, Option option) {
		ContentValues newTaskValue = new ContentValues();
		newTaskValue.put(option_id, option.id);
		newTaskValue.put(option_name, option.name);
		newTaskValue.put(option_author, option.author);
		
		db.insert(this.table_name, newTaskValue);
	}
	
	/**
	 * @author mnaylor
	 * @param db
	 * @param option
	 */
	public void update_object(Db db, Option option) {
		ContentValues values = new ContentValues();
		values.put(option_id, option.id);
		values.put(option_name, option.name);
		values.put(option_author, option.author);
		String whereClause = option_id + "= " + option.id;

		db.update(this.table_name, values, whereClause);
	}
	
	/**
	 * @author mnaylor
	 * @param db
	 * @param option
	 */
	public void delete_object(Db db, Option option) {
		String whereClause =  option_id + " = " + option.id;
		db.delete(this.table_name, whereClause);
	}
}

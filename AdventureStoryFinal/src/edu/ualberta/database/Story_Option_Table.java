package edu.ualberta.database;

import android.content.ContentValues;
import edu.ualberta.utils.*;

/**
 * Note: This table does not need an update function because
 * options will only be deleted or inserted.
 * 
 * @author mnaylor
 *
 */
public class Story_Option_Table extends Table {
	public String table_name = "story_option";
	// Define columns
	public String story_id = "story_id";
	public String option_id = "option_id";
	public String next_option_id = "next_option_id";
	public String create = 
			"CREATE TABLE IF NOT EXISTS " + this.table_name + " (" +
						  story_id + " INTEGER, " +
						  option_id + " INTEGER, " +
						  next_option_id + " INTEGER, " +
						  "PRIMARY KEY (" + story_id + ", " + option_id + ", " + 
						   next_option_id + "), " +
						  "FOREIGN KEY (" + story_id + ") " +
						  "REFERENCES Story(story_id), " +
						  "FOREIGN KEY (" + option_id + ") " +
						  "REFERENCES Option(option_id), " +
						  "FOREIGN KEY (" + next_option_id + ") " +
						  "REFERENCES Option(option_id));";
	/** 
	 * @author mnaylor
	 * @param db
	 * @param story
	 * @param cur_option
	 * @param new_option
	 */
	public void insert_object(Db db, Story story, Option cur_option, 
							  Option new_option) {
		ContentValues newTaskValue = new ContentValues();
		newTaskValue.put(story_id, story.id);
		newTaskValue.put(option_id, cur_option.id);
		newTaskValue.put(next_option_id, new_option.id);
		
		db.insert(this.table_name, newTaskValue);
	}
	
	/**
	 * @author mnaylor
	 * @param db
	 * @param story
	 * @param main_option
	 * @param next_option
	 */
	public void delete_object(Db db, Story story, Option main_option, 
							  Option next_option) {
		String whereClause =  story_id + " = " + story.id +
							  " AND " + option_id + " = " + main_option.id +
							  " AND " + next_option_id + " = " + next_option.id;
		db.delete(this.table_name, whereClause);
	}
}

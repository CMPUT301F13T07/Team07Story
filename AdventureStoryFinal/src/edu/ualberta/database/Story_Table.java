package edu.ualberta.database;

import android.content.ContentValues;
import edu.ualberta.utils.*;


public class Story_Table extends Table{
	public String table_name = "Story";
	// Define columns
	public String story_id = "story_id";
	public String story_name = "story_name";
	public String story_author = "story_author";
	public String first_option = "first_option";
	public String create = 
	         "CREATE TABLE IF NOT EXISTS" + this.table_name + "(" +
	         story_id + " INTEGER, " +
	         story_name + " TEXT, " +
	         story_author + " TEXT, " +
	         first_option + " INTEGER, " +
	         "PRIMARY KEY (" + story_id + "), " +
	         "FOREIGN KEY (" + first_option + ") " +
	         "REFERENCES Option(option_id));";
	
	public void insert_object(Db db, Story story) {
		ContentValues newTaskValue = new ContentValues();
		newTaskValue.put(story_id, story.id);
		newTaskValue.put(story_name, story.name);
		newTaskValue.put(story_author, story.author);
		newTaskValue.put(first_option, story.first_option);
		
		db.insert(this.table_name, newTaskValue);
	}
	
	public void update_object(Db db, Story story) {
		ContentValues values = new ContentValues();
		values.put(story_id, story.id);
		values.put(story_name, story.name);
		values.put(story_author, story.author);
		values.put(first_option, story.first_option);
		String whereClause = story_id + "= " + story.story_id;

		db.update(this.table_name, values, whereClause);
	}
	
	public void delete_object(Db db, Story story) {
		String whereClause =  story_id + " = " + story.id;
		db.delete(this.table_name, whereClause);
	}
}

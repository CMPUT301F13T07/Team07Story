package edu.ualberta.database;

import java.util.HashMap;

public class Tables {
	public static final String database_name = "story_storage";
	public static final int database_version = 1;
	public static HashMap<String, String> table_create;

	public void populate_table_create() {
		table_create = new HashMap<String, String>();
		table_create.put("Story", 
				         "CREATE TABLE IF NOT EXISTS Story (" +
				          "story_id INTEGER, " +
				          "story_name TEXT, " +
				          "author, TEXT, " +
				          "first_option INTEGER, " +
				          "PRIMARY KEY (story_id), " +
				          "FOREIGN KEY (first_option) " +
				          "REFERENCES Option(option_id));");
		table_create.put("Option", 
				         "CREATE TABLE IF NOT EXISTS Option (" +
                         "option_id INTEGER, " +
                         "option_name TEXT, " +
                         "option_author TEXT, " +
                         "PRIMARY KEY (option_id));");
		table_create.put("Story_Option",
				          "CREATE TABLE IF NOT EXISTS Story_Option (" +
						  "story_id INTEGER, " +
						  "option_id INTEGER, " +
						  "next_option_id INTEGER, " +
						  "PRIMARY KEY (story_id, option_id, next_option), " +
						  "FOREIGN KEY (story_id) REFERENCES Story(story_id), " +
						  "FOREIGN KEY (option_id) REFERENCES Option(option_id), " +
						  "FOREIGN KEY (next_option_id) REFERENCES Option(option_id));");
		table_create.put("Option_Multimedia",
						 "CREATE TABLE IF NOT EXISTS Option_Multimedia (" +
					     "option_id INTEGER, " +
						 "file_name TEXT, " +
						 "mult_type TEXT CHECK(mult_type = " + 
						 "'picture', mult_type = 'video', mult_type = 'audio'), " +
						 "PRIMARY KEY (option_id, file_name), " +
						 "FOREIGN KEY (option_id) REFERENCES Option(option_id));");
	}
}

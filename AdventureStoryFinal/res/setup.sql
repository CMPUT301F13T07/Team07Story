CREATE TABLE IF NOT EXISTS Story (
  story_id INTEGER,
  story_name TEXT,
  author, TEXT,
  first_option INTEGER,
  PRIMARY KEY (story_id)
  FOREIGN KEY (first_option) REFERENCES Option(option_id));

/* story_id not included in this table because an option may be reused
in multiple stories.  The Story_Option table will define the tree
structure of options given a story */
CREATE TABLE IF NOT EXISTS Option (
  option_id INTEGER,
  option_name TEXT,
  option_author TEXT,
  PRIMARY KEY (option_id));

/* Defines the tree structure of options for a given story 
   Enter a row for each next option */
CREATE TABLE IF NOT EXISTS Story_Option (
  story_id INTEGER,
  option_id INTEGER,
  next_option_id INTEGER,
  PRIMARY KEY (story_id, option_id, next_option),
  FOREIGN KEY (story_id) REFERENCES Story(story_id),
  FOREIGN KEY (option_id) REFERENCES Option(option_id),
  FOREIGN KEY (next_option_id) REFERENCES Option(option_id));


/* TODO: look into changing file_name column to BLOB 
         positional data needed? */
CREATE TABLE IF NOT EXISTS Option_Multimedia (
  option_id INTEGER,
  file_name TEXT,
  mult_type TEXT CHECK(mult_type = "picture", mult_type = "video", 
                       mult_type = "audio"),
  PRIMARY KEY (option_id, file_name),
  FOREIGN KEY (option_id) REFERENCES Option(option_id));
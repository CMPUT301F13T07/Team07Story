package edu.ualberta.data;

public class Constant {
	public static final String DATABASE_NAME = "story_page_storage";
    public static final int DATABASE_VERSION = 10;
    
	// table titles
	public static final String TABLE_STORY = "Story";
	public static final String TABLE_PAGE = "Page";
	public static final String TABLE_PAGE_CHILDREN = "Page_Children";
	public static final String TABLE_MULT = "Multimedia";
	public static final String TABLE_LOGIN = "Login";
	
	// common column titles
	public static final String STORY_ID = "story_id";
	public static final String PAGE_ID = "page_id";
	
	// story table columns
	public static final String STORY_TITLE = "story_title";
	public static final String STORY_AUTHOR = "story_author";
	public static final String ROOT = "root";
	
	// page table columns
	public static final String PAGE_TITLE = "page_title";
	public static final String PAGE_AUTHOR = "page_author";
	public static final String PAGE_TEXT = "page_text";
	public static final String READONLY = "read_only";
	
	// story_page columns
	public static final String NEXT_PAGE_ID = "next_page_id"; 
	
	// multimedia columns
	public static final String MULT_ID = "multimedia_id";
	public static final String DIRECTORY = "directory";
	public static final String INDEX = "multimedia_index";
	public static final String MULT_TYPE = "multimedia_type";
	public static final String SIZE = "pic_size";
	
	// table login
	public static final String USER = "user";
	
	public static final String CREATE_TABLE_LOGIN = "CREATE TABLE IF NOT EXISTS " 
													+ TABLE_LOGIN + " ("
													+ USER + " TEXT PRIMARY KEY)";
	
	public static final String CREATE_MULT_TABLE = "CREATE TABLE IF NOT EXISTS "
												   + TABLE_MULT + " ("
												   + MULT_ID + " INTEGER PRIMARY KEY, "
												   + INDEX + " INTEGER, "
												   + DIRECTORY + " TEXT, "
												   + PAGE_ID + " INTEGER, "
												   + MULT_TYPE + " TEXT CHECK ("
												   + MULT_TYPE + " = 'Picture' OR "
												   + MULT_TYPE + " = 'Video' OR "
												   + MULT_TYPE + " = 'SoundClip'), "
												   + SIZE + " INTEGER CHECK ("
												   + SIZE + " = 1 OR "
												   + SIZE + " = 2 OR "
												   + SIZE + " = 3), "
												   + "FOREIGN KEY (" + PAGE_ID
			  									   + ") REFERENCES " + TABLE_PAGE
			  									   + " (" + PAGE_ID + "))";
	
	public static final String CREATE_PAGE_TABLE = "CREATE TABLE IF NOT EXISTS "
												    + TABLE_PAGE + " ("
												    + PAGE_ID + " INTEGER PRIMARY KEY, "
												    + READONLY + " INTEGER, "
												    + PAGE_TITLE + " TEXT, "
												    + PAGE_AUTHOR + " TEXT, "
												    + PAGE_TEXT + " TEXT)";
	
	public static final String CREATE_STORY_TABLE = "CREATE TABLE IF NOT EXISTS " 
			  										 + TABLE_STORY + " ("
			  										 + STORY_ID + " INTEGER PRIMARY KEY, "
			  										 + STORY_TITLE + " TEXT, "
			  										 + STORY_AUTHOR + " TEXT, "
			  										 + ROOT + " INTEGER, "
			  										 + "FOREIGN KEY (" + ROOT
			  										 + ") REFERENCES " + TABLE_PAGE
			  										 + " (" + PAGE_ID + "))";
	
	public static final String CREATE_PAGE_CHILDREN_TABLE = "CREATE TABLE IF NOT EXISTS "
														  + TABLE_PAGE_CHILDREN + "(" + PAGE_ID + " INTEGER, "
														  + NEXT_PAGE_ID + " INTEGER, "
														  + "PRIMARY KEY (" 
														  + PAGE_ID + ", "
														  + NEXT_PAGE_ID + "), FOREIGN KEY ("
														  + PAGE_ID
														  + ") REFERENCES " + TABLE_PAGE
														  + " (" + PAGE_ID 
														  + "), FOREIGN KEY (" + NEXT_PAGE_ID
														  + ") REFERENCES " + TABLE_PAGE
														  + " (" + PAGE_ID + "))";
}

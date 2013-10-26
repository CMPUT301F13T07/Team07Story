package edu.ualberta.database;

public class Constant {
	// table titles
	public static final String TABLE_STORY = "Story";
	public static final String TABLE_PAGE = "Page";
	public static final String TABLE_STORY_PAGE = "Story_Page";
	//public static final String TABLE_MULTIMEDIA = "Multimedia";
	
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
	
	// story_page columns
	public static final String NEXT_PAGE_ID = "next_page_id"; 
	
	public static final String CREATE_PAGE_TABLE = "CREATE TABLE IF NOT EXISTS "
												    + TABLE_PAGE + " ("
												    + PAGE_ID + " INTEGER PRIMARY KEY, "
												    + PAGE_TITLE + " TEXT, "
												    + PAGE_AUTHOR + " TEXT)";
	
	public static final String CREATE_STORY_TABLE = "CREATE TABLE IF NOT EXISTS " 
			  										 + TABLE_STORY + " ("
			  										 + STORY_ID + " INTEGER PRIMARY KEY, "
			  										 + STORY_TITLE + " TEXT, "
			  										 + STORY_AUTHOR + " TEXT, "
			  										 + ROOT + " INTEGER, "
			  										 + "FOREIGN KEY (" + ROOT
			  										 + ") REFERENCES " + TABLE_PAGE
			  										 + " (" + PAGE_ID + "))";
	
	public static final String CREATE_STORY_PAGE_TABLE = "CREATE TABLE IF NOT EXISTS "
														  + TABLE_STORY_PAGE + " ("
														  + STORY_ID + " INTEGER, "
														  + PAGE_ID + " INTEGER, "
														  + NEXT_PAGE_ID + " INTEGER, "
														  + "PRIMARY KEY (" + STORY_ID 
														  + ", " + PAGE_ID + " ,"
														  + NEXT_PAGE_ID + "), FOREIGN KEY ("
														  + STORY_ID + "REFERENCES " 
														  + TABLE_STORY + " (" + STORY_ID 
														  + "), FOREIGN KEY (" + PAGE_ID
														  + ") REFERENCES " + TABLE_PAGE
														  + " (" + PAGE_ID 
														  + "), FOREIGN KEY (" + NEXT_PAGE_ID
														  + ") REFERENCES " + TABLE_PAGE
														  + " (" + PAGE_ID + "))";
}

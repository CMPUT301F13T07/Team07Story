package edu.ualberta.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Based on The Android Developer's Cookbook, Addison-Wesley, 2011 - Listing 9.10
// Also based on mnaylor's CMPUT301 Assignment1

public class DbHelper extends SQLiteOpenHelper{
	
	/**
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
    public DbHelper(Context context, String name, CursorFactory factory, 
    				int version) {
    	super(context, name, factory, version);
    }

    /**
     * Creates the tables which are subclasses of the Tables class
     * 
     * @see #Tables.java
     * @throws SQLiteException
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
        	db.execSQL(Constant.CREATE_STORY_TABLE);
        	db.execSQL(Constant.CREATE_PAGE_TABLE);
        	db.execSQL(Constant.CREATE_PAGE_CHILDREN_TABLE);
        	db.execSQL(Constant.CREATE_MULT_TABLE);
        	db.execSQL(Constant.CREATE_TABLE_LOGIN);
        } catch(SQLiteException ex) {
        	Log.v("Create table exception", ex.getMessage());
        }	
    }
    
    /**
     * Deletes and calls onCreate to recreate tables.
     * 
     * @see #onCreate(SQLiteDatabase db)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
    					  int newVersion) {
        db.execSQL("drop table if exists " + Constant.TABLE_STORY);
        db.execSQL("drop table if exists " + Constant.TABLE_PAGE);
        db.execSQL("drop table if exists " + Constant.TABLE_PAGE_CHILDREN);
        db.execSQL("drop table if exists " + Constant.TABLE_MULT);
        db.execSQL("drop table if exists " + Constant.TABLE_LOGIN);
    	onCreate(db);
    }
}

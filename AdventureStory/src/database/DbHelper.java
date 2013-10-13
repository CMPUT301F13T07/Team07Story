package database;

import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

// Based on The Android Developer's Cookbook, Addison-Wesley, 2011 - Listing 9.10
// Also based on mnaylor's CMPUT301 Assignment1
public class DbHelper extends SQLiteOpenHelper{
	
    public DbHelper(Context context, String name, CursorFactory factory, 
    				int version) {
    	super(context, name, factory, version);
    }

    /**
     * Creates the tables defined in Tables.java
     * 
     * @see #Tables.java
     * @throws SQLiteException
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	for (Map.Entry table: Table.table_create.entrySet()) {
        	try {
        		db.execSQL(Table.table_create.get(table));
        	} catch(SQLiteException ex) {
        		Log.v("Create table exception", ex.getMessage());
        	}	
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
    	for (Map.Entry table:Table.table_create.entrySet()) {
        	db.execSQL("drop table if exists " + table);	
    	}
    	onCreate(db);
    }
}

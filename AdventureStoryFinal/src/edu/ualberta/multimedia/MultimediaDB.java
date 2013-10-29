/*
 * NOTE: THIS IS A MOCK DATABASE SO I CAN TEST MULTIMEDIAS IN PAGES.
 * NOTE: THIS IS A MOCK DATABASE SO I CAN TEST MULTIMEDIAS IN PAGES.
 * NOTE: THIS IS A MOCK DATABASE SO I CAN TEST MULTIMEDIAS IN PAGES.
 * NOTE: THIS IS A MOCK DATABASE SO I CAN TEST MULTIMEDIAS IN PAGES.
 * NOTE: THIS IS A MOCK DATABASE SO I CAN TEST MULTIMEDIAS IN PAGES.
 * NOTE: THIS IS A MOCK DATABASE SO I CAN TEST MULTIMEDIAS IN PAGES.
 * NOTE: THIS IS A MOCK DATABASE SO I CAN TEST MULTIMEDIAS IN PAGES.
 */

// TODO: Add static methods for inserting/getting.
// 		 Add another integer so that these database save index. 
package edu.ualberta.multimedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MultimediaDB {
	static final String KEY_ROWID = "_id";				// id.
	static final String KEY_PICTUREID = "pic_id";		// Picture id.
	static final String KEY_INDEX = "index";			// position index.
	static final String KEY_DIRECTORY = "directory";	// filename.
	
	static final String TAG = "MultimediaDB";
	
	static final String DATABASE_NAME = "MultimediaDB";
	// I suggest separating the two to avoid duplicate resource.
	// This is for retreiving the fileNames of object.
	static final String BITMAP_TABLE = "bitmaptable";
	static final String SOUND_TABLE = "soundtable";
	static final String VIDEO_TABLE = "videotable";	
	// This is for retrieving MultimediaAbstract object.
	static final String PICTUREOBJ_TABLE = "pictureObj_table";
	static final String SOUNDOBJ_TABLE = "soundObj_table";
	static final String VIDEOOBJ_TABLE = "videoObj_table";
	
	static final int DATABASE_VERSION = 19;
	
	static final String BITMAP_DB_CREATE = 
			"CREATE TABLE " + BITMAP_TABLE + 
			" ("+KEY_ROWID + " INTEGER, " +
			KEY_DIRECTORY + " TEXT NOT NULL);";
	
	static final String SOUND_DB_CREATE = 
			"CREATE TABLE " + SOUND_TABLE + 
			" ("+KEY_ROWID + " INTEGER, " +
			KEY_DIRECTORY + " TEXT NOT NULL);";
	
	static final String VIDEO_DB_CREATE = 
			"CREATE TABLE " + VIDEO_TABLE + 
			" ("+KEY_ROWID + " INTEGER, " +
			KEY_DIRECTORY + " TEXT NOT NULL);";
	
	final Context mContext;
	DatabaseHelper mDBHelper;
	SQLiteDatabase mDB;
	
	public MultimediaDB(Context context) {
		mContext = context;
		// Create Bitmap Table.
		mDBHelper = new DatabaseHelper(context);		
	}
	
	private class DatabaseHelper extends SQLiteOpenHelper{		
		DatabaseHelper( Context context){
			super( context, DATABASE_NAME, null, DATABASE_VERSION);			
		}
		
		@Override
		public void onCreate(SQLiteDatabase db){
			try{
				db.execSQL(BITMAP_DB_CREATE);
				db.execSQL(SOUND_DB_CREATE);
				db.execSQL(VIDEO_DB_CREATE);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ){
			db.execSQL("DROP TABLE IF EXISTS " + BITMAP_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + SOUND_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + VIDEO_TABLE);
			onCreate(db);
		}
	}
	
	// Opens the database.
	public MultimediaDB open() throws SQLException{
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		mDBHelper.close();
	}
	
	public long _insertBitmapDirectory(int id, String directory){		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ROWID, id);
		initialValues.put(KEY_DIRECTORY, directory);
		
		return mDB.insert(BITMAP_TABLE, "xxx", initialValues);
	}
	
	public static long inserBitmapDirectory(int id, String directory, Context context){		
		MultimediaDB mdb = new MultimediaDB(context);
		mdb.open();
		long _id = mdb._insertBitmapDirectory(id, directory);
		mdb.close();
		return _id;
	}
	
	public String _getBitmapDirectory(long rowId) throws SQLException{
		Cursor cursor = mDB.query(true, BITMAP_TABLE, new String[]{ KEY_ROWID, KEY_DIRECTORY}, 
				KEY_ROWID+"="+rowId, null, null, null, null, null );
		if( cursor != null && cursor.getCount()>0){
			cursor.moveToFirst();
			String fileName = cursor.getString(cursor.getColumnIndex(KEY_DIRECTORY));
			cursor.close();
			return fileName;
		}else{
			cursor.close();
		}
		
		return null;
	}
	
	public static String getBitmapDirectory(long rowId, Context context){
		MultimediaDB mdb = new MultimediaDB(context);
		mdb.open();
		String fileName = mdb._getBitmapDirectory(rowId);
		mdb.close();
		return fileName;
	}
	
	public long _insertSoundDirectory(int id, String directory){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ROWID,  id);		
		initialValues.put(KEY_DIRECTORY, directory);
		
		return mDB.insert(SOUND_TABLE, "xxx", initialValues);
	}
	
	public static long insertSoundDirectory(int id, String directory, Context context){		
		MultimediaDB mdb = new MultimediaDB(context);
		mdb.open();
		long _id = mdb._insertSoundDirectory(id, directory);
		mdb.close();
		return _id;
	}
	
	public String _getSoundDirectory(long rowId) throws SQLException{
		Cursor cursor = mDB.query(true, SOUND_TABLE, new String[]{ KEY_ROWID, KEY_DIRECTORY}, 
				KEY_ROWID+"="+rowId, null, null, null, null, null );
		if( cursor != null && cursor.getCount()>0){
			cursor.moveToFirst();
			String fileName = cursor.getString(cursor.getColumnIndex(KEY_DIRECTORY));
			cursor.close();
			return fileName;
		}else{
			cursor.close();
		}
		
		return null;
	}
	
	public static String getSoundDirectory(long rowId, Context context){
		MultimediaDB mdb = new MultimediaDB(context);
		mdb.open();
		String fileName = mdb._getSoundDirectory(rowId);
		mdb.close();
		return fileName;
	}
	
	public long _insertVideoDirectory(int id, String directory){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ROWID, id);		
		initialValues.put(KEY_DIRECTORY, directory);
		
		return mDB.insert(VIDEO_TABLE, "xxx", initialValues);
	}
	
	public static long insertVideoDirectory(int id, String directory, Context context){		
		MultimediaDB mdb = new MultimediaDB(context);
		mdb.open();
		long _id = mdb._insertVideoDirectory(id, directory);
		mdb.close();
		return _id;
	}
	
	public String _getVideoDirectory(long rowId) throws SQLException{
		Cursor cursor = mDB.query(true, VIDEO_TABLE, new String[]{ KEY_ROWID, KEY_DIRECTORY}, 
				KEY_ROWID+"="+rowId, null, null, null, null, null );
		if( cursor != null && cursor.getCount()>0){
			cursor.moveToFirst();
			String fileName = cursor.getString(cursor.getColumnIndex(KEY_DIRECTORY));
			cursor.close();
			return fileName;
		}else{
			cursor.close();
		}
		
		return null;
	}
	
	public static String getVideoDirectory(long rowId, Context context){
		MultimediaDB mdb = new MultimediaDB(context);
		mdb.open();
		String fileName = mdb._getVideoDirectory(rowId);
		mdb.close();
		return fileName;
	}
}

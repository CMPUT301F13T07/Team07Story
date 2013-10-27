package com.example.multimedia;

// TODO: Add static methods for inserting/getting.

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
	static final String KEY_ROWID = "_id";				// Bitmap id.
	static final String KEY_NAME = "name";				// Filename (without extension).
	static final String KEY_FORMAT = "format";			// File format, will be mapped to
														//   extension.
	static final String KEY_BITMAP = "bitmapcolumn";	// Byte strings containing the bitmap.	
	static final String KEY_SOUND = "soundcolumn";		// Byte strings containing sound.
	static final String KEY_VIDEO = "videocolumn";		// Byte strings containing video.
	
	static final String TAG = "MultimediaDB";
	
	static final String DATABASE_NAME = "MultimediaDB";
	static final String BITMAP_TABLE = "bitmaptable";
	static final String SOUND_TABLE = "soundtable";
	static final String VIDEO_TABLE = "videotable";
	static final int DATABASE_VERSION = 10;
	
	static final String BITMAP_DB_CREATE = 
			"CREATE TABLE " + BITMAP_TABLE + 
			" ("+KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NAME + " TEXT NOT NULL, " +
			KEY_FORMAT + " TEXT NOT NULL, " +
			KEY_BITMAP + " BLOB);";
	
	static final String SOUND_DB_CREATE = 
			"CREATE TABLE " + SOUND_TABLE + 
			" ("+KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NAME + " TEXT NOT NULL, " +
			KEY_FORMAT + " TEXT NOT NULL, " +
			KEY_SOUND + " BLOB);";
	
	static final String VIDEO_DB_CREATE = 
			"CREATE TABLE " + VIDEO_TABLE + 
			" ("+KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NAME + " TEXT NOT NULL, " +
			KEY_FORMAT + " TEXT NOT NULL, " +
			KEY_VIDEO + " BLOB);";
	
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
	
	public long insertBitmap(String name, String format, Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_FORMAT, format);
		initialValues.put(KEY_BITMAP, byteArray);
		
		return mDB.insert(BITMAP_TABLE, "xxx", initialValues);
	}
	
	public Bitmap getBitmap(long rowId) throws SQLException{
		Cursor cursor = mDB.query(true, BITMAP_TABLE, new String[]{ KEY_ROWID, KEY_NAME,
				KEY_FORMAT, KEY_BITMAP}, KEY_ROWID+"="+rowId, null, null, null, 
				null, null );
		if( cursor != null){
			cursor.moveToFirst();
			byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(KEY_BITMAP));
			cursor.close();
			return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		}else{
			cursor.close();
		}
		
		return null;
	}
	
	public long insertSound(String name, String format, byte[] sound){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_FORMAT, format);
		initialValues.put(KEY_SOUND, sound);
		
		return mDB.insert(SOUND_TABLE, "xxx", initialValues);
	}
	
	public byte[] getSound(long rowId) throws SQLException{
		Cursor cursor = mDB.query(true, SOUND_TABLE, new String[]{ KEY_ROWID, KEY_NAME,
				KEY_FORMAT, KEY_SOUND}, KEY_ROWID+"="+rowId, null, null, null, 
				null, null );
		if( cursor != null){
			cursor.moveToFirst();
			byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(KEY_SOUND));
			cursor.close();
			return byteArray;
		}else{
			cursor.close();
		}
		
		return null;
	}
	
	public long insertVideo(String name, String format, byte[] sound){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_FORMAT, format);
		initialValues.put(KEY_VIDEO, sound);
		
		return mDB.insert(VIDEO_TABLE, "xxx", initialValues);
	}
	
	public byte[] getVideo(long rowId) throws SQLException{
		Cursor cursor = mDB.query(true, VIDEO_TABLE, new String[]{ KEY_ROWID, KEY_NAME,
				KEY_FORMAT, KEY_VIDEO}, KEY_ROWID+"="+rowId, null, null, null, 
				null, null );
		if( cursor != null){
			cursor.moveToFirst();
			byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(KEY_VIDEO));
			cursor.close();
			return byteArray;
		}else{
			cursor.close();
		}
		
		return null;
	}
	
	// -- STATIC SAVING/LOADING METHODS --
	// Used by modules to save their own localized files or 
	// could be used by the main application itself.
	public static void save( File file, byte[] content, Context context ){
		try{
			OutputStream os = new FileOutputStream(file);			
			os.write(content);
			os.close();
		}catch( IOException ioe ){
			ioe.printStackTrace();
		}
	}

	// Legacy code for loading files in a given fileName.
	public static byte[] load( File file, Context context ) throws IOException{
		InputStream fin = null;
		try{
			fin = new FileInputStream(file);
			byte[] byteArray = new byte[(int)file.length()];
			
			fin.read(byteArray);
			fin.close();
			return byteArray;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}		
		return null;
	}
}

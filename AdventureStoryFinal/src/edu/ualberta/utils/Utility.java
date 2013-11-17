package edu.ualberta.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;

// This class is meant to have assorted static methods/constants since they don't
// have the merit of belonging to a class of their own.
public class Utility {
	static public byte[] inputStreamToByteArray(InputStream inStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    byte[] buffer = new byte[8192];
	    int bytesRead;
	    while ((bytesRead = inStream.read(buffer)) > 0) {
	        baos.write(buffer, 0, bytesRead);
	    }
	    return baos.toByteArray();
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
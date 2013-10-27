package com.example.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

// This class is meant to have assorted static methods/constants since they don't
// have the merit of belonging to a class of their own.
public class Utility {
	static public String BITMAP_DB = "bitmap";
	
	// TODO: move his to SoundClip.java
	static public byte[] inputStreamToByteArray(InputStream inStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    byte[] buffer = new byte[8192];
	    int bytesRead;
	    while ((bytesRead = inStream.read(buffer)) > 0) {
	        baos.write(buffer, 0, bytesRead);
	    }
	    return baos.toByteArray();
	}
}
package eu.luckyApp.stickers.utils;

import java.io.File;

public class StickerUtils {
	
	/*
	 * Get the extension of a file.
	 */  
	public static final String getExtension(File f) {
	    String ext = null;
	    String s = f.getName();
	    int i = s.lastIndexOf('.');

	    if (i > 0 &&  i < s.length() - 1) {
	        ext = s.substring(i+1).toLowerCase();
	    }
	    return ext;
	}

}

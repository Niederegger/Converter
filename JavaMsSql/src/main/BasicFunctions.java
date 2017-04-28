package main;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Alexey Gasevic
 *
 *         This class is used to bundle basic functions needed for this project
 */
public class BasicFunctions {

	/**
	 * use trim to cut your String to a wished width
	 * 
	 * @param s
	 *            - your String
	 * @param width
	 *            - wanted width
	 * @return cutted String to less or equal width
	 */
	public static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1);
		else
			return s;
	}

	/**
	 * prints String in wanted format (basically just uses String.format())
	 * 
	 * @param msg
	 *            - String format msg
	 * @param args
	 *            - objects into format
	 */
	public static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	/**
	 * parses message and return first result matching pattern
	 * 
	 * @param message
	 *            - some message
	 * @param p
	 *            - pattern
	 */
	public static String match(String message, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(message);
		if (m.find()) {
			return m.group(0);
		}
		return null;
	}
	
	/**
	 * checks whether this path contains a file or not
	 * 
	 * @param path
	 * @param file
	 * @return boolean
	 */
	public static boolean containsFile(String path, String file) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if (listOfFiles[i].getName().equals(file))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the config file has the right ending and exists or not 
	 * @param path - path to config file
	 * @return boolean
	 */
	public static boolean checkConfigPath(String path){
		if(path.endsWith(".conf")){
			File f = new File(path);
			return (f.exists() && !f.isDirectory());
		}
		return false;
	}
}

package de.vv.stockstore.converter;

import java.io.File;
/**
 * 
 * @author Alexey Gasevic
 *
 *         This class is used to bundle basic functions needed for this project
 */
public class BasicFunctions {
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

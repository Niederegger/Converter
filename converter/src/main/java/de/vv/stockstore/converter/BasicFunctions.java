package de.vv.stockstore.converter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BasicFunctions {

	//--------------------------------------------------------------------------------------------------------------------
	// Config
	//--------------------------------------------------------------------------------------------------------------------
	
	public static boolean initConfig(String file) {

		if (!checkConfigPath(file)) {                        // ueberpruefe ob config exestiert
			App.logger.error("Invalid Config path and/or name: {}", file);
			return false;
		}

		Gson gson = new Gson();
		try {  																															// lade das Json Object aus der Text Datei in die config-Variable
			App.config = gson.fromJson(new FileReader(file), Config.class);		// setzt die config-Variable in App.config
			return true;
		} catch (Exception e) {
			App.logger.error("Exception: {}", e.getMessage());
		}
		return false;																												// Config konnte nicht erstellt werden
	}
	
	/** 
	 * This function is used to print the Config, so one has the right format
	 */
	public static void printConfig() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonInString = gson.toJson(App.config);
		System.out.println(jsonInString);
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
	
	//--------------------------------------------------------------------------------------------------------------------
	// Files
	//--------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Converts Path + File + Ending into one String
	 * 
	 * @return
	 */
	public static File getFile() {
		for (File f : getAllFileFromDir(App.config.Path)) {
			if (f.getName().contains(App.config.File) && !f.getName().contains("~")
					&& f.getName().endsWith(App.config.FileEnding)) {
				return f;
			}
		}
		return null;
	}
	
	/**
	 * returns a list of all files and folder in this path
	 * 
	 * @param path
	 * @return
	 */
	public static File[] getAllFileFromDir(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	}
	
	/**
	 * Writes a file from destination to another destination
	 * 
	 * @param from
	 *            - origin destination
	 * @param to
	 *            - next destination
	 */
	public static void moveFile(String from, String to) {
		System.out.println("moving file to: " + to);
		File a = new File(from);
		a.renameTo(new File(to));
		a.delete();
	}
	
	/**
	 * renames a file
	 * 
	 * @param f
	 *            - file
	 * @return renamed file name without path
	 */
	public static String renameFile(File f) {
		String name = f.getName();
		String[] spl = name.split("\\.");
		name = spl[0] + "~." + spl[1];
		File newFile = new File(f.getParent(), name);
		try {
			Files.move(f.toPath(), newFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}

}

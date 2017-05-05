package de.vv.stockstore.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Converter {

	public static Config config = new Config();

	/**
	 * This function is used to print the Config, so one has the right format
	 */
	public static void printConfig() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonInString = gson.toJson(config);
		System.out.println(jsonInString);
	}

	/**
	 * main
	 * 
	 * @param args
	 *            this program is supposed to be run with the path+config name
	 *            as arg[0]
	 */
	public static void main(String[] args) {
		// Program takes one Parameter: location of config.conf
		if (args.length <= 0 || args.length > 1) {
			System.err.println(
					"Use this Programm like this: java -jar hiesp.jar 'Path of config'.	 Config Files end have the eding .conf");
			return;
		} else {
			// try to load config
			String file = args[0];
			// does this file exists?
			if (BasicFunctions.checkConfigPath(file)) {
				// is it possible to load config?
				if (loadConfig(file)) {
					// start programm
					programm();
				} else { // faulty config
					System.err.println("This Config file doesn't fit the definition.");
				}
			} else {// file doesn't exist
				System.err.println("This Config file doesn't exists: " + file + ".");
			}
		}
	}

	/**
	 * Parsing file into queries and sending them to db, at the end,
	 * storedProcedure is called to update db
	 */
	public static void programm() {
		File file = getFile();
		if (file != null) {
			// changing filename -> this file is in use
			System.out.println("Renaming file");
			String newFile = renameFile(file);
			System.out.println("File renamed");
			DbConnect.sendQueries(Reader.read(config.Path + newFile));
			moveFile(config.Path + newFile, config.Path + "bak\\" + file.getName());
		} else {
			System.err.println("No file found.");
		}

	}

	/**
	 * Converts Path + File + Ending into one String
	 * 
	 * @return
	 */
	public static File getFile() {
		for (File f : files(config.Path)) {
			if (f.getName().contains(config.File) && !f.getName().contains("~")
					&& f.getName().endsWith(config.FileEnding)) {
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
	public static File[] files(String path) {
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

	/**
	 * loading Config
	 * 
	 * @param file
	 * @return whether it was possible to load config or not
	 */
	public static boolean loadConfig(String file) {
		Gson gson = new Gson();
		try {
			config = gson.fromJson(new FileReader(file), Config.class);
			return true;
		} catch (JsonSyntaxException e) {
			System.err.println("JsonSyntaxException: " + e.getMessage());
		} catch (JsonIOException e) {
			System.err.println("JsonIOException: " + e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		}
		return false;
	}
}

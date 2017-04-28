package main;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class start {
	public static Config config = new Config();

	public static void printConfig() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonInString = gson.toJson(config);
		System.out.println(jsonInString);
	}

//	public static void main(String[] args) {
//		System.out.println(args.length);
//		if (args.length <= 0 || args.length > 1) {
//			System.err.println(
//					"Use this Programm like this: java -jar hiesp.jar 'Path of config'.	 Config Files end have the eding .conf");
//			return;
//		} else {
//			String file = args[0];
//			if (BasicFunctions.checkConfigPath(file)) {
//				if (loadConfig(file)) {
//					programm();
//				} else {
//					System.err.println("This Config file doesn't fit the definition.");
//				}
//			} else {
//				System.err.println("This Config file doesn't exists: " + file + ".");
//			}
//		}
//	}

	public static void programm() {
		DbConnect.sendQueries(reader.read(filePath()));
	}

	public static String filePath() {
		return config.Path + config.File + config.FileEnding;
	}

	/**
	 * loading Config
	 * 
	 * @param file
	 * @return
	 */
	public static boolean loadConfig(String file) {
		Gson gson = new Gson();
		try {
			config = gson.fromJson(new FileReader(file), Config.class);
			// System.out.println(config);
			return true;
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			System.err.println("JsonSyntaxException | JsonIOException | FileNotFoundException: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	 public static void main(String[] args) {
	 printConfig();
	 String file =
	 "C:\\Users\\Alexey Gasevic\\Documents\\hiesp\\wassabi\\some.conf";
	 System.out.println(loadConfig(file));
	
	 }

}

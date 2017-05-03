package main;

import java.io.FileNotFoundException;
import java.io.FileReader;

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
	 * @param args this program is supposed to be run with the path+config name as arg[0]
	 */
	public static void main(String[] args) {
//		if (args.length <= 0 || args.length > 1) { // Ueberpruefen ob genau ein Argument, der Config-Pfad angegeben wurde.
//			System.err.println( //   wenn nicht, wird eine Error-Message geprintet
//					"Use this Programm like this: java -jar hiesp.jar 'Path of config'.	 Config Files end have the eding .conf");
//			return;//   und das Programm beendet
//		} else {
//			
			String file = "D:\\Alexey\\EclipseWorkspace\\JavaMsSql\\Configs\\some.conf";// args[0]; // lese den Pfad aus
			if (BasicFunctions.checkConfigPath(file)) { // UeberPruefe ob dieser Pfad Ok ist
				if (loadConfig(file)) { // falls der PFad korrekt ist, lade diese Config, und Ueberpruefe ob diese ebenfalls korrekt ist
					programm(); // die Config wurde geladen und beinhaltet das richtige Fromat, nun kann der Konvertierung und uebergabe an die Datenbank beginnen
				} else { // die Config beinhaltet ein falsches Format, ein Error wird geprintet
					System.err.println("This Config file doesn't fit the definition.");
				}
			} else {// der Pfad war nicht korrekt, ein Error wird geprintet
				System.err.println("This Config file doesn't exists: " + file + ".");
			}
//		}
	}

	/**
	 * Parsing file into queries and sending them to db,
	 * at the end, storedProcedure is called to update db
	 */
	public static void programm() {
		// hier sind mehrere funktionen gepiped 
		// Der Datei-Ort und -Name werden dem Reader uebergeben
		// dieser geht die Datei durch und erstellt zu dem Inhalt der
		// Datei Queries, welche an DbConnect.sendQueries() uebergeben werden.
		// sendQueries sendet im anschluss alle Queries an die DatenBank
		// und führt zum abschluss die StoredProcedure auf um die DatenBank
		// zu aktualisieren
		DbConnect.sendQueries(Reader.read(filePath()));
	}

	/**
	 * Converts Path + File + Ending into one String
	 * @return
	 */
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

	// public static void main(String[] args) {
	// printConfig();
	// String file =
	// "C:\\Users\\Alexey Gasevic\\Documents\\hiesp\\wassabi\\some.conf";
	// System.out.println(loadConfig(file));
	//
	// }

}

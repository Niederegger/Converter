package de.vv.stockstore.converter;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class App {
	final static Logger logger = LoggerFactory.getLogger(App.class);

	public static Config config = new Config();

	public static void main(String[] args) {                              // das Programm erwartet genau ein Argument (den Pfad der Config-Datei)

		if (args.length != 1) {																							// wurde dem Programm ein Parameter uebergeben?
			logger.error("Invalid amount of arguments: {}", args.length);
			return;
		}

		// args[0] enthaellt den Pfad zu Config
		// ab hier ist sicher, dass genau ein Parameter uebergeben wurde

		if (BasicFunctions.initConfig(args[0]))                             // initialiserit die config-Variable
			beginProgram();                                                   // nun kann versucht werden die Datei zu laden
		else
			logger.error("Invalid Config: {}", args[0]);
	}

	/**
	 * Parsing file into queries and sending them to db, at the end,
	 * storedProcedure is called to update db
	 */
	public static void beginProgram() {
		File file = BasicFunctions.getFile();																// hohlt die erste Datei, welche mit dem richtigen Namen beginnt
		
		if (file == null) {																									// falls keine File gefunden wurde, gibt's keine zum parsen
			logger.error("File not found: {}", file);
			return;
		}

		logger.info("Occupying file: {}", file);
		
		String occupiedFile = BasicFunctions.renameFile(file);							// benennt die Datei um, damit bekannt ist, dass diese im Betrieb ist
		
		DbConnect.establishConnection();
		
		Reader.read(config.Path + occupiedFile, file.getName());						// jetz wird die Datei geparsed und queries inserted
		
		DbConnect.closeConnection();
		
		BasicFunctions.moveFile(config.Path + occupiedFile, config.Path + "bak\\" + file.getName()); // schreibt die Datei in den bak Ordner

	}

}

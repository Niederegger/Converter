package de.vv.stockstore.converter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.vv.stockstore.converter.models.*;

public class Reader {
	final static Logger logger = LoggerFactory.getLogger(Reader.class);

	/**
	 * reads File and converts each entry into bunch of queries
	 * 
	 * @param file
	 *          this is the File which is going to be read
	 * @param fileName
	 *          origin name of this file (without '~')
	 * @return
	 */
	public static void read(String file, String fileName) {
		logger.info("Converter start.");
		ContainerQuery containerQuery = new ContainerQuery(App.config.Source_ID, App.config.File, App.config.URLSource,
				App.config.Comment);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int counter = 0;																														// wird gebraucht um den Kopf der Csv Datei zu "ignorieren"
			while ((line = br.readLine()) != null) {
				if (counter >= App.config.RowStart) {																			// der Kopf der Datei wurde ignoriert, jetzt beginnen die Daten
					ContainerRow qr = convertLine(line);																		// gibt die Zeile weiter, um diese in eine ContainerRow zu wandeln

					if (qr != null)																													// sicherheitsmasnahme falls null returned wird, anderfalls -> NullpointException
						containerQuery.add(qr);																								// füge die Konvertierte Zeile dem Query-Container hinzu

					if (containerQuery.rowAmount() >= App.config.MaxRowsTillInsert) {				// falls genug Inhalt akkumuliert wurde, wird dieser Inserted und gesäubert
						DbConnect.insertQueries(containerQuery, fileName);
						containerQuery.resetRows();																						// entlastet den Arbeitsspeicher
					}
				}
				counter++;
			}
			DbConnect.insertQueries(containerQuery, fileName);													// Inserten der Verbliebenen Daten
			DbConnect.execUpdateProcedure(fileName);																		// Aufruf der Stored-Procedure
		} catch (IOException e) {
			logger.error("IOException: {}", e.getMessage());
		}
		logger.info("Converter end.");
		return;
	}

	/**
	 * gets a TableRow and converts all necessary Data into queries
	 * 
	 * @param line
	 * @return
	 */
	public static ContainerRow convertLine(String line) {

		String[] columns = line.split(App.config.Seperator);
		if (columns.length != App.config.RowAmount) { 																// falls die Anzahl der Reihen nicht stimmt, liegt ein Fehler vor (csv/Zeilenumbruch/Parser)
			System.out.println("mismatch: " + columns.length);
			logger.trace(line);																													// diese Spalte wird im Trace-Log vermerkt
			return null;																																// null returned, damit die obere Funktion es nachvollziehen kann
		} else {																																			// ansonsten war die Reihe OK und kann verarbeitet werden
			// i - ISIN
			// m - MIC 			=> IMD, ISIN/MIC/DATE-Values
			// d - Date
			String[] imdValues = fetchIMD_Values(columns); 															// holt ISIN, MIC und AS_OF_DATE 

			// folglich wird ein Container fuer die Reihe erstellt, dieser parsed den Array mit Werten selbststaendig
			ContainerRow row_container = new ContainerRow(imdValues[0], imdValues[1], imdValues[2], columns);

			return row_container;

		}
	}

	/**
	 * collects MIC, ISIN and Date from current line Position 0 -> ISIN Position
	 * 1 -> MIC Position 2 -> Date
	 * 
	 * @param line
	 *          current row
	 * @return array of value Strings
	 */
	public static String[] fetchIMD_Values(String[] line) {
		String[] result = new String[3];
		result[0] = line[App.config.IsinPosition].trim(); 														// ISIN
		result[1] = line[App.config.MicPosition].trim();  														// MIC

		if (App.config.DatePosition != -1) {																					// falls DatePsition nicht -1 gesetzt wurde, wird ein As_Of_Date erwartet
			result[2] = line[App.config.DatePosition].trim();  													// AS_OF_DATE
			String[] date = result[2].split("\\.");
			if (date.length != 3) {  														 												// Es wird erwartet, dass das Datum aus 3 Teilen besteht
				logger.error("Corrupted Date: {}", result[2]);
				result[2] = null;
			} else { // changing date order
				result[2] = (date[2] + date[1] + date[0]).trim();													// Das Datum wird folgend konverteiert: yyyyMMdd
			}
		}

		return result;
	}
}

package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Reader {

	/**
	 * reads File and converts each entry into bunch of queries
	 * @param file
	 * @return
	 */
	public static ContainerQuery read(String file) {
		System.out.println("started Reading");
		// Sammlung der gesammten Query Bundles
		ContainerQuery query_container = new ContainerQuery(Converter.config.Source_ID, Converter.config.File, Converter.config.URLSource, Converter.config.Comment);
//		Vector<StringBuilder> query_Collection = new Vector<StringBuilder>();
		// Starte das File-Parsing
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line; // Akku fuer jeden Tabelleneintrag
			// dieser Counter zaehlt die Eintraege bis die tatsaechlichen Werte beginnen
			int counter = 0;
			// diese flag schaut ob der akutelle Query-Bundle bereits der Query-Sammlung hinzugefuegt wurde
//			boolean query_added = false; 
//			// akku des Query-Bundles
//			StringBuilder query_Bundle = new StringBuilder();
			// durchlaueft die Datei und verarbeitet die Eintraege
			while ((line = br.readLine()) != null ) { 
				// ab dem 5ten Eintrag beginnen die Werte, vorher
				// kommen nur Header INformationen
				if(counter >= 5){ // <- dieser parameter gehoert in die Config
					// uebergabe des aktuellen Eintrages an die Ueberarbeitungsfunktion
					// und anschließende konkatenation an den Query-Bundle
					//---------------------------------------------
					// Hier muss die anpassung rein der neuen Container
					//---------------------------------------------
					ContainerRow qr = workLine(line);
					if(qr != null)
					query_container.add(qr);
					
					//---------------------------------------------
//					query_Bundle.append(workLine(line));
//					// Falls die anzahl der gebuendelten Query das vordefinierte Maximum
//					// ueberschreitet packe das Bundle in die Sammlung und starte ein neues
//					if(query_amount>= query_Maximum){
//						query_Collection.add(query_Bundle);
//						query_Bundle = new StringBuilder();
//						query_amount = 0;
//						query_added = true;
//					} else query_added = false;
				}
				counter++;
			} // falls das letzte Bundle noch nicht der Sammlung hinzugefuegt wurde, 
			// fuege dieses ebenfalls der Sammlung hinzu
//			if(!query_added)query_Collection.add(query_Bundle);
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("finished Reading");
		return query_container;
	}

	// diesen Wert moechte ich in der Config fest setzen
	public static final int query_Maximum = 10000;
	// dieser Wert wird in workLine inkrementiert sodass
	// die Funktion read weiss wie viele Queries bereits akkumuliert wurden
	public static int query_amount = 0;
	
	/**
	 * gets a TableRow and converts all necessary Data into queries
	 * @param line
	 * @return
	 */
	public static ContainerRow workLine(String line) {
		
		
		String[] columns = line.split(Converter.config.Seperator); 
		if (columns.length == 123) { // this parameter has to be changeable <- put it into a config
			// i -ISIN
			// m - MIC
			// d - Date
			String[] imdValues = getDefValues(columns); // hier werden die Werte von ISIN, MIC und Datum aus der Reihe akkumuliert
			ContainerRow row_container = new ContainerRow(imdValues[0], imdValues[1], imdValues[2], columns);
			
			// obsolete
//			StringBuilder query = new StringBuilder(); 
//			String fieldValue; // diese Variable akkumiliert den Wert
//			String fieldName; // diese Variable akkumuliert den Feldnamen
//			// nun werden alle Feldnamen abgearbeitet und somit die Daten aus dem Tabelleneintrag verarbeitet
//			for (int i = 0; i < Converter.config.MasterValuesFields.length; i++) {
//				fieldValue = columns[Converter.config.Positions[i]]; // hohlt den Wert aus dem Vordefinierten Bereich
//				if(fieldValue != null && !fieldValue.equals("")){ // if value isn't null and empty, create a query
//					fieldName = Converter.config.MasterValuesFields[i]; // hohlt den Feldnamen aus der Matrix
//					// der Wert wird mit dem Feldnamen an die Funktion uebermittelt, welche daraus eine Query
//					// schreibt und diese der Query Sammlung hinzufuegt
//					query.append(createQuery( //  aufruf zur Erstellung der Query
//							fieldName,  //  uebergabe des Feldnamens
//							controlValue(fieldValue, fieldName), // uebergabe des korigierten Wertes
//							imdValues[0],  // uebergabe von der ISIN
//							imdValues[1],  // uebergabe der MIC
//							// anschließend wird das Datum uebergeben, hier muss geschaut werden
//							// ob der Feldname so definiert wurde, dass ein Ist-Datum benoetigt wird
//							(Converter.config.MV_AS_OF_DATE_NEEDED[i] ? imdValues[2] : null)));
//					query.append("\n");	
//					query_amount++; // inkrementiere den Query-Zaehler
//				}
//			}
			return row_container;
		} else {
			// Hier war der Tabellen eintrag Fehlerhaft, dieser soll anschließend
			// an einem Ort Akkumuliert werden um gegebenfalls abgearbeitet werden zu koennen
			// oder die Fehlerstelle abzuarbeiten
			return null;
			// save line in backup, this line didn't work properly
		}
	}

	/**
	 * collects MIC, ISIN and Date from current line
	 * Position 0 -> ISIN
	 * Position 1 -> MIC
	 * Position 2 -> Date
	 * @param line
	 *            current row
	 * @return array of value Strings
	 */
	public static String[] getDefValues(String[] line) {
		String[] ret = new String[3];
		ret[0] = line[Converter.config.IsinPosition]; // hohlt die ISIN aus der Datei-Tabelle
		ret[1] = line[Converter.config.MicPosition];  // hohlt die MIC aus der Dateie-Tabelle
		ret[2] = line[Converter.config.DatePosition]; // hohlt das Datum aus der Datei-Tabelle
		String[] date = ret[2].split("\\."); 		  
		if(date.length != 3){	// hier ist ein Fehler im Format des Datums
			System.err.println("Corrupted Date: " + ret[2]);
			ret[2] = null;
		} else { // Konvertiert das Datum in das akzeptierte DB format
			ret[2] = date[2] + date[1] + date[0];
		}
		return ret;
	}

	// obsolete
//	/**
//	 * creates query for one insert to db
//	 * @param fieldName
//	 * @param value
//	 * @param ISIN
//	 * @param MIC
//	 * @param Date
//	 * @return
//	 */
//	public static StringBuilder createQuery(String fieldName, String value, String ISIN, String MIC, String Date) {
//		// diese funktion konvertiert ein Feldname/Value paar in eine Query
//		// ein Beispiel:
//		// Insert vv_mastervalues_upload ( MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_DATA_ORIGIN, MVU_URLSOURCE, MVU_COMMENT ) 
//		// values ('DBAG', 'AN8068571086', 'XETR', 'Mnemonic', 'SCL', '20170427 allTradableInstruments.txt', 'http://www.deutsche-boerse-cash-market.com/dbcm-de/instrumente-statistiken/alle-handelbaren-instrumente/boersefrankfurt', 'Manuell von Kay');
//		// Achtung: Das arbeiten mit Strings kostet sehr viele Resourcen
//		// -> deshalb mit StringBuilder
//		StringBuilder query = new StringBuilder();
//		String Head1 = "Insert vv_mastervalues_upload ( MVU_SOURCE_ID, MVU_ISIN, MVU_MIC,";
//		String Head2 = " MVU_FIELDNAME, MVU_STRINGVALUE, MVU_DATA_ORIGIN, MVU_URLSOURCE, MVU_COMMENT )";
//		String ASOFDATE = "MVU_AS_OF_DATE,";
//		String Values1 = " values ( ";
//		String Values2 = "' );";
//		query.append(Head1);
//		if (Date != null) {
//			query.append(ASOFDATE);
//		}
//		query.append(Head2);
//		query.append(Values1);
//		query.append("'");
//		query.append(Converter.config.Source_ID);
//		query.append("', '");
//		query.append(ISIN);
//		query.append("', '");
//		query.append(MIC);
//		query.append("', '");
//		if (Date != null) {
//			query.append(Date);
//			query.append("', '");
//		}
//		query.append(fieldName);
//		query.append("', '");
//		query.append(value);
//		query.append("', '");
//		query.append(Converter.config.File);
//		query.append("', '");
//		query.append(Converter.config.URLSource);
//		query.append("', '");
//		query.append(Converter.config.Comment);
//		query.append(Values2);
//		return query;
//	}
}

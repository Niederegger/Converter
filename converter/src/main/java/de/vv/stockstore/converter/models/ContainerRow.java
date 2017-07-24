package de.vv.stockstore.converter.models;

import de.vv.stockstore.converter.App;

/**
 * ContainerRow stores all value from one row of an entry
 * 
 * @author Alexey Gasevic
 *
 */
public class ContainerRow {
	public String ISIN;
	public String MIC;
	public String AOD;
	public String[] values;

	/**
	 * creates an instance of ContainerRow, fills fixed RowValues and collects
	 * all other Values from destinated columns
	 * 
	 * @param ISIN
	 * @param MIC
	 * @param AOD
	 * @param columns
	 */
	public ContainerRow(String ISIN, String MIC, String AOD, String[] columns) {
		this.ISIN = ISIN;																					// Diese Wertwurden vom Reader ausgelesen
		this.MIC = MIC;																						// es wird erwartet, dass jede Zeile diese
		this.AOD = AOD;																						// Werde beinhaltet (zumindest die ISIN)
		
		init(columns);																						// lesen des Arrays und vermerken der Daten
	}

	/**
	 * get's an array of all columns and collects needed values
	 * 
	 * @param columns
	 */
	private void init(String[] columns) {
		int fieldAmt = App.config.MasterValuesFields.length;									// Anzahl der gewuenschten Eintraege pro Zeile
		values = new String[fieldAmt];																				// allokiert ein Array als Speicher
		String fieldValue;																										// akkumulator fuer aktuelle Feldwert
		String fieldName;																											// akkumulator fuer aktuelle FeldName
		for (int i = 0; i < fieldAmt; i++) {
			fieldName = App.config.MasterValuesFields[i];												// holt Feldnamen aus dem Config-Array "MasterValuesFields"
			fieldValue = columns[App.config.Positions[i]].trim();								// holt den Wert bezüglich des aktuellen Feldnamen, dieser ist verlinkt durch den Config-Array "Positions"
			values[i] = controlValue(fieldValue, fieldName);										// anschliesend wird ueberprueft, ob der Wert eventuell fehlerhaft ist und angepasst werden soll
		}
	}

	/**
	 * Controlls values:
	 * 
	 * one Apostrophe is replaced by two (SQL conventions), 
	 * 
	 * if WKN is larger than 6 characters, cut the first ones out
	 * 
	 * @param value
	 * @param fieldName
	 * @return
	 */
	public static String controlValue(String value, String fieldName) {
		
		if (value.contains("'"))																							// falls 1 Apostroph vorkommt, werden daraus 2 gemacht
			value = value.replaceAll("'", "''");
		
		if (fieldName.equals("WKN") && value.length() > 6)										// falls die WKN aus mehr als 6 Zeichen bestehth, werden nur die letzten 6 Zeichen genommen
			value = value.substring(value.length() - 6, value.length());
		
		return value;
	}
	
	/**
	 * basic toString function, for debug purpose
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ISIN: ");
		sb.append(ISIN);
		sb.append("\nMIC: ");
		sb.append(MIC);
		sb.append("\nAOD: ");
		sb.append(AOD);
		String fieldValue;
		String fieldName;
		sb.append("\n");
		int fieldAmt = App.config.MasterValuesFields.length;
		for (int i = 0; i < fieldAmt; i++) {
			fieldName = App.config.MasterValuesFields[i];
			fieldValue = values[i];
			sb.append(fieldName);
			sb.append(": ");
			sb.append(fieldValue);
			sb.append("\n");
		}
		return super.toString();
	}

}

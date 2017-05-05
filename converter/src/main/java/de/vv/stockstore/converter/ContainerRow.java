package de.vv.stockstore.converter;

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
		int fieldAmt = Converter.config.MasterValuesFields.length;
		for (int i = 0; i < fieldAmt; i++) {
			fieldName = Converter.config.MasterValuesFields[i];
			fieldValue = values[i];
			sb.append(fieldName);
			sb.append(": ");
			sb.append(fieldValue);
			sb.append("\n");
		}
		return super.toString();
	}

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
		this.ISIN = ISIN;
		this.MIC = MIC;
		this.AOD = AOD;
		init(columns);
	}

	/**
	 * get's an array of all columns and collects needed values
	 * 
	 * @param columns
	 */
	private void init(String[] columns) {
		int fieldAmt = Converter.config.MasterValuesFields.length;
		values = new String[fieldAmt];
		String fieldValue;
		String fieldName;
		for (int i = 0; i < fieldAmt; i++) {
			fieldName = Converter.config.MasterValuesFields[i];
			fieldValue = columns[Converter.config.Positions[i]];
			values[i] = controlValue(fieldValue, fieldName);
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
		// falls 1 Apostroph vorkommt, werden daraus 2 gemacht
		if (value.contains("'"))
			value = value.replaceAll("'", "''");
		// falls die WKN aus mehr als 6 Zeichen bestehth, werden nur die letzten
		// 6 Zeichen genommen
		if (fieldName.equals("WKN") && value.length() > 6)
			value = value.substring(value.length() - 6, value.length());
		return value;
	}

}

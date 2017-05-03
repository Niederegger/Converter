package main;

public class ContainerRow{
	public String ISIN;
	public String MIC;
	public String AOD;
	
	
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
			fieldName = Converter.config.MasterValuesFields[i]; // hohlt den Feldnamen aus der Matrix 
			fieldValue = values[i]; // hohlt den Wert aus dem Vordefinierten Bereich
			sb.append(fieldName);
			sb.append(": ");
			sb.append(fieldValue);
			sb.append("\n");
		}
		
		return super.toString();
	}
	
	public String[] values;
	
	public ContainerRow(String ISIN, String MIC, String AOD, String[] columns){
		this.ISIN = ISIN;
		this.MIC = MIC;
		this.AOD = AOD;
		init(columns);
	}
	
	private void init(String[] columns){
		int fieldAmt = Converter.config.MasterValuesFields.length;
		values = new String[fieldAmt];
		String fieldValue;
		String fieldName;
		for (int i = 0; i < fieldAmt; i++) {
			fieldName = Converter.config.MasterValuesFields[i]; // hohlt den Feldnamen aus der Matrix 
			fieldValue = columns[Converter.config.Positions[i]]; // hohlt den Wert aus dem Vordefinierten Bereich
			values[i] = controlValue(fieldValue, fieldName);
		}
	}
	
	/**
	 * Cheks value and repairs it if it's faulty
	 * @param value
	 * @param fieldName
	 * @return
	 */
	public static String controlValue(String value, String fieldName){
		// falls 1 Apostroph vorkommt, werden daraus 2 gemacht
		if(value.contains("'")) value = value.replaceAll("'", "''");
		// falls die WKN aus mehr als 6 Zeichen bestehth, werden nur die letzten 6 Zeichen genommen 
		if(fieldName.equals("WKN") && value.length()>6)value=value.substring(value.length()-6, value.length());
		return value;
	}
	
}

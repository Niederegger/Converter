package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class reader {

	public static Vector<StringBuilder> read(String file) {
		Vector<StringBuilder> queries = new Vector<StringBuilder>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String line;
			int i = 0;
			boolean added = false;
			StringBuilder storedQuery = new StringBuilder();
			while ((line = br.readLine()) != null ) { // && i < 100
//				System.out.println(i +"\t-> "+line);
				if(i >= 5){
					
					storedQuery.append(workLine(line));
					if(qamt>= maxQueries){
						queries.add(storedQuery);
						storedQuery = new StringBuilder();
						qamt = 0;
						added = true;
					} else added = false;
				}
				
					
				i++;
			}
			if(!added)queries.add(storedQuery);

		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
			e.printStackTrace();
		}
		return queries;
	}

	public static final int maxQueries = 10000;
	public static int qamt = 0;
	public static StringBuilder workLine(String line) {
		String[] sa = line.split(";");
		if (sa.length == 123) { // this parameter has to be changeable <- put it into a config
			String[] row = line.split(start.config.Seperator);
			String[] values = getDefValues(row);
			StringBuilder query = new StringBuilder();
			String value;
			String fieldName;
			for (int i = 0; i < start.config.MasterValuesFields.length; i++) {
				value = row[start.config.Positions[i]];
				if(value != null && !value.equals("")){ // if value isn't null and empty, create a query
					fieldName = start.config.MasterValuesFields[i];
					query.append(createQuery(fieldName, checkValue(value, fieldName), values[0], values[1], (start.config.MV_AS_OF_DATE_NEEDED[i] ? values[2] : null)));
					query.append("\n");	
					qamt++;
				}
			}
			return query;
		} else {
			return new StringBuilder("");
			// save line in backup, this line didn't work properly
		}
	}

	public static String checkValue(String value, String fieldName){
		if(value.contains("'")) value = value.replaceAll("'", "''");
		if(fieldName.equals("WKN") && value.length()>6)value=value.substring(value.length()-6, value.length());
		return value;
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
		ret[0] = line[start.config.IsinPosition];
		ret[1] = line[start.config.MicPosition];
		ret[2] = line[start.config.DatePosition];
		String[] date = ret[2].split("\\.");
		if(date.length != 3){
			System.err.println("Corrupted Date: " + ret[2]);
			ret[2] = null;
		} else {
			ret[2] = date[2] + date[1] + date[0];
		}
		
		return ret;
	}

	public static StringBuilder createQuery(String fieldName, String value, String ISIN, String MIC, String Date) {
		StringBuilder sb = new StringBuilder();
		String Head1 = "Insert vv_mastervalues_upload ( MVU_SOURCE_ID, MVU_ISIN, MVU_MIC,";
		String Head2 = " MVU_FIELDNAME, MVU_STRINGVALUE, MVU_DATA_ORIGIN, MVU_URLSOURCE, MVU_COMMENT )";
		String ASOFDATE = "MVU_AS_OF_DATE,";
		String Values1 = " values ( ";
		String Values2 = "' );";
		sb.append(Head1);
		if (Date != null) {
			sb.append(ASOFDATE);
		}
		sb.append(Head2);
		sb.append(Values1);
		sb.append("'");
		sb.append(start.config.Source_ID);
		sb.append("', '");
		sb.append(ISIN);
		sb.append("', '");
		sb.append(MIC);
		sb.append("', '");
		if (Date != null) {
			sb.append(Date);
			sb.append("', '");
		}
		sb.append(fieldName);
		sb.append("', '");
		sb.append(value);
		sb.append("', '");
		sb.append(start.config.File);
		sb.append("', '");
		sb.append(start.config.URLSource);
		sb.append("', '");
		sb.append(start.config.Comment);
		sb.append(Values2);
		return sb;
	}
}

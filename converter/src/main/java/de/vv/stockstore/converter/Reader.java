package de.vv.stockstore.converter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

	/**
	 * reads File and converts each entry into bunch of queries
	 * 
	 * @param file
	 * @return
	 */
	public static ContainerQuery read(String file) {
		System.out.println("started Reading");
		ContainerQuery query_container = new ContainerQuery(Converter.config.Source_ID, Converter.config.File,
				Converter.config.URLSource, Converter.config.Comment);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int counter = 0;
			while ((line = br.readLine()) != null) {
				if (counter >= 5) {
					ContainerRow qr = workLine(line);
					if (qr != null)
						query_container.add(qr);
				}
				counter++;
			}
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("finished Reading");
		return query_container;
	}

	/**
	 * gets a TableRow and converts all necessary Data into queries
	 * 
	 * @param line
	 * @return
	 */
	public static ContainerRow workLine(String line) {

		String[] columns = line.split(Converter.config.Seperator);
		if (columns.length == 123) { // needs to be parameterized
			// i -ISIN
			// m - MIC
			// d - Date
			String[] imdValues = getDefValues(columns); // fetching values
			ContainerRow row_container = new ContainerRow(imdValues[0], imdValues[1], imdValues[2], columns);

			return row_container;
		} else {
			return null;
		}
	}

	/**
	 * collects MIC, ISIN and Date from current line Position 0 -> ISIN Position
	 * 1 -> MIC Position 2 -> Date
	 * 
	 * @param line
	 *            current row
	 * @return array of value Strings
	 */
	public static String[] getDefValues(String[] line) {
		String[] ret = new String[3];
		ret[0] = line[Converter.config.IsinPosition]; // ISIN
		ret[1] = line[Converter.config.MicPosition]; // MIC
		ret[2] = line[Converter.config.DatePosition]; // AS_OF_DATE
		String[] date = ret[2].split("\\.");
		if (date.length != 3) { // date is supposed to vontains 3 values:
								// yyyy,mm,dd
			System.err.println("Corrupted Date: " + ret[2]);
			ret[2] = null;
		} else { // changing date order
			ret[2] = date[2] + date[1] + date[0];
		}
		return ret;
	}
}

package de.vv.stockstore.converter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reader {
	final static Logger logger = LoggerFactory.getLogger(Reader.class);

	/**
	 * reads File and converts each entry into bunch of queries
	 * 
	 * @param file
	 * @return
	 */
	public static ContainerQuery read(String file, String fileName) {
		logger.info("Converter start.");
		ContainerQuery query_container = new ContainerQuery(Converter.config.Source_ID, Converter.config.File,
				Converter.config.URLSource, Converter.config.Comment);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int counter = 0;
			while ((line = br.readLine()) != null) {
				if (counter >= Converter.config.RowStart) {//>= 5) {
					ContainerRow qr = workLine(line);
					if (qr != null)
						query_container.add(qr);
					if(query_container.rowAmount() >= Converter.config.MaxRowsTillInsert){
						DbConnect.sendQueries(query_container, fileName);
						query_container.resetRows();
					}
				}
				counter++;
			}
			DbConnect.execUpdateProcedure(fileName);
		} catch (IOException e) {
			logger.error("IOException: {}",e.getMessage());
		}
		logger.info("Converter end.");
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
		if (columns.length == Converter.config.RowAmount) { // needs to be parameterized
			// i -ISIN
			// m - MIC
			// d - Date
			String[] imdValues = getDefValues(columns); // fetching values
			ContainerRow row_container = new ContainerRow(imdValues[0], imdValues[1], imdValues[2], columns);

			return row_container;
		} else {
			System.out.println("mismatch: "+columns.length);
			logger.trace(line);
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
		ret[0] = line[Converter.config.IsinPosition].trim(); // ISIN
		ret[1] = line[Converter.config.MicPosition].trim(); // MIC
		if(Converter.config.DatePosition != -1){
			ret[2] = line[Converter.config.DatePosition].trim(); // AS_OF_DATE
			String[] date = ret[2].split("\\.");
			if (date.length != 3) { // date is supposed to vontains 3 values:
									// yyyy,mm,dd
				logger.error("Corrupted Date: {}", ret[2]);
				ret[2] = null;
			} else { // changing date order
				ret[2] = (date[2] + date[1] + date[0]).trim();
			}
		}
		return ret;
	}
}

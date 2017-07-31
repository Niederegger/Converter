package de.vv.stockstore.converter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import de.vv.stockstore.converter.models.*;

public class DbConnect {
	
	//--------------------------------------------------------------------------------------------------------------------
	// Variablen
	//--------------------------------------------------------------------------------------------------------------------
	
	
	final static Logger logger = LoggerFactory.getLogger(DbConnect.class);
	public static Connection con;

	//--------------------------------------------------------------------------------------------------------------------
	// Connection zur Datenbank
	//--------------------------------------------------------------------------------------------------------------------
	
	public static void establishConnection() {
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setIntegratedSecurity(false);
		ds.setUser(App.config.user);																// Datenbank - Username
		ds.setPassword(App.config.pw);															// Datenbank - Password des Users
		ds.setServerName(App.config.serverName);										// Name der Datenbnak
		ds.setPortNumber(App.config.port);													// Port der Datenbank
		ds.setDatabaseName(App.config.dbName);											// Database Name
		try {
			con = ds.getConnection();																	// oeffnen der Verbnidung ueber die Variable con
		} catch (SQLServerException e) {
			logger.error("SQLServerException: {}", e.getMessage());
		}
	}

	public static void closeConnection() {
		try {
			con.close();																							// schliesen der Verbindung ueber die Variable con
		} catch (SQLException e) {
			logger.error("SQLException: {}", e.getMessage());
		}
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	// Kommonikation mit der Datenbank (Queries)
	//--------------------------------------------------------------------------------------------------------------------
	
	public static void insertQueries(ContainerQuery queries, String fileName) {
		logger.info("starting queries");
		
		// querie ohne as of Date
		String insertDefault = "INSERT INTO vv_mastervalues_upload"
				+ "(MVU_DATA_ORIGIN, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_SOURCEFILE, MVU_COMMENT) VALUES"
				+ "(?,?,?,?,?,?,?,?);";
		
		// Query zum insert mit As_of_date
		String insertWithDate = "INSERT INTO vv_mastervalues_upload"
				+ "(MVU_DATA_ORIGIN, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_AS_OF_DATE, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_SOURCEFILE, MVU_COMMENT) VALUES"
				+ "(?,?,?,?,?,?,?,?,?);";

		PreparedStatement preparedStatement = null;
		int stmtCount = 1;
		boolean aod = false;
		try {
			for (ContainerRow qr : queries.rows) {
				for (int i = 0; i < App.config.MasterValuesFields.length; i++) {
					aod = App.config.MV_AS_OF_DATE_NEEDED[i];

					preparedStatement = con.prepareStatement(aod ? insertWithDate : insertDefault);			// hier wird ueberprueft ob ein As-Of_date erwuenscht ist

					preparedStatement.setString(stmtCount++, App.config.DataOrigin); 																// Data Origin
					preparedStatement.setString(stmtCount++, queries.sourceId);													// Source_Id

					if (qr == null) {																																		// falls Der Row-Container null ist (darf an sich nicht passieren)
						logger.error("Invalid ContainerRow {}", qr);																			// wird dieser Eintraf geloggt
						continue;																																					// und die For-Schleife fortgefuehrt
					}
					
					preparedStatement.setString(stmtCount++, qr.ISIN);																	// isin
					preparedStatement.setString(stmtCount++, qr.MIC);																		// mic
					if (aod)																																						// evt As_of_date
						preparedStatement.setString(stmtCount++, qr.AOD);
					preparedStatement.setString(stmtCount++, App.config.MasterValuesFields[i]);					// Fieldname
					preparedStatement.setString(stmtCount++, qr.values[i]);															// StringValue
					preparedStatement.setString(stmtCount++, fileName);																	// SourceFile
					preparedStatement.setString(stmtCount++, queries.comment);													// Comment
					preparedStatement.executeUpdate();																									// execute Insert
					stmtCount = 1;																																			// reset stmtCount zum setzen der naechsten Inserts
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException: {}", e.getMessage());
		}
	}

	public static void execUpdateProcedure(String fileName) {
		try {
			
			PreparedStatement preparedStatement;
			int stmtCount = 1;
			logger.info("starting StoredProcedure");
			preparedStatement = con.prepareStatement("exec vvsp_import_uploadV3 ?, ?, ?, ?, ?;");		// vorbereitung der StoredProcedure
			preparedStatement.setString(stmtCount++, App.config.Source_ID); 												// SourceId
			preparedStatement.setString(stmtCount++, App.config.DataOrigin);  											// Data Origin
			preparedStatement.setString(stmtCount++, App.config.URLSource);  												// UrlSource
			preparedStatement.setString(stmtCount++, fileName);  																		// SourceFile
			preparedStatement.setString(stmtCount++, App.config.Comment);  													// Comment
			preparedStatement.executeUpdate();																											// ausfuehrung der Stored-Procedure

																																															// error-Handling
		} catch (SQLServerException e) {
			logger.error("SQLServerException: {}", e.getMessage());
		} catch (SQLException e) {
			logger.error("SQLException: {}", e.getMessage());
		}

	}

}

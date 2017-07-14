package de.vv.stockstore.converter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class DbConnect {
	final static Logger logger = LoggerFactory.getLogger(DbConnect.class);

	/**
	 * sending all queries to Database
	 * 
	 * @param queries
	 *            data from file
	 */
	public static void sendQueries(ContainerQuery queries, String fileName) {
		// Declare the JDBC objects.
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		SQLServerDataSource ds = new SQLServerDataSource();
		logger.info("Establishing SQL connection.");
		try {
			// Establish the connection.
			con = establishConnection(ds);
			// execute queries
			executeQueries(queries, con, fileName);
			con.close();
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			logger.error("Exception: {}", e.getMessage());
		} finally {
			errorHandling(con, cstmt, rs);
		}
	}

	private static void errorHandling(Connection con, CallableStatement cstmt, ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
				logger.error("Exception: {}", e.getMessage());
			}
		if (cstmt != null)
			try {
				cstmt.close();
			} catch (Exception e) {
				logger.error("Exception: {}", e.getMessage());
			}
		if (con != null)
			try {
				con.close();
			} catch (Exception e) {
				logger.error("Exception: {}", e.getMessage());
			}
	}

	private static void executeQueries(ContainerQuery queries, Connection con, String fileName) throws SQLException {
		logger.info("starting queries");
		// default insert query for preparedStatement
		String insertDefault = "INSERT INTO vv_mastervalues_upload"
				+ "(MVU_DATA_ORIGIN, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_COMMENT) VALUES"
				+ "(?,?,?,?,?,?,?);";
		// date insert query for preparedStatement
		String insertWithDate = "INSERT INTO vv_mastervalues_upload"
				+ "(MVU_DATA_ORIGIN, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_AS_OF_DATE, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_COMMENT) VALUES"
				+ "(?,?,?,?,?,?,?,?);";

		PreparedStatement preparedStatement = null;
		int stmtCount = 1;
		boolean aod = false;
		int count = 0;
		int done = 0;
		for (ContainerRow qr : queries.rows) {
			for (int i = 0; i < Converter.config.MasterValuesFields.length; i++) {
				aod = Converter.config.MV_AS_OF_DATE_NEEDED[i];
				// loads prepared statement depending whether needed as of date
				// or not
				preparedStatement = con.prepareStatement(aod ? insertWithDate : insertDefault);
				preparedStatement.setString(stmtCount++, fileName); // Data Origin
				preparedStatement.setString(stmtCount++, queries.sourceId);
				if (qr == null) {
					logger.error("Invalid ContainerRow {}", qr);
				}
				preparedStatement.setString(stmtCount++, qr.ISIN);
				preparedStatement.setString(stmtCount++, qr.MIC);
				if (aod)
					preparedStatement.setString(stmtCount++, qr.AOD);
				preparedStatement.setString(stmtCount++, Converter.config.MasterValuesFields[i]);
				preparedStatement.setString(stmtCount++, qr.values[i]);
				preparedStatement.setString(stmtCount++, queries.comment);
				preparedStatement.executeUpdate();
				stmtCount = 1;
			}
			if (count++ > 1000) {
				done++;
				logger.info("Queries sent: {}", done * 9000);
				count = 0;
			}
		}
		logger.info("starting StoredProcedure");
		stmtCount = 1;
		logger.info("starting StoredProcedure");
		preparedStatement = con.prepareStatement("exec vvsp_import_uploadV2 ?, ?, ?, ?;");
		preparedStatement.setString(stmtCount++, Converter.config.Source_ID); // SourceId
		preparedStatement.setString(stmtCount++, fileName); // Data Origin
		preparedStatement.setString(stmtCount++, Converter.config.URLSource); // UrlSource
		preparedStatement.setString(stmtCount++, Converter.config.Comment); // Comment
		preparedStatement.executeUpdate();
		logger.info("completed StoredProcedure");
		logger.info("completed StoredProcedure");
	}

	private static Connection establishConnection(SQLServerDataSource ds) throws SQLServerException {
		Connection con;
		ds.setIntegratedSecurity(false);
		ds.setUser(Converter.config.user);
		ds.setPassword(Converter.config.pw);
		ds.setServerName(Converter.config.serverName);
		ds.setPortNumber(Converter.config.port);
		ds.setDatabaseName(Converter.config.dbName);
		con = ds.getConnection();
		logger.info("Connection successeded");
		return con;
	}

}

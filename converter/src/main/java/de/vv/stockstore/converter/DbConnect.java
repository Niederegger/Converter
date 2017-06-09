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

import de.vv.stockstore.converter.esma.registers_mifid_sha.Entry;
import de.vv.stockstore.converter.esma.registers_mifid_sha.EntryList;
import de.vv.stockstore.converter.esma.registers_mifid_sha.EntryLookUp;

public class DbConnect {
	final static Logger logger = LoggerFactory.getLogger(DbConnect.class);

	/**
	 * sending all queries to Database
	 * 
	 * @param entries
	 *            data from file
	 */
	public static void sendQueries(EntryList entries) {
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
			executeQueries(entries, con);
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

	private static void executeQueries(EntryList entries, Connection con) throws SQLException {
		Statement stmt = con.createStatement();
		logger.info("starting queries");
		// default insert query for preparedStatement
		String insertDefault = "INSERT INTO vv_mastervalues_upload"
				+ "(MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_COMMENT) VALUES"
				+ "(?,?,?,?,?,?);";

		PreparedStatement preparedStatement = null;
		int stmtCount = 1;
		int count = 0;
		int done = 0;
		for (Entry entry : entries.response.docs) {
			// _root_
			if (entry == null) {
				logger.error("Invalid Entry {}", entry);
				continue;
			}
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp._root_);
			preparedStatement.setString(stmtCount++, entry._root_);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_modificationDateStr
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_modificationDateStr);
			preparedStatement.setString(stmtCount++, entry.sha_modificationDateStr);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_countryCode
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_countryCode);
			preparedStatement.setString(stmtCount++, entry.sha_countryCode);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_relevantAuthority
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_relevantAuthority);
			preparedStatement.setString(stmtCount++, entry.sha_relevantAuthority);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_exchangeRate
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_exchangeRate);
			preparedStatement.setString(stmtCount++, entry.sha_exchangeRate);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_name
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_name);
			preparedStatement.setString(stmtCount++, entry.sha_name);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_sms
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_sms);
			preparedStatement.setString(stmtCount++, entry.sha_sms);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_status
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_status);
			preparedStatement.setString(stmtCount++, entry.sha_status);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_dailyTransactions
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_dailyTransactions);
			preparedStatement.setString(stmtCount++, entry.sha_dailyTransactions);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// id
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.id);
			preparedStatement.setString(stmtCount++, entry.id);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_type
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_type);
			preparedStatement.setString(stmtCount++, entry.sha_type);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_freeFloatRange
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_freeFloatRange);
			preparedStatement.setString(stmtCount++, entry.sha_freeFloatRange);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_adt
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_adt);
			preparedStatement.setString(stmtCount++, entry.sha_adt);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// sha_avt
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.sha_avt);
			preparedStatement.setString(stmtCount++, entry.sha_avt);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// _version_
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp._version_);
			preparedStatement.setString(stmtCount++, entry._version_);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			// timestamp
			preparedStatement = con.prepareStatement(insertDefault);
			preparedStatement.setString(stmtCount++, Converter.config.Source_ID);// entries.sourceId);
			preparedStatement.setString(stmtCount++, entry.sha_isin);
			preparedStatement.setString(stmtCount++, "");// entry.MIC);
			preparedStatement.setString(stmtCount++, EntryLookUp.timestamp);
			preparedStatement.setString(stmtCount++, entry.timestamp);
			preparedStatement.setString(stmtCount++, Converter.config.Comment);// entries.comment);
			preparedStatement.executeUpdate();
			stmtCount = 1;
			count += 16;
			if (count > 100) {
				done++;
				logger.info("Queries sent: {}", done * 16 * 100);
				count = 0;
			}
		}
		stmtCount = 1;
		logger.info("starting StoredProcedure");
		preparedStatement = con.prepareStatement("exec vvsp_import_uploadV2 ?, ?, ?, ?;");
		preparedStatement.setString(stmtCount++, Converter.config.Source_ID); // SourceId
		preparedStatement.setString(stmtCount++, Converter.config.File); // Data Origin
		preparedStatement.setString(stmtCount++, Converter.config.URLSource); // UrlSource
		preparedStatement.setString(stmtCount++, Converter.config.Comment); // Comment
		preparedStatement.executeUpdate();
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

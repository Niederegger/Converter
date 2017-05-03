package main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class DbConnect {

	/**
	 * sending all queries to Database
	 * 
	 * @param queries
	 *            data from file
	 */
	public static void sendQueries(ContainerQuery queries) {
		// Declare the JDBC objects.
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		SQLServerDataSource ds = new SQLServerDataSource();
		System.out.println("Starting SQL connection");
		try {
			// Establish the connection.
			con = establishConnection(ds);
			// execute queries
			executeQueries(queries, con);
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			errorHandling(con, cstmt, rs);
		}
	}

	private static void errorHandling(Connection con, CallableStatement cstmt, ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
			}
		if (cstmt != null)
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		if (con != null)
			try {
				con.close();
			} catch (Exception e) {
			}
	}

	private static void executeQueries(ContainerQuery queries, Connection con) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println("starting queries");
		// default insert query for preparedStatement
		String insertDefault = "INSERT INTO vv_mastervalues_upload"
				+ "(MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_DATA_ORIGIN, MVU_URLSOURCE, MVU_COMMENT) VALUES"
				+ "(?,?,?,?,?,?,?,?)";
		// date insert query for preparedStatement
		String insertWithDate = "INSERT INTO vv_mastervalues_upload"
				+ "(MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_AS_OF_DATE, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_DATA_ORIGIN, MVU_URLSOURCE, MVU_COMMENT) VALUES"
				+ "(?,?,?,?,?,?,?,?,?)";

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
				preparedStatement.setString(stmtCount++, queries.sourceId);
				if(qr == null){
					System.out.println("qr==null");
					System.out.println(qr);
				}
				if(qr.ISIN == null){
					System.out.println("QR:ISIN==null");
					System.out.println(qr);
				}
				preparedStatement.setString(stmtCount++, qr.ISIN);
				preparedStatement.setString(stmtCount++, qr.MIC);
				if (aod)
					preparedStatement.setString(stmtCount++, qr.AOD);
				preparedStatement.setString(stmtCount++, Converter.config.MasterValuesFields[i]);
				preparedStatement.setString(stmtCount++, qr.values[i]);
				preparedStatement.setString(stmtCount++, queries.dataOrigin);
				preparedStatement.setString(stmtCount++, queries.urlSource);
				preparedStatement.setString(stmtCount++, queries.comment);
				preparedStatement.executeUpdate();
				stmtCount = 1;
			}
			if(count++ > 1000){
				done++;
				System.out.println("Done: " + done*1000);
				count = 0;
			}
			// // execute insert SQL stetement
			// stmt.executeUpdate(q.toString());
			// System.out.println("chunk " +count+++ " finished: " + "");
		}
		System.out.println("starting StoredProcedure");
		stmt.executeUpdate("exec vvsp_import_upload");
		System.out.println("completed");
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
		System.out.println("Connection success:");
		return con;
	}

}

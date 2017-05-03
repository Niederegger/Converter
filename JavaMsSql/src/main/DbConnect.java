package main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class DbConnect {

	/**
	 * sending all queries to Database
	 * @param queries data from file
	 */
	public static void sendQueries(Vector<StringBuilder> queries){
		// Declare the JDBC objects.
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		System.out.println("Starting SQL connection");
		try {
			// Establish the connection.
			SQLServerDataSource ds = new SQLServerDataSource();
			ds.setIntegratedSecurity(false);
			ds.setUser(Converter.config.user);
			ds.setPassword(Converter.config.pw);
			ds.setServerName(Converter.config.serverName);
			ds.setPortNumber(Converter.config.port);
			ds.setDatabaseName(Converter.config.dbName);

			con = ds.getConnection();
			System.out.println("Connection success:");
			Statement stmt = con.createStatement();
			int count = 0;
			System.out.println("starting queries");
			for(StringBuilder q : queries){
				// execute insert SQL stetement
				stmt.executeUpdate(q.toString());
				System.out.println("chunk " +count+++  " finished: " + "");
			}
			System.out.println("starting sp");
			stmt.executeUpdate("exec vvsp_import_upload");
			System.out.println("completed");
		}
		
		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
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
	}
	
}

/* Clase    : WSRecuperarDatos
 * Autor    : Wilmer Reyes Alfaro
 * Revision : 23/10/2014 16:07
 * Funcion  : Clase generica que permite obtener la data proporcionados 
 *            por el Web Services del proveedor de la aplicacion movil.
 *            La clase necesita el nombre de la tabla a donde guardar la
 *            información y el nombre de sus columnas debe corresponder
 *            con los tags del XML de los datos.
 *            Este WebService permite recuperar data desde la respuesta de un WS Kaanbal.
 * */
package com.aje.apm.webservices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WSRecuperarDatos {

	public WSRecuperarDatos() {
	}

	public String execute(String compania, String wsName, String wsParams,
			String tableName, String xmlRoot, String xmlChild) {

		// Get the table structure from database
		String result = "";
		ConnectStringDB aConn = new ConnectStringDB();
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			Connection con = DriverManager.getConnection("jdbc:sqlserver://"
					+ aConn.getDataBaseHost().trim() + ":1433;"
					+ "databaseName=" + aConn.getDataBaseName().trim()
					+ ";user=" + aConn.getDataBaseUser().trim() + ";password="
					+ aConn.getDataBasePassword().trim() + ";");

			String selectQuery = "select top 1 * from " + tableName.trim();
			String insertQuery = "insert into " + tableName.trim() + " (";
			String columns = "";
			String values = "";
			ResultSet rs = con.createStatement().executeQuery(selectQuery);

			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			List<String> listaCols = new ArrayList<String>();

			for (int i = 1; i <= colCount; i++) {
				String columnName = rsmd.getColumnName(i);
				listaCols.add(columnName);
				columns = columns.trim() + columnName + ",";
				values = values.trim() + "?,";
			}
			columns = columns.substring(0, columns.length() - 1);
			values = values.substring(0, values.length() - 1);
			insertQuery = insertQuery + columns + ") values(" + values + ")";

			// System.out.println("insertQuery: " + insertQuery);

			// Read the XML response and send data to List<Map<>>
			WebServiceListMap xmlResponse = new WebServiceListMap(compania,
					wsName, wsParams, listaCols, xmlRoot, xmlChild);

			// Read the List<Map<>> and insert records into database
			PreparedStatement ps = con.prepareStatement(insertQuery);
			final int batchSize = 1000;
			int count = 0;

			for (Map<String, String> reg : xmlResponse.getListTable()) {
				int i = 0;
				for (String col : listaCols) {
					// System.out.println(col + " : " + reg.get(col));
					ps.setString(i + 1, reg.get(col));
					i = i + 1;
				}
				ps.addBatch();

				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
			}
			ps.executeBatch();
			result = ps.getUpdateCount() + " row(s) affected.";
			ps.close();
			con.close();

		} catch (ClassNotFoundException e) {
			result = "0 row(s) affected.";
			System.out.println("Class Error: " + e);
		} catch (SQLException e) {
			result = "0 row(s) affected.";
			System.out.println("SQL Error: " + e);
		}
		return result;
	}

}

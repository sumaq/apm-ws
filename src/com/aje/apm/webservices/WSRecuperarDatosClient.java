package com.aje.apm.webservices;

/* Adaptacion de clase cliente para usar la clase servidor de forma local en el proyecto.
 * */

public class WSRecuperarDatosClient {

	public static void main(String[] args) {
		try {
			String compania = args[0];
			String tableName = args[1];
			String wsName = args[2];
			String wsParams = args[3];
			String xmlRoot = args[4];
			String xmlChild = args[5];

			// Replace params char separator
			wsParams = wsParams.replace("#", "&");

			WSRecuperarDatos stub = new WSRecuperarDatos();
			String response = "";

			response = stub.execute(compania, tableName, wsName, wsParams,
					xmlRoot, xmlChild);

			System.out.println(response);

		} catch (Exception e) {
			System.out.println("General Error: " + e);
			e.printStackTrace();
		}

	}

}

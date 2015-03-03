package com.aje.apm.webservices;

/* Adaptacion de clase cliente para usar la clase servidor de forma local en el proyecto.
 * */

public class WSCargarDatosClient {

	public static void main(String[] args) {
		try {
			String compania = args[0];
			String spName = args[1];
			String wsName = args[2];
			String wsNameParamList = args[3];
			String xmlRoot = args[4];
			String xmlElement = args[5];

			WSCargarDatos stub = new WSCargarDatos();
			String response = "";

			response = stub.execute(compania, spName, wsName, wsNameParamList,
					xmlRoot, xmlElement);

			System.out.println(response);

		} catch (Exception e) {
			System.out.println("General Error: " + e.getMessage());
			e.printStackTrace();
		}

	}

}

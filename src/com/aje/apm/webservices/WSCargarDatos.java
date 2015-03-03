/* Clase    : WSCargarData
 * Autor    : Wilmer Reyes Alfaro
 * Revision : 22/10/2014 16:50
 * Funcion  : Permite gestionar el envio de catalogos hacia 
 *            un Web Services del proveedor de la aplicacion movil.
 *            Este WebService permite enviar datos hacia un WS Kaanbal de tipo Carga.
 * */
package com.aje.apm.webservices;

public class WSCargarDatos {
	private String compania;
	private String spName;
	private String wsName;
	private String wsNameParamList;
	private String xmlRoot;
	private String xmlElement;

	public WSCargarDatos() {
	}

	public String execute(String compania, String spName, String wsName,
			String wsNameParamList, String xmlElement, String xmlRoot) {
		this.compania = compania;
		this.spName = spName;
		this.wsName = wsName;
		this.wsNameParamList = wsNameParamList;
		this.xmlElement = xmlElement;
		this.xmlRoot = xmlRoot;

		String urlXML = new String();
		StringBuilder sqlStatement = new StringBuilder();
		String resultWS = new String();
		// System.out.println("execute: " + compania);

		QueryXML qx = new QueryXML();
		qx.setRootXML(this.xmlRoot);
		qx.setElementXML(this.xmlElement);

		sqlStatement.append("EXEC " + this.spName.trim());
		qx.setQueryXML(sqlStatement.toString());
		urlXML = qx.execute();
		// System.out.println(urlXML);

		Instancia instancia = new Instancia(compania.trim());

		WebService ws = new WebService();

		ws.setUrlAddress(instancia.getWsUrl() + "/" + this.wsName.trim());
		ws.setUrlQuery("Usuario=" + instancia.getWsUser() + "&Password="
				+ instancia.getWsPassword() + "&" + this.wsNameParamList.trim()
				+ "=");
		ws.setUrlXML(urlXML);

		// System.out.println(ws.getUrlAddress() + "?" + ws.getUrlQuery());
		// + ws.getUrlXML());

		resultWS = ws.sendWebService();

		// System.out.println(ws.getUrlAddress().trim() + "?"
		// + ws.getUrlQuery().trim() + resultWS.trim());

		return ws.getUrlAddress().trim() + "?" + ws.getUrlQuery().trim()
				+ ws.getUrlXML().trim() + "\n" + resultWS;
	}
}

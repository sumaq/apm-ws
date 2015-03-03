/* Clase    : WebService
 * Autor    : Wilmer Reyes Alfaro
 * Revision : 22/06/2013 12:45
 * Funcion  : Define una URL de Web Service y lo consume enviando entre
 *            sus parametros contenido XML.
 * */
package com.aje.apm.webservices;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WebService {

	private String urlAddress;
	private String urlQuery;
	private String urlXML;

	public String getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public String getUrlQuery() {
		return urlQuery;
	}

	public void setUrlQuery(String urlQuery) {
		this.urlQuery = urlQuery;
	}

	public String getUrlXML() {
		return urlXML;
	}

	public void setUrlXML(String urlXML) {
		this.urlXML = urlXML;
	}

	public String sendWebService() {
		StringBuilder responseBuilder = new StringBuilder();
		String result = new String();
		try {
			// Create a URLConnection object for a URL
			StringBuilder fullURL = new StringBuilder();

			// fullURL.append(urlAddress);
			fullURL.append(urlQuery);
			fullURL.append(URLEncoder.encode(urlXML, "UTF-8"));

			result = this.executePost(urlAddress, fullURL.toString());
			/*
			 * // System.out.println(fullURL.toString()); URL url = new
			 * URL(fullURL.toString()); URLConnection conn =
			 * url.openConnection();
			 * 
			 * // System.out.println(conn.getURL().getQuery());
			 * HttpURLConnection httpConn; httpConn = (HttpURLConnection) conn;
			 * httpConn.setRequestMethod("POST");
			 * 
			 * BufferedReader rd = new BufferedReader(new
			 * InputStreamReader(httpConn.getInputStream())); String line;
			 * 
			 * while ((line = rd.readLine()) != null) {
			 * responseBuilder.append(line + '\n'); // System.out.println(line);
			 * }
			 */
		} catch (Exception e) {
			System.out.println("Webservice Error: " + this.urlAddress + "?"
					+ this.urlQuery + " | " + e);
		}
		return result; // responseBuilder.toString();

	}

	public static String executePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String response;
		// WebService ws = new WebService();
		// ws.setUrlAddress("http://monterrey.kaanbal.com/monitoreoservicios/monitor/sdk/index.php/verPedidos");
		// ws.setUrlAddress("http://www.staging.kaanbal.com/monitoreoservicios/monitor/sdk/index.php/verPedidos");
		// ws.setUrlQuery("Usuario=admin&Password=admin&CompaniaID=0007&Fecha=2013-07-13&NroRegsRetornar=1000");
		// ws.setUrlQuery("Usuario=admin&Password=admin&CompaniaID=0007&NroRegsRetornar=5");
		// ws.setUrlXML("");
		// response = ws.sendWebService();
		// System.out.println(response);
	}
}

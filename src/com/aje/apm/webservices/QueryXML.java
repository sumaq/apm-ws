/* Clase    : QueryXML
 * Autor    : Wilmer Reyes Alfaro
 * Revision : 22/06/2013 12:45
 * Funcion  : Permite gestionar la extraccion de informacion a traves de SQL
 *            y posteriormente generar el resultado en formato XML.
 * */
package com.aje.apm.webservices;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class QueryXML {

	private String rootXML;
	private String elementXML;
	private String queryXML;
	private StringBuffer sbKeyParent = new StringBuffer();

	public String getSbKeyParent() {
		return sbKeyParent.toString();
	}

	public void setSbKeyParent(String sbKeyParent) {
		this.sbKeyParent.append(sbKeyParent);
		this.sbKeyParent.append(",");
	}

	public String getRootXML() {
		return rootXML;
	}

	public void setRootXML(String rootXML) {
		this.rootXML = rootXML;
	}

	public String getElementXML() {
		return elementXML;
	}

	public void setElementXML(String elementXML) {
		this.elementXML = elementXML;
	}

	public String getQueryXML() {
		return queryXML;
	}

	public void setQueryXML(String queryXML) {
		this.queryXML = queryXML;
	}

	public String execute() {
		String result = new String();
		ConnectStringDB aConn = new ConnectStringDB();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element results = doc.createElement(rootXML);
			doc.appendChild(results);

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection con = DriverManager.getConnection("jdbc:sqlserver://"
					+ aConn.getDataBaseHost().trim() + ":1433;"
					+ "databaseName=" + aConn.getDataBaseName().trim()
					+ ";user=" + aConn.getDataBaseUser().trim() + ";password="
					+ aConn.getDataBasePassword().trim() + ";");

			ResultSet rs = con.createStatement().executeQuery(queryXML);

			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();

			while (rs.next()) {
				Element row = doc.createElement(elementXML);
				results.appendChild(row);
				for (int i = 1; i <= colCount; i++) {
					String columnName = rsmd.getColumnName(i);
					Object value = rs.getObject(i);
					// Element node = doc.createElement(columnName);
					Attr nom = doc.createAttribute(columnName);
					nom.setValue(value.toString());
					row.setAttributeNode(nom);
					// node.appendChild(doc.createTextNode(value.toString()));
					// row.appendChild(node);
				}
			}
			DOMSource domSource = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);

			// System.out.println(sw.toString());
			result = sw.toString();
			con.close();
			rs.close();
			// return result;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return result;
	}

	public String getXMLFromDatabase() {
		String result = new String();
		ConnectStringDB aConn = new ConnectStringDB();
		try {
			StringBuffer sb = new StringBuffer();

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection con = DriverManager.getConnection("jdbc:sqlserver://"
					+ aConn.getDataBaseHost().trim() + ":1433;"
					+ "databaseName=" + aConn.getDataBaseName().trim()
					+ ";user=" + aConn.getDataBaseUser().trim() + ";password="
					+ aConn.getDataBasePassword().trim() + ";");

			ResultSet rs = con.createStatement().executeQuery(queryXML);

			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();

			while (rs.next()) {
				for (int i = 1; i <= colCount; i++) {
					Object value = rs.getObject(i);
					sb.append(value.toString());
				}
			}
			result = sb.toString();
			con.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	public String getNestedXML() {
		String result = new String();
		ConnectStringDB aConn = new ConnectStringDB();
		try {
			StringBuffer sb = new StringBuffer();
			StringBuffer sbAnt = new StringBuffer();

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element results = doc.createElement(rootXML);
			doc.appendChild(results);

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection con = DriverManager.getConnection("jdbc:sqlserver://"
					+ aConn.getDataBaseHost().trim() + ":1433;"
					+ "databaseName=" + aConn.getDataBaseName().trim()
					+ ";user=" + aConn.getDataBaseUser().trim() + ";password="
					+ aConn.getDataBasePassword().trim() + ";");

			ResultSet rs = con.createStatement().executeQuery(queryXML);

			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();

			// Navegar por cada registro extraido del resulset
			while (rs.next()) {
				sb = new StringBuffer();

				// Construir la llave primaria para distinguir encabezado
				for (int i = 1; i <= colCount; i++) {
					String column = rsmd.getColumnName(i);
					Object value = rs.getObject(i);
					// leer la lista de columnas que forman la PK
					if (sbKeyParent.toString().contains(column)) {
						sb.append(value.toString());
					}
				}
				// Crear nodo para el encabezado
				Element row = doc.createElement(elementXML);

				// Si pk es diferente se trata de un nuevo encabezado, quebrar
				if (!(sb.toString().equalsIgnoreCase(sbAnt.toString()))) {

					// results.appendChild(row);

					// Crear un nodo para detalle
					Element row2 = doc.createElement("DET");

					// System.out.println("cab:" + sb.toString());

					// Agregar atributos al nodo detalle que no pertezcan al
					// encabezado
					for (int j = 1; j <= colCount; j++) {
						String column = rsmd.getColumnName(j);
						Object val = rs.getObject(j);

						// Extraer detalle diferenciado columnas que no se
						// incluyen en la keyParent
						if (!(sbKeyParent.toString().contains(column))) {
							// System.out.println(column + " : " +
							// val.toString());
							Attr nom = doc.createAttribute(column);
							nom.setValue(val.toString());
							row2.setAttributeNode(nom);

						} else {
							// Agregar atributos del encabezado
							Attr nom = doc.createAttribute(column);
							nom.setValue(val.toString());
							row.setAttributeNode(nom);
						}
					}
					// Añadir nodo detalle al nodo encabezado
					row.appendChild(row2);
				} else {
					Element row3 = doc.createElement("DET");

					for (int j = 1; j <= colCount; j++) {
						String column = rsmd.getColumnName(j);
						// Extaer detalle diferenciado columnas que no se
						// incluyen en la keyParent
						if (!(sbKeyParent.toString().contains(column))) {
							Object val = rs.getObject(j);

							Attr nomAt = doc.createAttribute(column);
							nomAt.setValue(val.toString());
							row3.setAttributeNode(nomAt);

							System.out.println(column + " : " + val.toString());
						}
					}
					row.appendChild(row3);
				}
				results.appendChild(row);
				sbAnt = sb;

			}

			DOMSource domSource = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);

			result = sw.toString();
			con.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	public static void main(String args[]) {
		// QueryXML qx = new QueryXML();
		// qx.setRootXML("COMPANIA");
		// qx.setElementXML("COMPANIA");
		// qx.setQueryXML("select compania id, rtrim(ltrim(nombre)) nombre from mcompa1f");
		// qx.execute();
	}
}

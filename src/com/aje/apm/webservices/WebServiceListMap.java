/* Clase    : WebServiceListMap
 * Autor    : Wilmer Reyes Alfaro
 * Revision : 24/10/2014 12:46
 * Funcion  : Clase generica que permite analizar un contenido XML 
 *            y la deriva hacia una estructura de lista como tabla
 *            y una estructura Map para los registros. Considera XML
 *            anidados hasta un segundo nivel.
 * */
package com.aje.apm.webservices;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WebServiceListMap extends DefaultHandler {
	private String tmpValue;
	private String compania;
	private String wsName;
	private String wsParams;
	private List<String> listaCols;
	private String xmlRoot;
	private String xmlChild;
	private List<Map<String, String>> listTable = new ArrayList<Map<String, String>>();
	private HashMap<String, String> reg;
	private HashMap<String, String> reg_padre;
	private HashMap<String, String> cols_padre;
	private HashMap<String, String> cols_child;

	public WebServiceListMap(String compania, String wsName, String wsParams,
			List<String> listaCols, String xmlRoot, String xmlChild) {
		this.compania = compania;
		this.wsName = wsName;
		this.wsParams = wsParams;
		this.listaCols = listaCols;
		this.xmlRoot = xmlRoot;
		this.xmlChild = xmlChild;

		if (this.wsParams.length() > 0) {
			this.wsParams = "&" + this.wsParams.trim();
		}
		Instancia instancia = new Instancia(compania.trim());
		WebService ws = new WebService();
		ws.setUrlAddress(instancia.getWsUrl() + "/" + this.wsName.trim());
		ws.setUrlQuery("Usuario=" + instancia.getWsUser().trim() + "&Password="
				+ instancia.getWsPassword().trim() + this.wsParams);

		ws.setUrlXML("");
		// System.out.println(ws.getUrlAddress() + "?" + ws.getUrlQuery());
		String response = ws.sendWebService();
		// System.out.println(response);
		parseDocument(response);
	}

	public void parseDocument(String xmlContent) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(new InputSource(new StringReader(xmlContent)), this);
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfig error");
		} catch (SAXException e) {
			System.out.println("SAXException : xml not well formed");
		} catch (IOException e) {
			System.out.println("IO error");
		}
	}

	@Override
	public void startElement(String s, String s1, String elementName,
			Attributes attributes) throws SAXException {

		if (elementName.equalsIgnoreCase(this.xmlRoot)) {
			reg_padre = new HashMap<String, String>();
			cols_padre = new HashMap<String, String>();
			cols_child = new HashMap<String, String>();

			for (String columnName : this.listaCols) {
				String value = attributes.getValue(columnName);
				if (value == null) {
					value = "";
				} else {
					cols_padre.put(columnName, columnName);
				}
				reg_padre.put(columnName, value);
			}
		}
		// Create details
		if (elementName.equalsIgnoreCase(this.xmlChild)) {
			reg = new HashMap<String, String>();

			for (String columnName : this.listaCols) {
				String value = attributes.getValue(columnName);
				if (value == null) {
					value = reg_padre.get(columnName);
				}
				// Validate do not overwrite fields with the same name on father
				if (cols_padre.get(columnName) != null) {
					reg.put(columnName, reg_padre.get(columnName));
				} else {
					cols_child.put(columnName, columnName);
					reg.put(columnName, value);
				}
			}
		}
		// Create record in case do not exist child
		if (elementName.equalsIgnoreCase(this.xmlRoot)) {
			reg = new HashMap<String, String>();

			for (String columnName : this.listaCols) {
				String value = reg_padre.get(columnName);
				if (value == null) {
					value = "";
				}
				reg.put(columnName, value);
			}
		}

	}

	@Override
	public void endElement(String s, String s1, String element)
			throws SAXException {
		if ((element.equals(this.xmlChild) && !cols_child.isEmpty())
				|| (element.equals(this.xmlRoot)) && cols_child.isEmpty()) {
			listTable.add(reg);
		}
	}

	@Override
	public void characters(char[] ac, int i, int j) throws SAXException {
		tmpValue = new String(ac, i, j);
	}

	public List<Map<String, String>> getListTable() {
		return listTable;
	}

	public void setListTable(List<Map<String, String>> listTable) {
		this.listTable = listTable;
	}

}

/* Clase    : WSRecuperarEncuestaRespuestas
 * Autor    : Wilmer Reyes Alfaro
 * Revision : 22/06/2013 12:45
 * Funcion  : Clase especializada que permite obtener los pedidos proporcionados 
 *            por el Web Services del proveedor de la aplicacion movil.
 * */
package com.aje.apm.webservices;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.aje.apm.common.ImageUtils;
import com.aje.apm.schema.EncuestaRespuesta;

public class WSRecuperarEncuestaRespuestas extends DefaultHandler {
  static List<EncuestaRespuesta> respuestaL;
  String respuestaXmlFileName;
  String tmpValue;
  EncuestaRespuesta encuestaRespuesta;
  EncuestaRespuesta respuestaTmp;
  EncuestaRespuesta encuesta;
  SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");

  public WSRecuperarEncuestaRespuestas(String respuestaXmlFileName) {
    this.respuestaXmlFileName = respuestaXmlFileName;
    respuestaL = new ArrayList<EncuestaRespuesta>();
    parseDocument();
    printDatas();
  }

  private void parseDocument() {
    // parse
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      SAXParser parser = factory.newSAXParser();
      parser.parse(new InputSource(new StringReader(respuestaXmlFileName)), this);
    } catch (ParserConfigurationException e) {
      System.out.println("ParserConfig error");
    } catch (SAXException e) {
      System.out.println("SAXException : xml not well formed");
    } catch (IOException e) {
      System.out.println("IO error");
    }
  }

  private void printDatas() {
    for (EncuestaRespuesta tmpB : respuestaL) {
      // System.out.println(tmpB.toString());
    }
  }

  @Override
  public void startElement(String s, String s1, String elementName, Attributes attributes)
      throws SAXException {
    // if current element is book , create new book
    // clear tmpValue on start of element

    // if current element is EncuestaRespuesta
    if (elementName.equalsIgnoreCase("encuesta")) {
      respuestaTmp = new EncuestaRespuesta();
      respuestaTmp.setId(attributes.getValue("id"));
      respuestaTmp.setIdEncuesta(attributes.getValue("id_encuesta"));
      respuestaTmp.setIdCliente(attributes.getValue("cliente"));
      respuestaTmp.setMotivoNoEncuesta(attributes.getValue("motivo_no_encuesta"));
    }

    if (elementName.equalsIgnoreCase("pregunta")) {
      encuestaRespuesta = new EncuestaRespuesta();
      encuestaRespuesta.setId(respuestaTmp.getId());
      encuestaRespuesta.setIdEncuesta(respuestaTmp.getIdEncuesta());
      encuestaRespuesta.setIdCliente(respuestaTmp.getIdCliente());
      encuestaRespuesta.setMotivoNoEncuesta(respuestaTmp.getMotivoNoEncuesta());
       
      encuestaRespuesta.setIdPregunta(attributes.getValue("id_pregunta"));
      encuestaRespuesta.setPregunta(attributes.getValue("pregunta"));
      encuestaRespuesta.setTipo(attributes.getValue("tipo"));
    }

    if(elementName.equalsIgnoreCase("respuesta")){
    	encuesta = new EncuestaRespuesta();
        encuesta.setId(encuestaRespuesta.getId());
        encuesta.setIdEncuesta(encuestaRespuesta.getIdEncuesta());
        encuesta.setIdCliente(encuestaRespuesta.getIdCliente());
        encuesta.setMotivoNoEncuesta(encuestaRespuesta.getMotivoNoEncuesta());
        encuesta.setIdPregunta(encuestaRespuesta.getIdPregunta());
        encuesta.setPregunta(encuestaRespuesta.getPregunta());
        encuesta.setTipo(encuestaRespuesta.getTipo());
        
        encuesta.setIdEncuestaRespuesta(attributes.getValue("id_enc_respuesta"));
        if (attributes.getValue("respuesta").length() > 1000){
        	encuesta.setValorImagen(attributes.getValue("respuesta"));
        	encuesta.setValor("");
        }else{
        	encuesta.setValorImagen("");
        	encuesta.setValor(attributes.getValue("respuesta"));
        }
        
    }
  }

  @Override
  public void endElement(String s, String s1, String element) throws SAXException {
    // if end of book element add to list
    if (element.equals("respuesta")) {
      respuestaL.add(encuesta);
    }
  }

  @Override
  public void characters(char[] ac, int i, int j) throws SAXException {
    tmpValue = new String(ac, i, j);
  }

  public static void main(String[] args) {
    ConnectStringDB aConn = new ConnectStringDB();
    try {
      String urlXML;
      String pCompania = new String(args[0]);
      String pSucursal = new String(args[1]);
      String pFecha = new String(args[2]);
      String pVendedorID = new String(args[3]);
      
      Instancia instancia = new Instancia(pCompania.trim());

      WebService ws = new WebService();
      ws.setUrlAddress(instancia.getWsUrl() + "/verEncuestas");
      ws.setUrlQuery("Usuario=admin&Password=admin&CompaniaID=" + pCompania.trim() + "&SucursalID="
          + pSucursal.trim() + "&Fecha=" + pFecha.trim() + "&VendedorID=" + pVendedorID.trim());

      ws.setUrlXML("");

      System.out.println(ws.getUrlAddress() + "?" + ws.getUrlQuery());

      urlXML = ws.sendWebService();

      System.out.println(urlXML);

      new WSRecuperarEncuestaRespuestas(urlXML);

      // Insertar Lista en base de datos
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      Connection con = DriverManager.getConnection("jdbc:sqlserver://"
          + aConn.getDataBaseHost().trim() + ":1433;" + "databaseName="
          + aConn.getDataBaseName().trim() + ";user=" + aConn.getDataBaseUser().trim()
          + ";password=" + aConn.getDataBasePassword().trim() + ";");

      StringBuffer sbSQL = new StringBuffer();
      sbSQL.append("insert into tmpAPMEncuestaRespuesta (id, idEncuesta, idCliente, motivoNoEncuesta, idPregunta, pregunta, tipo, idEncuestaRespuesta, valor, valorImagen)");
      sbSQL.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

      PreparedStatement ps = con.prepareStatement(sbSQL.toString());

      final int batchSize = 1000;
      int count = 0;

      for (EncuestaRespuesta _respuesta : respuestaL) {
  	  
        ps.setString(1, _respuesta.getId());
        ps.setString(2, _respuesta.getIdEncuesta());
        ps.setString(3, _respuesta.getIdCliente());
        ps.setString(4, _respuesta.getMotivoNoEncuesta());
        ps.setString(5, _respuesta.getIdPregunta());
        ps.setString(6, _respuesta.getPregunta());
        ps.setString(7, _respuesta.getTipo());
        ps.setString(8, _respuesta.getIdEncuestaRespuesta());
        ps.setString(9, _respuesta.getValor());
        
        //Pasar la cadena (base64) al buffer para luego fijarlo en el stmt
        InputStream inputImage = new ByteArrayInputStream(_respuesta.getValorImagen().getBytes());
        ps.setBinaryStream(10, inputImage, (int)(_respuesta.getValorImagen().length()));
        
        //Convertir el contenido string (base64) hacia un archivo de imagen
        ImageUtils.decodeToImage(_respuesta.getValorImagen(),"C:\\" + _respuesta.getIdEncuestaRespuesta() + ".jpg");
       
        ps.addBatch();

        if (++count % batchSize == 0) {
          ps.executeBatch();
        }
      }

      ps.executeBatch(); // insert remaining records
      ps.close();
      con.close();

    } catch (Exception e) {
      System.out.println(e);
    }
  }

}

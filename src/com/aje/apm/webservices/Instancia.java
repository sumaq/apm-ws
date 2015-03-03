/* Clase    : Instancia
 * Autor    : Wilmer Reyes Alfaro
 * Revision : 22/06/2013 12:45
 * Funcion  : Permite obtener los datos de la instancia a partir de la compania y sucursal.
 * */
package com.aje.apm.webservices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Instancia {
  private String wsUrl;
  private String wsUser;
  private String wsPassword;
  private String dbConnectString;

  public Instancia(String pCompania) {
    ConnectStringDB aConn = new ConnectStringDB();
    try {

      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      Connection con = DriverManager.getConnection("jdbc:sqlserver://"
          + aConn.getDataBaseHost().trim() + ":1433;" + "databaseName="
          + aConn.getDataBaseName().trim() + ";user=" + aConn.getDataBaseUser().trim()
          + ";password=" + aConn.getDataBasePassword().trim() + ";");

      //pCompania = "0006"; // COMENTAR LUEGO, SOLO ES PRUEBA PARA ENVIAR FIJO A STAGING

      ResultSet rs = con.createStatement().executeQuery(
          "EXEC USP_APM_SQL_CIA_INSTANCIA '" + pCompania + "'");

      ResultSetMetaData rsmd = rs.getMetaData();
      int colCount = rsmd.getColumnCount();

      while (rs.next()) {
        for (int i = 1; i <= colCount; i++) {
          String columnName = rsmd.getColumnName(i);
          Object value = rs.getObject(i);
          if (columnName.equalsIgnoreCase("WS_URL")) {
            this.wsUrl = value.toString();
          }
          if (columnName.equalsIgnoreCase("WS_USER")) {
            this.wsUser = value.toString();
          }
          if (columnName.equalsIgnoreCase("WS_PASSWORD")) {
            this.wsPassword = value.toString();
          }
          if (columnName.equalsIgnoreCase("MGDB_CONNECT_STRING")) {
            this.dbConnectString = value.toString();
          }
        }
      }

      con.close();
      rs.close();
      // return result;
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  public String getWsUrl() {
    return wsUrl;
  }

  public void setWsUrl(String wsUrl) {
    this.wsUrl = wsUrl;
  }

  public String getWsUser() {
    return wsUser;
  }

  public void setWsUser(String wsUser) {
    this.wsUser = wsUser;
  }

  public String getWsPassword() {
    return wsPassword;
  }

  public void setWsPassword(String wsPassword) {
    this.wsPassword = wsPassword;
  }

  public String getDbConnectString() {
    return dbConnectString;
  }

  public void setDbConnectString(String dbConnectString) {
    this.dbConnectString = dbConnectString;
  }

  public static void main(String args[]) {
    // Instancia instancia = new Instancia();

  }
}

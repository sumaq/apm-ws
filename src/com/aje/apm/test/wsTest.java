package com.aje.apm.test;

import com.aje.apm.webservices.WSCargarDatos;
import com.aje.apm.webservices.WSRecuperarDatos;
import com.aje.apm.webservices.WSRecuperarEncuestaRespuestas;

public class wsTest {
	public static void main(String[] args) {
		String[] paramsArgs = { "0004", "0002", "2014-10-23", "1285797" };
		WSRecuperarEncuestaRespuestas wsRER = new WSRecuperarEncuestaRespuestas(
				"");
		// wsRER.main(paramsArgs);

		WSCargarDatos wsCargar = new WSCargarDatos();
		// wsCargar.execute("0005", "USP_APM_SQL_COMPANIA '0003'",
		// "cargarCompania", "ListaCompania", "COMPANIA", "COMPANIA");

		WSRecuperarDatos wsRecuperar = new WSRecuperarDatos();
		// wsRecuperar.execute(
		// "0004",
		// "verPedidos",
		// "CompaniaID=0004&SucursalID=0002&Fecha=2014-10-23&VendedorID=1285797",
		// "tmpAPMPedido2", "pedido", "detalle");

		wsRecuperar.execute("0004", "verTurnosCerrados",
				"companiaID=0004&fecha=2014-10-23", "tmpAPMTurno2", "turnos",
				"");
	}
}

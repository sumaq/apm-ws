package com.aje.apm.test;

import com.aje.apm.webservices.WSCargarDatosClient;
import com.aje.apm.webservices.WSRecuperarDatosClient;

public class wsTestClient {
	public static void main(String[] args) {

		WSCargarDatosClient wsCargar = new WSCargarDatosClient();
		String[] paramsArgs = { "0004", "USP_APM_SQL_COMPANIA '0004'",
				"cargarCompania", "ListaCompania", "COMPANIA", "COMPANIA" };
		wsCargar.main(paramsArgs);

		WSRecuperarDatosClient wsRecuperar = new WSRecuperarDatosClient();
		String[] paramsArgs2 = { "0004", "verTurnosCerrados",
				"companiaID=0004&fecha=2015-02-11", "tmpAPMTurno2", "turnos",
				"" };
		// wsRecuperar.execute(
		// "0004",
		// "verPedidos",
		// "CompaniaID=0004&SucursalID=0002&Fecha=2014-10-23&VendedorID=1285797",
		// "tmpAPMPedido2", "pedido", "detalle");

		wsRecuperar.main(paramsArgs2);
	}
}

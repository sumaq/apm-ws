package com.aje.apm.schema;

public class EncuestaRespuesta {
  String id;
  String idEncuesta;
  String idCliente;
  String motivoNoEncuesta;
  String idPregunta;
  String pregunta;
  String tipo;
  String idEncuestaRespuesta;
  String valorImagen;
  String valor;
  
public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}


public String getIdEncuesta() {
	return idEncuesta;
}


public void setIdEncuesta(String idEncuesta) {
	this.idEncuesta = idEncuesta;
}


public String getIdCliente() {
	return idCliente;
}


public void setIdCliente(String idCliente) {
	this.idCliente = idCliente;
}


public String getMotivoNoEncuesta() {
	return motivoNoEncuesta;
}


public void setMotivoNoEncuesta(String motivoNoEncuesta) {
	this.motivoNoEncuesta = motivoNoEncuesta;
}


public String getIdPregunta() {
	return idPregunta;
}


public void setIdPregunta(String idPregunta) {
	this.idPregunta = idPregunta;
}


public String getPregunta() {
	return pregunta;
}


public void setPregunta(String pregunta) {
	this.pregunta = pregunta;
}


public String getTipo() {
	return tipo;
}


public void setTipo(String tipo) {
	this.tipo = tipo;
}


public String getIdEncuestaRespuesta() {
	return idEncuestaRespuesta;
}


public void setIdEncuestaRespuesta(String idEncuestaRespuesta) {
	this.idEncuestaRespuesta = idEncuestaRespuesta;
}

public String getValorImagen() {
	return valorImagen;
}

public void setValorImagen(String valorImagen) {
	this.valorImagen = valorImagen;
}

public String getValor() {
	return valor;
}


public void setValor(String valor) {
	this.valor = valor;
}


public String toString() {
    return this.id + "-" + this.idEncuesta;
  }

}

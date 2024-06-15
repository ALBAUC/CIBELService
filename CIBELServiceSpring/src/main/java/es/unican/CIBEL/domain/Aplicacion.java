package es.unican.CIBEL.domain;

import javax.persistence.Entity;

@Entity
public class Aplicacion extends Activo {
	
	public Aplicacion() {
		
	}
	
	public Aplicacion(String nombre, String icono, Tipo tipo) {
		super(nombre, icono, tipo);
	}

}

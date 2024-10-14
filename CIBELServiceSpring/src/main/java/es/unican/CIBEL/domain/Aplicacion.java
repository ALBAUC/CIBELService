package es.unican.CIBEL.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;

@Schema(description = "Represents an Android application, which is a type of asset in the system.")
@Entity
public class Aplicacion extends Activo {
	
	public Aplicacion() {
		
	}
	
	public Aplicacion(String nombre, String icono, Tipo tipo) {
		super(nombre, icono, tipo);
	}

}

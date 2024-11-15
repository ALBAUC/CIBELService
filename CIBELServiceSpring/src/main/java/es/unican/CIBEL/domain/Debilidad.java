package es.unican.CIBEL.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Schema(description = "Represents a Common Weakness Enumeration (CWE), providing information about software security weaknesses.")
public class Debilidad {
	
	@Schema(description = "Unique identifier for the Common Weakness Enumeration (CWE). For example, 'CWE-89' for SQL Injection.")
	@Id
	private String idCWE;
	
	@Schema(description = "Short name or title of the weakness, providing a concise description of its nature in spanish. For example, 'SQL Injection'.")
	private String nombre;
	
	@Schema(description = "Detailed description of the weakness, explaining its characteristics, potential risks, and impacts in spanish.")
	@Column(length = 5000)
	private String descripcion;
	
	@Schema(description = "Short name or title of the weakness, providing a concise description of its nature in english. For example, 'SQL Injection'.")
	private String nombre_en;
	
	@Schema(description = "Detailed description of the weakness, explaining its characteristics, potential risks, and impacts in english.")
	@Column(length = 5000)
	private String descripcion_en;

	public Debilidad(String idCWE, String nombre, String descripcion) {
		this.idCWE = idCWE;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	public Debilidad() {
		// Constructor vacío
	}

	public String getIdCWE() {
		return idCWE;
	}

	public void setIdCWE(String idCWE) {
		this.idCWE = idCWE;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre_en() {
		return nombre_en;
	}

	public void setNombre_en(String nombre_en) {
		this.nombre_en = nombre_en;
	}

	public String getDescripcion_en() {
		return descripcion_en;
	}

	public void setDescripcion_en(String descripcion_en) {
		this.descripcion_en = descripcion_en;
	}

	@Override
	public String toString() {
		return "Debilidad [idCWE=" + idCWE + ", nombre=" + nombre + ", descripcion=" + descripcion + "]";
	}
}
